package com.huaxixingfu.sqj.ui.activity.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.diskkiller.base.BaseActivity;
import com.diskkiller.base.BaseDialog;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.SettingBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.PersonDataBean;
import com.huaxixingfu.sqj.bean.PersonEnumDataBean;
import com.huaxixingfu.sqj.bean.VCode;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.dialog.PickerDateDialog;
import com.huaxixingfu.sqj.dialog.PickerHightClosedDialog;
import com.huaxixingfu.sqj.dialog.PickerHightDialog;
import com.huaxixingfu.sqj.http.api.EditBirApi;
import com.huaxixingfu.sqj.http.api.EditHeadApi;
import com.huaxixingfu.sqj.http.api.EditIntroApi;
import com.huaxixingfu.sqj.http.api.EditNationApi;
import com.huaxixingfu.sqj.http.api.EditNikeNameApi;
import com.huaxixingfu.sqj.http.api.EditOccApi;
import com.huaxixingfu.sqj.http.api.EditPolApi;
import com.huaxixingfu.sqj.http.api.EditSexApi;
import com.huaxixingfu.sqj.http.api.GetNationApi;
import com.huaxixingfu.sqj.http.api.GetPolApi;
import com.huaxixingfu.sqj.http.api.PersonalDataApi;
import com.huaxixingfu.sqj.http.api.PersonalSexApi;
import com.huaxixingfu.sqj.http.api.UpdateImageApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.dialog.AgreementDialog;
import com.huaxixingfu.sqj.ui.dialog.CardNotifocationDialog;
import com.huaxixingfu.sqj.utils.GlideEngine;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.MyTime;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PersonalDataActivity extends AppActivity {


    private static final String TAG = PersonalDataActivity.class.getSimpleName();

    private static final int REQUEST_CertificationActivity_1 = 1;
    private static final int REQUEST_ResidentActivity_2 = 2;

    private ImageView icIcon;
    private SettingBar sbPersonalName,sbPersonalSex,sbPersonalBirthday,
            sbPersonalMinzu,sbPersonalMianmao,sbPersonalRealName,
            sbPersonalAdress,sbPersonalPhone,sb_personal_work;

    private TextView sbPersonalNikeName;
    private TextView tv_title;
    private ImageView bar_back;
    private PersonDataBean personDataBean;


    @SuppressLint("NewApi")
    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_personal_data;
    }

    public void initView() {


        setOnClickListener(R.id.ll_setting_header,R.id.sb_personal_name,R.id.sb_personal_sex,
        R.id.sb_personal_birthday,R.id.sb_personal_mianmao,R.id.sb_personal_minzu,R.id.sb_personal_real_name,
        R.id.sb_personal_adress,R.id.sb_personal_work,R.id.sb_personal_nike_name);

        icIcon = findViewById(R.id.ic_icon);
        sbPersonalName = findViewById(R.id.sb_personal_name);
        sbPersonalSex = findViewById(R.id.sb_personal_sex);
        sbPersonalBirthday = findViewById(R.id.sb_personal_birthday);
        sbPersonalMinzu = findViewById(R.id.sb_personal_minzu);
        sbPersonalNikeName = findViewById(R.id.sb_personal_nike_name);
        sbPersonalMianmao = findViewById(R.id.sb_personal_mianmao);
        sbPersonalRealName = findViewById(R.id.sb_personal_real_name);
        sbPersonalAdress = findViewById(R.id.sb_personal_adress);
        sbPersonalPhone = findViewById(R.id.sb_personal_phone);
        sb_personal_work = findViewById(R.id.sb_personal_work);

    }


    public void initData() {
        getPersonDate();
    }

    private void getPersonDate(){
        EasyHttp.post(this)
                .api(new PersonalDataApi())
                .request(new HttpCallback<HttpData<PersonDataBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<PersonDataBean> data) {
                        if(data.getData() != null){
                            personDataBean = data.getData();

                            SPManager.instance(getActivity()).putModel(Constants.USERINFO,personDataBean);

                            if(StringUtils.isNotEmpty(personDataBean.getUserAvatarUrl()))
                                Glide.with(getContext()).load(personDataBean.getUserAvatarUrl()).into(icIcon);
                            if(StringUtils.isNotEmpty(personDataBean.getUserNickName()))
                                sbPersonalName.setRightText(personDataBean.getUserNickName());
                            if(StringUtils.isNotEmpty(personDataBean.getUserSexCodeName()))
                                sbPersonalSex.setRightText(personDataBean.getUserSexCodeName());

                            if(personDataBean.getUserBir() != 0){
                                Calendar c=Calendar.getInstance();
                                int seconds = personDataBean.getUserBir();
                                sbPersonalBirthday.setRightText(MyTime.geTime_yyyyMMddString(String.valueOf(seconds)));
                            }


                            if(StringUtils.isNotEmpty(personDataBean.getUserNationCodeName()))
                                sbPersonalMinzu.setRightText(personDataBean.getUserNationCodeName());

                            if(StringUtils.isNotEmpty(personDataBean.getUserSignName()))
                                sbPersonalNikeName.setText(personDataBean.getUserSignName());

                            if(StringUtils.isNotEmpty(personDataBean.getResidentPoliticsFaceName()))
                                sbPersonalMianmao.setRightText(personDataBean.getResidentPoliticsFaceName());

                            if(StringUtils.isNotEmpty(personDataBean.getResidentCareerInfo()))
                                sb_personal_work.setRightText(personDataBean.getResidentCareerInfo());

                            if(StringUtils.isNotEmpty(personDataBean.getCardStartusName())){
                                if(personDataBean.getCardStartus() == 2)
                                    sbPersonalRealName.setRightTextColor(Color.parseColor("#8CD130"));

                                sbPersonalRealName.setRightText(personDataBean.getCardStartusName());
                            }

                            if(StringUtils.isNotEmpty(personDataBean.getUserResidentCertStatusName())){
                                if(personDataBean.getUserResidentCertStatus() == 2)
                                    sbPersonalAdress.setRightTextColor(Color.parseColor("#8CD130"));
                                sbPersonalAdress.setRightText(personDataBean.getUserResidentCertStatusName());
                            }
                            if(StringUtils.isNotEmpty(personDataBean.getUserPhone()))
                                sbPersonalPhone.setRightText(StringUtils.phoneNumber());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    private void getPersonSex(){
        EasyHttp.post(this)
                .api(new PersonalSexApi())
                .request(new HttpCallback<HttpData<List<PersonEnumDataBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<PersonEnumDataBean>> data) {
                        if(data.getData() != null){
                            List<PersonEnumDataBean> list = data.getData();
                            List<String> sexlist = new ArrayList<>();
                            if(list != null && list.size()>0){
                                for (PersonEnumDataBean personEnumDataBean : list) {
                                    sexlist.add(personEnumDataBean.getDictName());
                                }

                                new PickerHightClosedDialog.Builder(getContext())
                                        // ????????????????????????
                                        .setTitle("??????")
                                        .setData((ArrayList) sexlist)
                                        // ??????????????????
                                        .setConfirm("??????")
                                        // ?????? null ???????????????????????????
                                        .setCancel("??????")
                                        .setGravity(Gravity.BOTTOM)
                                        // ???????????????????????????????????????
                                        //.setAutoDismiss(false)
                                        .setListener(new PickerHightClosedDialog.OnListener() {

                                            @Override
                                            public void onConfirm(String data,int position) {
                                                if(StringUtils.isEmpty(data))
                                                    sbPersonalSex.setRightText(sexlist.get(0));
                                                else
                                                    sbPersonalSex.setRightText(data);

                                                editSex(position+"");
                                            }

                                            @Override
                                            public void onCancel(BaseDialog dialog) {
                                            }
                                        })
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    private void editSex(String userSexCode){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", userSexCode);
        EasyHttp.post(this)
                .api(new EditSexApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                ToastUtils.show("????????????");
                            }else{
                                ToastUtils.show("????????????");
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void editNikeName(String name){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", name);
        EasyHttp.post(this)
                .api(new EditNikeNameApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                getPersonDate();
                                ToastUtils.show("????????????");
                            }else{
                                ToastUtils.show("????????????");
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void editHead(String url){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", url);
        EasyHttp.post(this)
                .api(new EditHeadApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                ToastUtils.show("????????????");
                            }else{
                                ToastUtils.show("????????????");
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void editNation(String nation){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", nation);
        EasyHttp.post(this)
                .api(new EditNationApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                ToastUtils.show("????????????");
                            }else{
                                ToastUtils.show("????????????");
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void getNationPolitics(){
        EasyHttp.post(this)
                .api(new GetNationApi())
                .request(new HttpCallback<HttpData<List<PersonEnumDataBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<PersonEnumDataBean>> data) {
                        if(data.getData() != null){
                            List<PersonEnumDataBean> list = data.getData();
                            List<String> datelist = new ArrayList<>();
                            if(list != null && list.size()>0){
                                for (PersonEnumDataBean personEnumDataBean : list) {
                                    datelist.add(personEnumDataBean.getDictName());
                                }

                                new PickerHightClosedDialog.Builder(getContext())
                                        // ????????????????????????
                                        .setTitle("???????????????")
                                        .setData((ArrayList) datelist)
                                        // ??????????????????
                                        .setConfirm("??????")
                                        // ?????? null ???????????????????????????
                                        .setCancel("??????")
                                        .setGravity(Gravity.BOTTOM)
                                        // ???????????????????????????????????????
                                        //.setAutoDismiss(false)
                                        .setListener(new PickerHightClosedDialog.OnListener() {

                                            @Override
                                            public void onConfirm(String data,int position) {
                                                if(StringUtils.isEmpty(data))
                                                    data = datelist.get(0);

                                                sbPersonalMinzu.setRightText(data);
                                                editNation(list.get(position).getDictCode());
                                            }

                                            @Override
                                            public void onCancel(BaseDialog dialog) {
                                            }
                                        })
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void editintro(String intro){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", intro);
        EasyHttp.post(this)
                .api(new EditIntroApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                getPersonDate();
                                ToastUtils.show("????????????");
                            }else{
                                ToastUtils.show("????????????");
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void editPolitics(String politics){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", politics);
        EasyHttp.post(this)
                .api(new EditPolApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                ToastUtils.show("????????????");
                            }else{
                                ToastUtils.show("????????????");
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void getPersonPolitics(){
        EasyHttp.post(this)
                .api(new GetPolApi())
                .request(new HttpCallback<HttpData<List<PersonEnumDataBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<PersonEnumDataBean>> data) {
                        if(data.getData() != null){
                            List<PersonEnumDataBean> list = data.getData();
                            List<String> datelist = new ArrayList<>();
                            if(list != null && list.size()>0){
                                for (PersonEnumDataBean personEnumDataBean : list) {
                                    datelist.add(personEnumDataBean.getDictName());
                                }

                                new PickerHightClosedDialog.Builder(getContext())
                                        // ????????????????????????
                                        .setTitle("?????????????????????")
                                        .setData((ArrayList) datelist)
                                        // ??????????????????
                                        .setConfirm("??????")
                                        // ?????? null ???????????????????????????
                                        .setCancel("??????")
                                        .setGravity(Gravity.BOTTOM)
                                        // ???????????????????????????????????????
                                        //.setAutoDismiss(false)
                                        .setListener(new PickerHightClosedDialog.OnListener() {

                                            @Override
                                            public void onConfirm(String data,int position) {
                                                if(StringUtils.isEmpty(data))
                                                    data = datelist.get(0);
                                                sbPersonalMianmao.setRightText(data);
                                                editPolitics(list.get(position).getDictCode());

                                            }

                                            @Override
                                            public void onCancel(BaseDialog dialog) {
                                            }
                                        })
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void editOccupation(String occupation){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", occupation);
        EasyHttp.post(this)
                .api(new EditOccApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                getPersonDate();
                                ToastUtils.show("????????????");
                            }else{
                                ToastUtils.show("????????????");
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void editBir(long bir){
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", bir);
        EasyHttp.post(this)
                .api(new EditBirApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                getPersonDate();
                                ToastUtils.show("????????????");
                            }else{
                                ToastUtils.show("????????????");
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
                                editHead(model.imageUri);
                                Glide.with(getContext()).load(model.httpHost+model.imageUri).into(icIcon);
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
        switch (id) {
            case R.id.ll_setting_header:
                picSelect();
                break;
            case R.id.sb_personal_name:
                String name = StringUtils.isEmpty(personDataBean.getUserNickName()) ? "":personDataBean.getUserNickName();
                InputTextActivity.start(PersonalDataActivity.this, "????????????", name,10,new InputTextActivity.OnFinishResultListener() {
                    @Override
                    public void onSucceed(String data) {
                        //sbPersonalName.setRightText(data);
                        editNikeName(data);
                    }
                });
                break;
            case R.id.sb_personal_birthday:
                new PickerDateDialog.Builder(getContext())
                        // ????????????????????????
                        .setTitle("???????????????")
                        // ??????????????????
                        .setConfirm("??????")
                        // ?????? null ???????????????????????????
                        .setCancel("??????")
                        .setGravity(Gravity.BOTTOM)
                        // ???????????????????????????????????????
                        //.setAutoDismiss(false)
                        .setListener(new PickerDateDialog.OnListener() {

                            @Override
                            public void onConfirm(String date) {
                                try {
                                    if(date == null)
                                        date = MyTime.getTime_3(System.currentTimeMillis());
                                    Date curTime = MyTime.stringToDate(MyTime.getTime_yyyy_MM_dd(System.currentTimeMillis()),"yyy-MM-dd");
                                    Date selTime = MyTime.stringToDate(date,"yyy-MM-dd");
                                    if(MyTime.afterCalendar(selTime,curTime)){
                                        ToastUtils.show("??????????????????????????????");
                                        return;
                                    }
                                    sbPersonalBirthday.setRightText(date);
//                                    Integer time = Integer.valueOf(String.valueOf(selTime.getTime()).substring(0,10));
//                                    editBir(time.intValue());
                                    editBir(Integer.valueOf(MyTime.geTime_yyyyMMdd(selTime)));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                            }
                        })
                        .show();
                break;

            case R.id.sb_personal_mianmao:
                getPersonPolitics();
                break;
            case R.id.sb_personal_minzu:
                getNationPolitics();
                break;
            case R.id.sb_personal_sex:
                getPersonSex();
                break;
            case R.id.sb_personal_nike_name:
                String sigeName = StringUtils.isEmpty(personDataBean.getUserSignName()) ? "":personDataBean.getUserSignName();
                InputTextActivity.start(PersonalDataActivity.this, "????????????", sigeName,50,new InputTextActivity.OnFinishResultListener() {
                    @Override
                    public void onSucceed(String data) {
                        editintro(data);
                    }
                });
                break;
            case R.id.sb_personal_work:
                String work = StringUtils.isEmpty(personDataBean.getResidentCareerInfo()) ? "":personDataBean.getResidentCareerInfo();
                InputTextActivity.start(PersonalDataActivity.this, "????????????", work,10,new InputTextActivity.OnFinishResultListener() {
                    @Override
                    public void onSucceed(String data) {
                        editOccupation(data);
                    }
                });
                break;
            case R.id.sb_personal_real_name:
                int isReal = personDataBean.getCardStartus() == 2 ? 1 : 0;
                CertificationActivity.start(PersonalDataActivity.this,"????????????",isReal,new CertificationActivity.OnFinishResultListener() {
                    @Override
                    public void onSucceed(String data) {
                        getPersonDate();
                    }
                });
                break;
            case R.id.sb_personal_adress:

                personAdressClick();
                break;

        }
    }

    /**
     *  ????????????
     */
    private void personAdressClick(){

        if("?????????".equals(personDataBean.getCardStartusName())){
            //????????????
            ((BaseActivity)this).startActivityForResult(new Intent(getActivity(), ResidentActivity.class), (resultCode, data) -> {
                getPersonDate();
            });
        }else{

            new CardNotifocationDialog.Builder(this)

                    .setTitle("?????????????????????????????????")
                // ??????????????????
                .setConfirm(getString(R.string.common_agress))
                // ?????? null ???????????????????????????
                .setCancel(getString(R.string.common_refuse))
                // ???????????????????????????????????????
//                    .setAutoDismiss(false)
                .setCancelable(false)
                //.setAutoDismiss(false)
                .setCanceledOnTouchOutside(false)
                .setListener(new CardNotifocationDialog.OnListener() {

                    @Override
                    public void onConfirm() {
                        startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
//                        finish();
                    }
                })
                .show();
        }
    }
    /**
     * ????????????
     */
    @SuppressLint("NewApi")
    private void picSelect() {

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//????????????
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)//??????
                .isEnableCrop(true)//??????????????????
//                .isSingleDirectReturn(true)//???????????????????????????
                .isWeChatStyle(true)//??????????????????
                .isCamera(true)//????????????????????????
                .isGif(false)//????????????gif
//                .cutOutQuality(50)//??????????????????
                .withAspectRatio(1, 1)//????????????
                .circleDimmedLayer(false)// ????????????????????????
                .showCropFrame(true)// ?????????????????????????????? ???????????????????????????false
                .showCropGrid(true)//?????????????????????????????? ???????????????????????????false
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
                        // ????????????
                        LogUtil.e("aaaaaaaa_selectphoto", result.get(0).getCutPath());
                        uploadFile(new File(result.get(0).getCutPath()));
                    }

                    @Override
                    public void onCancel() {
                        // ??????
                    }
                });
    }
}
