package com.huaxixingfu.sqj.widget.tuita;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.widget.tuita.recycler.RecyclerAdapter;

/**
 * @version 1.0.0
 */
public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {
    ImageView mFace;

    public FaceHolder(View itemView) {
        super(itemView);
        mFace = itemView.findViewById(R.id.im_face);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null
                // drawable 资源 id
                && ((bean.preview instanceof Integer)
                // face zip 包资源路径
                || bean.preview instanceof String))
            Glide.with(itemView.getContext())
            .asBitmap()
                    .load(bean.preview)
                    .format(DecodeFormat.PREFER_ARGB_8888) //设置解码格式8888，保证清晰度
                    .placeholder(R.drawable.default_face)
                    .into(mFace);
    }
}
