package com.leo.wan.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期转化工具
 *
 * @author lanyan
 */
public class DateUtil {

    /**
     * 将long型格式转化为所需格式的日期，以字符串返回
     *
     * @param time    需要转化的时间，单位是毫秒
     * @param pattern 待转化的格式
     * @return
     */
    public static String format(long time, String pattern) {
        if (time < 0 || TextUtils.isEmpty(pattern)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    /**
     * 将String型格式转化为所需格式的日期，以Date返回
     *
     * @param time    需要转化的时间
     * @param pattern 待转化的格式
     * @return
     */
    public static Date format(String time, String pattern) throws ParseException {
        if (TextUtils.isEmpty(time) || TextUtils.isEmpty(pattern)) {
            return new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
        return sdf.parse(time);
    }

    /**
     * 将string型格式转化为所需格式的日期，以字符串返回
     *
     * @param time     需要转化的时间
     * @param previous 转化前的格式
     * @param pattern  待转化的格式
     * @return
     */
    public static String format(String time, String previous, String pattern) {
        if (TextUtils.isEmpty(time) || TextUtils.isEmpty(pattern)) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(previous, Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            Date dt = sdf1.parse(time);
            return sdf2.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static long getTimeMillis(String time, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            Date date = df.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取与当前时间的间隔毫秒数
     *
     * @param time
     * @param pattern
     * @return
     */
    public static long getIntervalTime(String time, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            Date date = df.parse(time);
            return date.getTime() - System.currentTimeMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将毫秒数转成天 小时 分 秒
     *
     * @param time
     * @return
     */
    public static String calculateTimeBySec(long time) {
        if (time < 0) {
            return null;
        }
        time = System.currentTimeMillis() - time;
        int days = (int) (time / 86400000);
        int hour = (int) ((time - days * 86400000) / 3600000);
        int minute = (int) ((time - days * 86400000 - hour * 3600000) / 60000);
        int second = Math.abs((int) ((time - days * 86400000 - hour * 3600000 - minute * 60000) / 1000));
        if (days < 1) {
            if (hour < 1) {
                if (minute < 1) {
                    return second + "秒";
                } else {
                    return minute + "分钟";
                }
            } else {
                return hour + "小时";
            }
        } else {
            return days + "天";
        }
    }

    public static String calculateTimeBy(long time) {
        if (time < 0) {
            return null;
        }
        time = time - System.currentTimeMillis();
        int days = (int) (time / 86400000);
        int hour = (int) ((time - days * 86400000) / 3600000);
        int minute = (int) ((time - days * 86400000 - hour * 3600000) / 60000);
        int second = Math.abs((int) ((time - days * 86400000 - hour * 3600000 - minute * 60000) / 1000));
        if (days < 30) {
            if (hour < 1) {
                if (minute < 1) {
//                    return second + "秒";
                    return "";
                } else {
                    return minute + "分钟";
                }
            } else {
                return days + "天" + hour + "小时" + minute + "分钟";
            }
        } else {
            return days + "天";
        }
    }

    public static String calculateTimeByResult(long time) {
        if (time < 0) {
            return null;
        }
        int days = (int) (time / 86400000);
        int hour = (int) ((time - days * 86400000) / 3600000);
        int minute = (int) ((time - days * 86400000 - hour * 3600000) / 60000);
        int second = Math.abs((int) ((time - days * 86400000 - hour * 3600000 - minute * 60000) / 1000));
        if (days < 30) {
            if (hour < 1) {
                if (minute < 1) {
//                    return second + "秒";
                    return "";
                } else {
                    return minute + "分钟";
                }
            } else {
                return days + "天" + hour + "小时" + minute + "分钟";
            }
        } else {
            return days + "天";
        }
    }


    public static String converVideoTime(long time) {
        if (time < 0) {
            return null;
        }
        int times = (int) (time / 1000);

        //小时计算
        int hours = (int) ((times) % (24 * 3600) / 3600);

        //分钟计算
        int minutes = (int) ((times) % 3600 / 60);

        //秒计算
        int second = (int) ((times) % 60);


        if (hours == 0 && minutes == 0) {
            if (second < 10) {
                return String.valueOf("00:00:0" + second);
            } else {
                return String.valueOf("00:00:" + second);
            }
        }

        if (hours == 0) {
            String resultSecond;
            if (second < 10) {
                resultSecond = "0" + second;
            } else {
                resultSecond = String.valueOf(second);
            }
            if (minutes < 10) {
                return String.valueOf("00:0" + minutes + ":") + resultSecond;
            } else {
                return String.valueOf("00:" + minutes + ":") + resultSecond;
            }
        }

        String resultSecond;
        if (second < 10) {
            resultSecond = "0" + second;
        } else {
            resultSecond = String.valueOf(second);
        }
        String resultMinute;
        if (minutes < 10) {
            resultMinute = String.valueOf("0" + minutes + ":") + resultSecond;
        } else {
            resultMinute = String.valueOf(minutes + ":") + resultSecond;
        }

        if (hours < 10) {
            return String.valueOf("0" + hours + ":") + resultMinute;
        } else {
            return String.valueOf(hours + ":") + resultMinute;
        }
    }

    /**
     * 判断日期是否有效，即是否大于等于今天
     *
     * @param date
     * @param format
     * @return
     */
    public static boolean dateValidate(String date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
            String curDate = formatter.format(new Date());
            long curTimeMillis = formatter.parse(curDate).getTime();
            long targetTimeMillis = formatter.parse(date).getTime();
            return targetTimeMillis >= curTimeMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 比较2个时间的大小
     *
     * @param time1   第一个时间
     * @param format1 第一个时间的时间个时
     * @param time2   第二个时间
     * @param format2 第二个时间的时间格式
     * @return 1 第一个时间迟;-1 第二个时间迟;0 相等
     */
    public static int compareDates(String time1, String format1, String time2,
                                   String format2) {
        if (TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)
                || TextUtils.isEmpty(format1) || TextUtils.isEmpty(format2)) {
            return 0;
        }
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(format1, Locale.CHINA);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.CHINA);
            Date d1 = sdf1.parse(time1);
            Date d2 = sdf2.parse(time2);
            if (d1.after(d2)) {
                return 1;
            } else if (d1.before(d2)) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取一个月份的总天数
     *
     * @param time
     * @param pattern
     * @return
     */
    public static int getMonthDays(String time, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date date = format.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getActualMaximum(Calendar.DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取一个月份的总天数
     *
     * @param time
     * @return
     */
    public static int getMonthDays(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.getActualMaximum(Calendar.DATE);
    }


    public static String getOldDate(String signDate, int distanceDay, String format) {
        try {
            SimpleDateFormat dft = new SimpleDateFormat(format);
            Date beginDate = null;
            beginDate = dft.parse(signDate);

            Calendar date = Calendar.getInstance();
            date.setTime(beginDate);
            date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
            Date
                    endDate = dft.parse(dft.format(date.getTime()));
            return dft.format(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 计算时间差
     *
     * @param inputTime
     * @return
     */
    public static String getTimeInterval(String inputTime) {
        if (inputTime.length() != 19) {
            return inputTime;
        }
        String result = null;
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //  ParsePosition 是指从哪个位置开始索引
            ParsePosition pos = new ParsePosition(0);
            Date d1 = sd.parse(inputTime, pos);
            // 用现在距离1970年的时间间隔new
            // Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
            long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒
            if (time / 1000 < 60) {
                result = "刚刚";
            } else if (time / 60000 < 60) {
                result = time / 60000 + "分钟前";
            } else if (time / 3600000 < 24) {
                result = time / 3600000 + "小时前";
            } else if (time / 86400000 < 2) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                result = sdf.format(d1.getTime());
                result = "昨天" + result;
            } else if (time / 86400000 < 3) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                result = sdf.format(d1.getTime());
                result = "前天" + result;
            } else if (time / 86400000 < 30) {
                result = time / 86400000 + "天前";
            } else if (time / 86400000 < 60) {
                result = "1个月前";
            } else if (time / 86400000 < 90) {
                result = "2个月前";
            } else if (time / 86400000 < 120) {
                result = "3个月前";
            } else if (time / 86400000 < 150) {
                result = "4个月前";
            } else if (time / 86400000 < 180) {
                result = "5个月前";
            } else if (time / 86400000 < 210) {
                result = "6个月前";
            } else if (time / 86400000 < 240) {
                result = "7个月前";
            } else if (time / 86400000 < 270) {
                result = "8个月前";
            } else if (time / 86400000 < 300) {
                result = "9个月前";
            } else if (time / 86400000 < 330) {
                result = "10个月前";
            } else if (time / 86400000 < 360) {
                result = "11个月前";
            } else if (time / 86400000 < 720) {
                result = "1年前";
            } else if (time / 86400000 < 1080) {
                result = "2年前";
            } else {
                // 大于2年，显示年月日时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                result = sdf.format(d1.getTime());
            }
        } catch (Exception e) {
            return inputTime;
        }
        return result;
    }

    /**
     * 判断周几
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    //获取视频时间
    public static boolean judgeVideoTime(String videoUrl, Context context) {
        int seconds = 0;
        String[] projection = new String[]{MediaStore.Video.Media.DURATION};
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                , projection, MediaStore.Video.Media.DATA + "='" + videoUrl + "'", null, null);
        if (cursor != null) {
            if (cursor.getCount() == 1) {


                cursor.moveToFirst();
                long duration = 0;
                if (cursor.moveToFirst()) {
                    do {
                        duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    } while (cursor.moveToNext());
                }
                seconds = (int) duration / 1000;

            }

        }
        cursor.close();

        if (seconds <= 30) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @return
     */
    public static String addCurrentEveryDay(Date date, int day) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, day);
        return sf.format(c.getTime());
    }

    public static Long coverCurrentEveryDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, day);
        return c.getTimeInMillis();
    }


}
