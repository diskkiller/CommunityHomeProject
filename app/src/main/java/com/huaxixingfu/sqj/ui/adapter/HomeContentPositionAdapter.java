package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.listener.OnHttpListener;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.http.api.BarListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.ui.activity.home.HomeCenterActivity;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

/**
 *    desc   : 首页金刚位列表
 */
public final class HomeContentPositionAdapter extends AppAdapter<BarListApi.Bean> {


    private LifecycleOwner lifecycleOwner;
    private OnHttpListener listener;

    public HomeContentPositionAdapter(Context context, LifecycleOwner lifecycleOwner, OnHttpListener listener) {
        super(context);
        this.lifecycleOwner =  lifecycleOwner;
        this.listener =  listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewNormalHolder();

    }

    public final class ViewNormalHolder extends ViewHolder {

        private final TextView itemText;
        private final ImageView itemImg;

        private ViewNormalHolder() {
            super(R.layout.sqj_fragment_home_position_item);
            itemText = findViewById(R.id.iv_text);
            itemImg = findViewById(R.id.iv_img);
        }

        @Override
        public void onBindView(int position) {
            BarListApi.Bean bean = getItem(position);
            if(bean == null){
                return;
            }
            String url = bean.sysBarImageUrl;
            itemText.setText(bean.sysBarTitle);
            if(StringUtils.isNotEmpty(url)){
                itemImg.setVisibility(View.VISIBLE);
                GlideApp.with(getContext())
                        .load(url)
                        .centerCrop()
                        .into(itemImg);
            }
            getItemView().setOnClickListener(view->{

                if(bean.isLoginFlag && !SPManager.instance(getContext()).isLogin()){

                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                HomeCenterActivity.start((BaseActivity) getContext(), bean);

            });
        }
    }
}