package com.huaxixingfu.sqj.ui.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.base.BaseDialog;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppApplication;
import com.huaxixingfu.sqj.bean.HomeCenterBean;
import com.huaxixingfu.sqj.bean.PersonDataBean;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.commom.SysBarType;
import com.huaxixingfu.sqj.http.api.BarListApi;
import com.huaxixingfu.sqj.http.api.HomeCenterApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.me.PersonalDataActivity;
import com.huaxixingfu.sqj.ui.activity.msg.ResidentListActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.activity.position.HallActivity;
import com.huaxixingfu.sqj.ui.activity.position.news.SimpleNewListActivity;
import com.huaxixingfu.sqj.ui.dialog.MessageDialog;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：党建中心
 */

public class HomeCenterActivity extends AppActivity{

    private TextView tv_day_count,tv_total_count,tv_title,tv_content, tv_sub_content, tv_sub_title;

    private HomeCenterBean homeCenterBean;

    private LinearLayout ll_center_group,ll_center_content;

//    private String sysBarTitle,newsColumnCode;

//    private int sysBarId;

    private BarListApi.Bean data;

    public static void start(BaseActivity activity, String sysBarTitle,String newsColumnCode,int sysBarId,  HomeCenterGroupListActivity.OnFinishResultListener listener) {
        Intent intent = new Intent(activity, HomeCenterActivity.class);
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

    public static void start(BaseActivity activity, BarListApi.Bean data){
        Intent intent = new Intent(activity, HomeCenterActivity.class);
        intent.putExtra(IntentKey.OBJECT, data);
        activity.startActivity(intent);
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
        tv_sub_content = findViewById(R.id.tv_sub_content);
        tv_sub_title = findViewById(R.id.tv_sub_title);
        tv_content = findViewById(R.id.tv_content);
        ll_center_group = findViewById(R.id.ll_center_group);
        ll_center_content = findViewById(R.id.ll_center_content);

        setOnClickListener(R.id.ll_center_group,R.id.ll_center_content);
        data = getParcelable(IntentKey.OBJECT);
//        sysBarTitle = getString(IntentKey.TITLE);
//        newsColumnCode = getString(IntentKey.CODE);
//        sysBarId = getInt(IntentKey.ID);
        setTitle(data.sysBarTitle);
        tv_title.setText(data.sysBarTitle+"群");
        tv_content.setText(data.sysBarTitle+"内容");
        if (!"DANGJIANZHONGXIN".equals(data.sysBarCode)){
            tv_sub_title.setVisibility(View.INVISIBLE);
            tv_sub_content.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 23 党政中心  27 文化中心
     */
    protected void initData(){
        Map<String,Object> map = new HashMap();
        map.put("sysBarId",data.sysBarId);
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
                            if (homeCenterBean.chatRoomVOList == null || homeCenterBean.chatRoomVOList.size() < 1){
                                ll_center_group.setVisibility(View.GONE);
                            }

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
            if(SPManager.instance(AppApplication.getInstances()).
                    getModel(Constants.USERINFO, PersonDataBean.class).
                    getUserResidentCertStatus() == 2){
                HomeCenterGroupListActivity.start(HomeCenterActivity.this,homeCenterBean.chatRoomVOList,null);
            }else{
                new MessageDialog.Builder(getContext())
                        .setTitle("温馨提示")
                        .setMessage("请完成居民认证后再去操作")
                        .setCancelable(false)
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {

                            }
                        })
                        .show();

            }
        }else if(id == R.id.ll_center_content){
            routerJump(data);
        }

    }



    /**
     * 调整类型： sys_bar_type 类型：
     *      * THIS_SYS (0, "本系统"),
     *      * EXTERNAL_LINKS(1, "外部链接"),
     *      * APP(2, "app应用"),  //没有使用
     *      * NEWS(3, "新闻"),
     *      * NOTICE(4, "公告"),  //没有使用
     *      * FUNCTIONS_TO_DEVELOPED(5, "待开发功能"),
     *      * PAGE_TO_DEVELOPED(6, "待开发页面"),
     */
    private void routerJump(BarListApi.Bean bean){
        switch(bean.sysBarType){
            case SysBarType.NATIVE:
                nativeFoward(bean.sysBarUrl);
                break;
            case SysBarType.H5:
                if(StringUtils.isNotEmpty(bean.sysBarUrl)){
                    BrowserActivity.start(getContext(),bean.sysBarUrl,bean.sysBarPosition);
                }else {
                    ToastUtils.show("sysBarUrl is null");
                }
                break;
            case SysBarType.NATIVE_APP:
                ToastUtils.show("暂未使用");
                break;
            case SysBarType.NEWS:
                SimpleNewListActivity.start((BaseActivity) getContext(),bean.sysBarTitle,bean.sysBarCode);
                break;
            case SysBarType.NOTICE:
                ToastUtils.show("暂未使用");

                break;
            case SysBarType.FUNCTIONS_TO_DEVELOPED:
                ToastUtils.show("待开发功能");

                break;
            case SysBarType.PAGE_TO_DEVELOPED:
                ToastUtils.show("待开发页面");

                break;
        }

    }


    private void  nativeFoward(String switchtext){
        switch (switchtext){
            case "CommunityHome://home-officehall"://   办事大厅
                getContext().startActivity(new Intent(getContext(), HallActivity.class));
                break;
            case "CommunityHome://home-epidemicdynamic"://  疫情防控
                BrowserActivity.start(getContext(),"https://news.qq.com/zt2020/page/feiyan.htm#/?ADTAG=chunyun2021","疫情防控");
                break;
            case "CommunityHome://home-socialworker"://    社工在线
                if(SPManager.instance(AppApplication.getInstances()).
                        getModel(Constants.USERINFO, PersonDataBean.class).
                        getUserResidentCertStatus() == 2){
                    getContext().startActivity(new Intent(getContext(), ResidentListActivity.class));
                }else{
                    new MessageDialog.Builder(getContext())
                            .setTitle("温馨提示")
                            .setMessage("请完成居民认证后再去操作")
                            .setCancelable(false)
                            .setListener(new MessageDialog.OnListener() {
                                @Override
                                public void onConfirm(BaseDialog dialog) {
                                    startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {

                                }
                            })
                            .show();

                }

                break;

            default:

        }


    }


}
