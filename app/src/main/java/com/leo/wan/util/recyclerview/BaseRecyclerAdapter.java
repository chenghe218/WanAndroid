package com.leo.wan.util.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.leo.wan.R;

import java.util.List;


/**
 * Created by zhy on 16/6/23.
 */
public abstract class BaseRecyclerAdapter<T> extends MultiItemTypeAdapter<T> {

    protected static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;
    protected static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private View mEmptyView;
    private int mEmptyLayoutId;

    protected View mLoadMoreView;
    protected int mLoadMoreLayoutId;
    /**
     * 初始化的时候 是否有数据  避免Adapter构造函数未传递datas的时候显示EmptyView
     * 正常逻辑应该是网络请求加载完之后 再判断是否有数据 是否显示EmptyView
     */
    public boolean isInitData;

    public BaseRecyclerAdapter(final Context context, final int layoutId, List<T> ts) {
        super(context, ts);
        isInitData = true;
        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                BaseRecyclerAdapter.this.convert(holder, t, position);
            }
        });
    }

    public BaseRecyclerAdapter(final Context context, final int layoutId) {
        super(context);
        isInitData = false;
        mEmptyLayoutId = R.layout.view_nodata;
        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                BaseRecyclerAdapter.this.convert(holder, t, position);
            }
        });
    }

    public BaseRecyclerAdapter(final Context context) {
        super(context);
        isInitData = false;
    }

    @Override
    public void setDatas(List<T> datas) {
        super.setDatas(datas);
        isInitData = true;
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    protected void convertEmptyView(ViewHolder holder) {
    }

    private boolean hasLoadMore() {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0;
    }

    private boolean isEmpty() {
        return (mEmptyView != null || mEmptyLayoutId != 0) && super.getItemCount() == 0 && isInitData;
    }

    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= super.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmpty()) {
            return ITEM_TYPE_EMPTY;
        }
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (isEmpty()) {
            ViewHolder holder;
            if (mEmptyView != null) {
                holder = (ViewHolder) ViewHolder.createViewHolder(parent.getContext(), mEmptyView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mEmptyLayoutId);
                mEmptyView = holder.itemView;
            }
            return holder;
        }
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            if (mLoadMoreView != null) {
                holder = (ViewHolder) ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mLoadMoreLayoutId);
            }
            return holder;
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isEmpty()) {
            convertEmptyView(holder);
            return;
        }
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    /*@Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isEmpty() || isShowLoadMore(position)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }*/

    @Override
    public boolean isFullSpan(int position) {
        return isEmpty() || isShowLoadMore(position);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isEmpty() || isShowLoadMore(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) {
            return 1;
        }
        return super.getItemCount() + (hasLoadMore() ? 1 : 0);
    }

    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mOnLoadMoreListener = loadMoreListener;
    }

    public void setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
    }

    public void setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public View getmEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(int layoutId) {
        mEmptyLayoutId = layoutId;
    }

}
