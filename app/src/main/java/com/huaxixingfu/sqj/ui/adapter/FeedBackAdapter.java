package com.huaxixingfu.sqj.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppApplication;
import com.huaxixingfu.sqj.bean.TypeBean;

import java.util.List;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.TypeViewHolder> {

    private Activity activity;
    private List<TypeBean> typeBeanList;
    private OnItemClickListener listener;

    public FeedBackAdapter(Activity activity, List<TypeBean> typeBeanList, OnItemClickListener listener) {
        this.activity = activity;
        this.typeBeanList = typeBeanList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClickListener(View view, int position,TypeBean typeBean);
    }
    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.sqj_item_feedback_type, parent, false);
        return new TypeViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (typeBeanList != null && typeBeanList.size() > 0) {
            TypeBean typeBean = typeBeanList.get(position);
            if (typeBean != null) {
                holder.typeName.setText(typeBean.codeName);
                holder.typeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        typeBean.setSelected(!typeBean.isSelected);
                        listener.onItemClickListener(v,position,typeBean);
                    }
                });
//                typeBean.setSelected(!typeBean.isSelected);
                if (typeBean.isSelected) {
                    holder.typeName.setBackgroundResource(R.drawable.bg_question_type_red_oval);
                    holder.typeName.setTextColor(AppApplication.getContext().getColor(R.color.main));

                } else {
                    holder.typeName.setBackgroundResource(R.drawable.bg_question_type_oval);
                    holder.typeName.setTextColor(AppApplication.getContext().getColor(R.color.color_ff999999));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return typeBeanList.size();
    }

    class TypeViewHolder extends RecyclerView.ViewHolder {
        TextView typeName;
        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            typeName = itemView.findViewById(R.id.tvTypeName);
        }
    }

}
