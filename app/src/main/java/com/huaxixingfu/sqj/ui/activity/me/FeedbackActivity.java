package com.huaxixingfu.sqj.ui.activity.me;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;

import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;

import com.huaxixingfu.sqj.bean.FeedBackImageBean;
import com.huaxixingfu.sqj.bean.TypeBean;
import com.huaxixingfu.sqj.bean.VSucess;
import com.huaxixingfu.sqj.http.api.FeedBackApi;
import com.huaxixingfu.sqj.http.api.GetFeedBackTypeApi;
import com.huaxixingfu.sqj.http.api.UpdateImageApi;
import com.huaxixingfu.sqj.http.model.HttpData;

import com.huaxixingfu.sqj.ui.adapter.FeedBackAdapter;
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

public class FeedbackActivity extends AppActivity{

    private RecyclerView rvFeedType,rc_image;
    private EditText etDescribe;
    private TextView submit;
    private String feedbackText;
    private FeedBackAdapter adapter;
    private FeedBackImageAdapter imageAdapter;

    private TypeBean selectTypeBean;

    private List<FeedBackImageBean> listImage = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_feedback;
    }

    @Override
    protected void initView() {
        initEvent();
    }

    public void initData() {
        getFeedbackType();
    }

    private void getFeedbackType(){
        EasyHttp.post(this)
                .api(new GetFeedBackTypeApi())
                .request(new HttpCallback<HttpData<List<TypeBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<TypeBean>> data) {
                        if(data.getData() != null){
                            adapter = new FeedBackAdapter(FeedbackActivity.this,
                                    data.getData(), new FeedBackAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClickListener(View view, int position, TypeBean typeBean) {
                                    //??????????????????
                                    if(selectTypeBean != null){
                                        selectTypeBean.setSelected(false);
                                    }

                                    if(selectTypeBean == typeBean){
                                        return;
                                    }
                                    typeBean.setSelected(true);
                                    selectTypeBean = typeBean;

                                    adapter.notifyDataSetChanged();
                                }

                            });
                            rvFeedType.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
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
                                    ToastUtils.show("??????????????????,?????????????????????");
                                    new Handler().postDelayed(()->{
                                        FeedbackActivity.this.finish();
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
        rvFeedType = findViewById(R.id.rvFeedbackType);
        etDescribe = findViewById(R.id.et_describe);
        submit = findViewById(R.id.tv_submit);
        TextView tvHint = findViewById(R.id.tvHint);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        rvFeedType.setLayoutManager(layoutManager);

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
        imageAdapter = new FeedBackImageAdapter(FeedbackActivity.this,
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

                                        feedBackImageBean.httpHost = model.httpHost ;
                                        feedBackImageBean.imageUrlHttp = model.imageUri;
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
        if (id == R.id.tv_submit) {
            if (TextUtils.isEmpty(feedbackText)) {
                ToastUtils.show("????????????????????????");
                return;
            }
            if (feedbackText != null && feedbackText.length() > 500) {
                ToastUtils.show("??????????????????500???");
                return;
            }

            if(null== selectTypeBean){  
                ToastUtils.show("???????????????????????????");
                return;
            }

            List<String> sysFeedbackImageList = new ArrayList<>();
            for (FeedBackImageBean item:listImage) {
                if(item.defualt == FeedBackImageBean.TYPE_IMAGE_HTTP){
                    sysFeedbackImageList.add(item.imageUrlHttp);
                }
            }
            feedback(selectTypeBean.code,feedbackText,sysFeedbackImageList);
        }

    }

    /**
     * ????????????
     */
    @SuppressLint("NewApi")
    private void setOpenAlbum(int num){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.MULTIPLE)//??????
                .maxSelectNum(num)
//                .isEnableCrop(true)//??????????????????
//                .isSingleDirectReturn(true)//???????????????????????????
                .isWeChatStyle(true)//??????????????????
                .isCamera(true)//????????????????????????
                .isGif(false)//????????????gif
//                .cutOutQuality(50)//??????????????????
                .withAspectRatio(1, 1)//????????????
                .circleDimmedLayer(false)// ????????????????????????
//                .showCropFrame(true)// ?????????????????????????????? ???????????????????????????false
//                .showCropGrid(true)//?????????????????????????????? ???????????????????????????false
                //.setCropDimmedColor(getColor(R.color.color_33cfcfcf))//??????????????????????????????
                .setCircleDimmedBorderColor(getColor(R.color.main))//??????????????????????????????
                .rotateEnabled(true)//???????????????????????????
                .scaleEnabled(true)//?????????????????????????????????
                .isDragFrame(true)//????????????????????????(??????)
                .minimumCompressSize(200)// ????????????kb??????????????????
                .isReturnEmpty(true)//?????????????????????????????????????????????
                .isAndroidQTransform(true)//Android Q???????????????????????????????????????????????????
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
                            //????????????
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