package com.huaxixingfu.sqj.ui.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.HomeCenterBean;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.HomeCenterApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.position.news.SimpleNewListActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：党建中心
 */

public class HomeCenterActivity extends AppActivity{

    private TextView tv_day_count,tv_total_count,tv_title,tv_content;

    private HomeCenterBean homeCenterBean;

    private LinearLayout ll_center_group,ll_center_content;

    private String sysBarTitle,newsColumnCode;

    private int sysBarId;

    public static void start(BaseActivity activity, String sysBarTitle,String newsColumnCode,int sysBarId,  HomeCenterGroupListActivity.OnFinishResultListener listener) {
        Intent intent = new Intent(activity, HomeCenterGroupListActivity.class);
        intent.putExtra(IntentKey.TITLE, sysBarTitle);
        intent.putExtra(IntentKey.CODE, newsColumnCode);
        intent.putExtra(IntentKey.ID, sysBarId);
        activity.startActivityForResult(intent, (resultCode, data) -> {
            if (listener == null) {
                return;
            }
            if (resultCode == RESULT_OK) {
                listener.onSucceed(data.getStringExtra(IntentKey.STRING_DATE));
            } else {
                listener.onFail();
            }
        });
    }

    public void finishForResult(String date) {
        setResult(RESULT_OK, new Intent()
                .putExtra(IntentKey.STRING_DATE, date));
        finish();
    }

    /**
     * 注册监听
     */
    public interface OnFinishResultListener {

        void onSucceed(String data);

        default void onFail() {}
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_home_center_activity;
    }

    @Override
    protected void initView() {
        tv_day_count = findViewById(R.id.tv_day_count);
        tv_total_count = findViewById(R.id.tv_total_count);
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        ll_center_group = findViewById(R.id.ll_center_group);
        ll_center_content = findViewById(R.id.ll_center_content);

        setOnClickListener(R.id.ll_center_group,R.id.ll_center_content);

        sysBarTitle = getString(IntentKey.TITLE);
        newsColumnCode = getString(IntentKey.CODE);
        sysBarId = getInt(IntentKey.ID);
        setTitle(sysBarTitle);

    }


    /**
     * 23 党政中心  27 文化中心
     */
    protected void initData(){
        Map<String,Object> map = new HashMap();
        map.put("sysBarId",sysBarId);
        EasyHttp.post(this)
                .api(new HomeCenterApi())
                .json(map)
                .request(new HttpCallback<HttpData<HomeCenterBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<HomeCenterBean> data) {
                        if(data.getData() != null){
                            homeCenterBean = data.getData();
                            tv_day_count.setText(homeCenterBean.sysBarAccessDayNum+"");
                            tv_total_count.setText(homeCenterBean.sysBarAccessTotalNum+"");
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.ll_center_group){
            HomeCenterGroupListActivity.start(HomeCenterActivity.this,homeCenterBean.chatRoomVOList,null);
        }else if(id == R.id.ll_center_content){
            SimpleNewListActivity.start((BaseActivity) getContext(),sysBarTitle,newsColumnCode);
        }

    }
}
