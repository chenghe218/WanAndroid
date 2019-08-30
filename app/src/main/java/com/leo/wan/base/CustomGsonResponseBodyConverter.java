package com.leo.wan.base;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * @author zxy
 * @date 2018/9/27
 * Describe:
 */
public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        /**
         * 在解析前 先拿到请求返回码 判断是否成功
         * 如请求失败 抛出异常 不在进行解析
         */
        String response = value.string();
        BaseBean httpStatus = gson.fromJson(response, BaseBean.class);
        if (httpStatus.getErrorCode() == HttpStatusConstants.SUCCESS) {
            /**
             * 解析源码
             * ResponseBody 只能读取一次 上面已经value.string() 所以value.charStream()会报错
             */
            //   JsonReader jsonReader = gson.newJsonReader(value.charStream());
            MediaType contentType = value.contentType();
            Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
            InputStream inputStream = new ByteArrayInputStream(response.getBytes());
            Reader reader = new InputStreamReader(inputStream, charset);

            JsonReader jsonReader = gson.newJsonReader(reader);
            try {
                T result = adapter.read(jsonReader);
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
                return result;
            } finally {
                value.close();
            }
        }else {
            value.close();
            throw new ApiException(httpStatus.getErrorCode(), httpStatus.getErrorMsg());
        }
    }
}
