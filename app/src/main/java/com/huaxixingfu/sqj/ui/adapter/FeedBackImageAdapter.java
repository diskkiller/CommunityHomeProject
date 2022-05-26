package com.huaxixingfu.sqj.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.FeedBackImageBean;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.List;

public class FeedBackImageAdapter extends RecyclerView.Adapter<FeedBackImageAdapter.TypeViewHolder> {

    private Activity activity;
    private List<FeedBackImageBean> typeBeanList;
    private OnItemClickListener listener;

    public List<FeedBackImageBean> getTypeBeanList() {
        return typeBeanList;
    }

    public void setTypeBeanList(List<FeedBackImageBean> typeBeanList) {
        this.typeBeanList = typeBeanList;
    }

    public FeedBackImageAdapter(Activity activity, List<FeedBackImageBean> typeBeanList, OnItemClickListener listener) {
        this.activity = activity;
        this.typeBeanList = typeBeanList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClickListener(View view, int position,FeedBackImageBean typeBean);
    }
    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.sqj_item_feedback_image, parent, false);
        return new TypeViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (typeBeanList != null && typeBeanList.size() > 0) {
            FeedBackImageBean typeBean = typeBeanList.get(position);
            if (typeBean != null) {

                switch (typeBean.defualt){

                    case FeedBackImageBean.TYPE_DEFUALT:
                        holder.typeName.setImageResource(R.mipmap.ic_add_pic);
                        holder.typeNameClose.setVisibility(View.GONE);
                        break;
                    case FeedBackImageBean.TYPE_IMAGE_LOCAL:
                        Glide.with(activity).load(typeBean.imageUrl).centerCrop().into(holder.typeName);


                        if(typeBean.del){
                            holder.typeNameClose.setVisibility(View.VISIBLE);
                        }else{
                            holder.typeNameClose.setVisibility(View.INVISIBLE);
                            holder.typeName.setImageResource(R.color.color_ffffff);
                        }
                        break;
                    case FeedBackImageBean.TYPE_IMAGE_HTTP:
                        Glide.with(activity).load(typeBean.imageUrlHttp).centerCrop().into(holder.typeName);
                        if(typeBean.del){
                            holder.typeNameClose.setVisibility(View.VISIBLE);
                        }else{
                            holder.typeNameClose.setVisibility(View.INVISIBLE);
                            holder.typeName.setImageResource(R.color.color_ffffff);
                        }
                        break;
                }
                holder.typeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(typeBean.defualt == FeedBackImageBean.TYPE_DEFUALT){
                            listener.onItemClickListener(v,position,typeBean);
                        }
                    }
                });
                holder.typeNameClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(typeBean.defualt != FeedBackImageBean.TYPE_DEFUALT){
                            listener.onItemClickListener(v,position,typeBean);
                        }
                    }
                });

            }
        }
    }

    @Override
    public int getItemCount() {
        return typeBeanList.size();
    }

    class TypeViewHolder extends RecyclerView.ViewHolder {
        ImageView typeName;
        ImageView typeNameClose;
        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            typeName = itemView.findViewById(R.id.iv_photo);
            typeNameClose = itemView.findViewById(R.id.iv_close);
        }
    }

}
