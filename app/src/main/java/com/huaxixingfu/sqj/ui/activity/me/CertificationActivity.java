package com.huaxixingfu.sqj.ui.activity.me;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.SettingBar;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.RealNameDataBean;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.LogoutSubmitApi;
import com.huaxixingfu.sqj.http.api.RealNameApi;
import com.huaxixingfu.sqj.http.api.RealNameSubmitApi;
import com.huaxixingfu.sqj.http.api.UpdateImageApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.wildma.idcardcamera.camera.IDCardCamera;

import java.io.File;
import java.util.HashMap;

public class CertificationActivity extends AppActivity {


    private RealNameDataBean realNameData;

    public static void start(BaseActivity activity, String title, int isReal, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, CertificationActivity.class);
        intent.putExtra(IntentKey.TITLE, title);
        intent.putExtra(IntentKey.STATUS, isReal);
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

    private ImageView btn_front,btn_back;
    private SettingBar sb_setting_name,sb_setting_idcard;
    private String userName,cardNumber,cardFrontUrl,cardBackUrl,cardFrontUrltemp,cardBackUrltemp;
    private TextView tv_submit;
    private TitleBar tb_title;

    public CertificationActivity() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_certification;
    }

    public void initView() {

        String title = getString(IntentKey.TITLE);
        int isReal = getInt(IntentKey.STATUS);
        tb_title = findViewById(R.id.tb_title);
        tb_title.setTitle(title);
        tb_title.getRightView().setVisibility(View.INVISIBLE);


        btn_front = findViewById(R.id.btn_front);
        btn_back = findViewById(R.id.btn_back);

        sb_setting_name = findViewById(R.id.sb_setting_name);
        sb_setting_idcard = findViewById(R.id.sb_setting_idcard);
        tv_submit = findViewById(R.id.tv_submit);
        if(isReal == 1)
            tv_submit.setVisibility(View.GONE);

        setOnClickListener(btn_front,btn_back,sb_setting_name,sb_setting_idcard,tv_submit);

    }


    public void initData() {
        getRealNameDate();
    }

    private void getRealNameDate(){
        EasyHttp.post(this)
                .api(new RealNameApi())
                .request(new HttpCallback<HttpData<RealNameDataBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<RealNameDataBean> data) {
                        if(data.getData() != null){
                            realNameData = data.getData();
                            if(realNameData !=null){
                                if(StringUtils.isNotEmpty(realNameData.getUserName())){
                                    sb_setting_name.setRightText(realNameData.getUserName());
                                }
                                if(StringUtils.isNotEmpty(realNameData.getCardNumber())){
                                    sb_setting_idcard.setRightText(realNameData.getCardNumber());
                                }
                                if(StringUtils.isNotEmpty(realNameData.getCardFrontUrl())){
                                    Glide.with(getContext()).load(realNameData.getCardFrontUrl()).into(btn_front);
                                }
                                if(StringUtils.isNotEmpty(realNameData.getCardBackUrl())){
                                    Glide.with(getContext()).load(realNameData.getCardBackUrl()).into(btn_back);
                                }

                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void realNameSubmit(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        map.put("cardNumber", cardNumber);
        map.put("cardFrontUrl", cardFrontUrl);
        map.put("cardBackUrl", cardBackUrl);
        EasyHttp.post(this)
                .api(new RealNameSubmitApi())
                .json(map)
                .request(new HttpCallback<HttpData<RealNameSubmitApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<RealNameSubmitApi.Bean> data) {
                        if(data.getData() != null){
                            RealNameSubmitApi.Bean model = data.getData();
                            if(model.status){
                                ToastUtils.show("认证成功");
                                finishForResult("0");
                            }else{
                                ToastUtils.show(model.message);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void uploadFile(File file){
        EasyHttp.post(this)
                .api(new UpdateImageApi()
                        .setImage(file))
                .request(new HttpCallback<HttpData<UpdateImageApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<UpdateImageApi.Bean> data) {
                        if(data.getData() != null){
                            UpdateImageApi.Bean model = data.getData();
                            if(StringUtils.isNotEmpty(model.httpHost) && StringUtils.isNotEmpty(model.imageUri)){
                                cardFrontUrl = model.httpHost+model.imageUri;
                                cardFrontUrltemp = model.imageUri;
                                Glide.with(getContext()).load(cardFrontUrl).into(btn_front);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void uploadBackFile(File file){
        EasyHttp.post(this)
                .api(new UpdateImageApi()
                        .setImage(file))
                .request(new HttpCallback<HttpData<UpdateImageApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<UpdateImageApi.Bean> data) {
                        if(data.getData() != null){
                            UpdateImageApi.Bean model = data.getData();
                            if(StringUtils.isNotEmpty(model.httpHost) && StringUtils.isNotEmpty(model.imageUri)){
                                cardBackUrl = model.httpHost+model.imageUri;
                                cardBackUrltemp = model.imageUri;
                                Glide.with(getContext()).load(cardBackUrl).into(btn_back);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_front) {
            if(realNameData.getCardStartus() == 2)return;
            IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);

        }else if(id == R.id.btn_back){
            if(realNameData.getCardStartus() == 2)return;
            IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
        }else if(id == R.id.sb_setting_name){
            if(realNameData.getCardStartus() == 2)return;
            InputTextActivity.start(CertificationActivity.this, "输入姓名", "",10,new InputTextActivity.OnFinishResultListener() {
                @Override
                public void onSucceed(String data) {
                    sb_setting_name.setRightText(data);
                    userName = data;
                }
            });
        }else if(id == R.id.sb_setting_idcard){
            if(realNameData.getCardStartus() == 2)return;
            InputTextActivity.start(CertificationActivity.this, "输入身份证号", "",0,new InputTextActivity.OnFinishResultListener() {
                @Override
                public void onSucceed(String data) {
                    sb_setting_idcard.setRightText(data);
                    cardNumber = data;
                }
            });
        }else if(id == R.id.tv_submit){
            realNameSubmit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面

                    uploadFile(new File(path));
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面

                    uploadBackFile(new File(path));
                }


                //将图片上传到服务器成功后需要删除全部缓存图片
                //FileUtils.clearCache(this);
            }
        }
    }
}
