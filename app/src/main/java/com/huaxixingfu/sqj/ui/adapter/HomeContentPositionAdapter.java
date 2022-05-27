package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.http.api.BarListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.ui.activity.home.HomeCenterActivity;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.ui.activity.msg.ResidentListActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.activity.position.HallActivity;
import com.huaxixingfu.sqj.ui.activity.position.news.SimpleNewListActivity;
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
               switch (bean.sysBarType){
                   case NATIVE_0:
                       if ("WENHUAZHONGXIN".equals(bean.sysBarCode) || "DANGJIANZHONGXIN".equals(bean.sysBarCode)){
                           HomeCenterActivity.start((BaseActivity) getContext(), bean.sysBarTitle, bean.sysBarCode,
                                   bean.sysBarId, null);
                       }else if("/sggl/mobile/home/v1.0/news/page".equals(bean.sysBarUrl) && StringUtils.isNotEmpty(bean.sysBarCode)){
                           SimpleNewListActivity.start((BaseActivity) getContext(),bean.sysBarTitle,bean.sysBarCode);
                       }else{

                           nativeFoward(bean.sysBarUrl);
                       }
                       break;
                   case H5_STATIC_2:
                   case H5_PARAM_1:
                       if(StringUtils.isNotEmpty(bean.sysBarUrl)){
                           BrowserActivity.start(getContext(),bean.sysBarUrl,bean.sysBarPosition);
                       }
                       break;
                   case EMPTY_PAGE_DEVELOP_4:
                   case TOAST_DEVELOP_3:
                   default:
                       ToastUtils.show(getString(R.string.position_need_develop_toast));
               }

            });
        }


        private void  nativeFoward(String switchtext){


            switch (switchtext){
                case "CommunityHome://home-officehall":
//                办事大厅
                    getContext().startActivity(new Intent(getContext(), HallActivity.class));
                    break;
                case "CommunityHome://home-epidemicdynamic":

                    BrowserActivity.start(getContext(),"https://news.qq.com/zt2020/page/feiyan.htm#/?ADTAG=chunyun2021","疫情防控");
//                疫情防控
                    break;
                case "CommunityHome://home-socialworker":
                    getContext().startActivity(new Intent(getContext(), ResidentListActivity.class));
//                社工在线
                    break;








                default:

            }


        }

        private final  int NATIVE_0 = 0;
        private final  int H5_PARAM_1 = 1;
        private final  int H5_STATIC_2 = 2;
        private final  int TOAST_DEVELOP_3 = 3;
        private final  int EMPTY_PAGE_DEVELOP_4 = 5;

    }
}