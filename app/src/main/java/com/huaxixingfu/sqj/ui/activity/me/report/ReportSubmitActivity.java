package com.huaxixingfu.sqj.ui.activity.me.report;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.CheckNet;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.FeedBackImageBean;
import com.huaxixingfu.sqj.bean.VSucess;
import com.huaxixingfu.sqj.http.api.FeedBackApi;
import com.huaxixingfu.sqj.http.api.UpdateImageApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.FeedBackImageAdapter;
import com.huaxixingfu.sqj.utils.GlideEngine;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  提交举报
 */
public class ReportSubmitActivity extends AppActivity{

    private RecyclerView rc_image;
    private EditText etDescribe;
    private String feedbackText;
    private FeedBackImageAdapter imageAdapter;

    private int selectTypeBean = -1;
    private String selectTypeBeanType = "";

    private List<FeedBackImageBean> listImage = new ArrayList<>();

    public static  final String TITLE_REQUEST = "TITLE_REQUEST";

    @CheckNet
//    @Log
    public static void start(BaseActivity activity, String content) {
        Intent intent = new Intent(activity, ReportSubmitActivity.class);
        intent.putExtra(TITLE_REQUEST, content);
        activity.startActivity(intent);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_report_submit;
    }

    @Override
    protected void initView() {

        selectTypeBeanType = getIntent().getStringExtra(TITLE_REQUEST);
        initEvent();
    }

    public void initData() {

    }


    private void feedback(int sysFeedbackType,String sysFeedbackContent, List<String> sysFeedbackImageList){
        HashMap<String, Object> map = new HashMap<>();
        map.put("sysFeedbackType", sysFeedbackType);
        map.put("sysFeedbackContent", sysFeedbackContent);
        map.put("sysFeedbackImageList", sysFeedbackImageList);
        EasyHttp.post(this)
                .api(new FeedBackApi())
                .json(map)
                .request(new HttpCallback<HttpData<VSucess>>(this) {

                    @Override
                    public void onSucceed(HttpData<VSucess> mdata) {
                        if(mdata.getData() != null){
                            VSucess data = mdata.getData();

                            if(null != data){
                                boolean isstatus = data.status;
                                if(isstatus){
                                    ToastUtils.show("感谢您的反馈,我们会及时处理");
                                    new Handler().postDelayed(()->{
                                        ReportSubmitActivity.this.finish();
                                    },2000);
                                }else{
                                    String message = data.message;

                                    if(!TextUtils.isEmpty(message)){
                                        ToastUtils.show(message);
                                    }
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

    private void initEvent() {
        findViewById(R.id.tv_submit).setOnClickListener(this);
        setRvImageInit();
        etDescribe = findViewById(R.id.et_describe);
        TextView tvHint = findViewById(R.id.tvHint);

        TextView tvQuestionType = findViewById(R.id.tvQuestionType);
        tvQuestionType.setText(selectTypeBeanType);

        etDescribe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvHint.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {
                feedbackText = etDescribe.getText().toString();
                if (TextUtils.isEmpty(feedbackText)) {
                    tvHint.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setRvImageInit(){
        rc_image = findViewById(R.id.rc_image);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        rc_image.setLayoutManager(layoutManager);
        listImage.add(new FeedBackImageBean());
        imageAdapter = new FeedBackImageAdapter(ReportSubmitActivity.this,
                listImage, (View view, int position, FeedBackImageBean typeBean) ->{
            if(typeBean.defualt == FeedBackImageBean.TYPE_DEFUALT){
                setOpenAlbum(Math.max(9 + 1 - listImage.size() , 0));
            }else{
                imageAdapter.getTypeBeanList().remove(typeBean);
                if(imageAdapter.getTypeBeanList().size() > 0 &&
                        imageAdapter.getTypeBeanList().get(imageAdapter.getTypeBeanList().size()-1).defualt != FeedBackImageBean.TYPE_DEFUALT ){
                    imageAdapter.getTypeBeanList().add(new FeedBackImageBean());
                }
                imageAdapter.notifyDataSetChanged();
            }
        });
        rc_image.setAdapter(imageAdapter);
    }

    private int loadUrl  = 0;
    private void uploadFile(){

        if(listImage == null){
            return;
        }
        if(loadUrl == 0){
            return;
        }
        for (int i = 0; i < listImage.size(); i++) {
            FeedBackImageBean feedBackImageBean = listImage.get(i);
            if(feedBackImageBean.defualt == FeedBackImageBean.TYPE_IMAGE_LOCAL){
                File file = new File(feedBackImageBean.imageUrl);
                EasyHttp.post(this)
                        .api(new UpdateImageApi()
                                .setImage(file))
                        .request(new HttpCallback<HttpData<UpdateImageApi.Bean>>(this) {

                            @Override
                            public void onSucceed(HttpData<UpdateImageApi.Bean> data) {
                                if(data.getData() != null){
                                    UpdateImageApi.Bean model = data.getData();
                                    if(StringUtils.isNotEmpty(model.httpHost) && StringUtils.isNotEmpty(model.imageUri)){
                                        String  imageUri = model.httpHost + model.imageUri;
                                        feedBackImageBean.imageUrlHttp = imageUri;
                                        feedBackImageBean.defualt =  FeedBackImageBean.TYPE_IMAGE_HTTP;
                                        loadUrl --;
                                        uploadFile();
                                    }
                                }
                            }

                            @Override
                            public void onFail(Exception e) {
                                super.onFail(e);
                            }
                        });
                break;
            }
        }
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();

        // TODO 结果
        ReportSubmitDetailsActivity.start(this,"xx",true);
        if (id == R.id.tv_submit) {
            if (TextUtils.isEmpty(feedbackText)) {
                ToastUtils.show("举报详细说明不能为空");
                return;
            }
            if (feedbackText != null && feedbackText.length() > 500) {
                ToastUtils.show("字数限制最多500字");
                return;
            }

            if(selectTypeBean == -1 ){
                ToastUtils.show("请选择举报类型");
                return;
            }

            List<String> sysFeedbackImageList = new ArrayList<>();
            for (FeedBackImageBean item:listImage) {
                if(item.defualt == FeedBackImageBean.TYPE_IMAGE_HTTP){
                    sysFeedbackImageList.add(item.imageUrlHttp);
                }
            }
            feedback(selectTypeBean,feedbackText,sysFeedbackImageList);

        }

    }

    /**
     * 打开图库
     */
    @SuppressLint("NewApi")
    private void setOpenAlbum(int num){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.MULTIPLE)//多张
                .maxSelectNum(num)
//                .isEnableCrop(true)//是否开启裁剪
//                .isSingleDirectReturn(true)//单选后是否直接返回
                .isWeChatStyle(true)//是否微信风格
                .isCamera(true)//是否显示拍照按钮
                .isGif(false)//是否显示gif
//                .cutOutQuality(50)//裁剪输出质量
                .withAspectRatio(1, 1)//裁剪比例
                .circleDimmedLayer(false)// 是否开启圆形裁剪
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(true)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                //.setCropDimmedColor(getColor(R.color.color_33cfcfcf))//设置圆形裁剪背景色值
                .setCircleDimmedBorderColor(getColor(R.color.main))//设置圆形裁剪边框色值
                .rotateEnabled(true)//裁剪是否可旋转图片
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .isDragFrame(true)//是否可拖动裁剪框(固定)
                .minimumCompressSize(200)// 小于多少kb的图片不压缩
                .isReturnEmpty(true)//未选择数据时按确定是否可以退出
                .isAndroidQTransform(true)//Android Q版本下是否需要拷贝文件至应用沙盒内
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++) {
                                String cutPath = result.get(i).getRealPath();
                                FeedBackImageBean feedBackImageBean = new FeedBackImageBean();
                                feedBackImageBean.imageUrl = cutPath;
                                feedBackImageBean.defualt = FeedBackImageBean.TYPE_IMAGE_LOCAL;
                                listImage.add(0,feedBackImageBean);
                                loadUrl ++;
                            }

                            if(listImage.size() == 10){
                                listImage.remove(9);
                            }
                            //上传图片
                            uploadFile();
                            imageAdapter.notifyDataSetChanged();

                        }
                    }
                    @Override
                    public void onCancel() {

                    }
                });
    }

}