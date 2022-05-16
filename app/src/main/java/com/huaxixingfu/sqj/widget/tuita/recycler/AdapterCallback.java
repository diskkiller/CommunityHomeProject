package com.huaxixingfu.sqj.widget.tuita.recycler;

/**
 * @version 1.0.0
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
