package com.huaxixingfu.sqj.ui.activity.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

import com.diskkiller.base.BaseDialog;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.ResidentConfig;
import com.huaxixingfu.sqj.bean.VCode;
import com.huaxixingfu.sqj.bean.VCommunity;
import com.huaxixingfu.sqj.bean.VResident;
import com.huaxixingfu.sqj.bean.VSucess;
import com.huaxixingfu.sqj.dialog.PickerHightDialog;
import com.huaxixingfu.sqj.http.api.GetCommunityDataApi;
import com.huaxixingfu.sqj.http.api.GetResidentInitApi;
import com.huaxixingfu.sqj.http.api.SubmitCommunityDataApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：lblbh on 2022/5/1 10:58
 * 邮箱：lanbuhan@163.com
 * 功能：居民认证
 */

public class ResidentActivity extends AppActivity {

    VCommunity communityVCommunity = null;
    VCommunity quartersVCommunity = null;
    VCommunity numberVCommunity = null;
    VCommunity unitVCommunity = null;
    VCommunity floorVCommunity = null;
    VCommunity roomNumVCommunity = null;


    List<VCommunity> communitys = null;
    List<VCommunity> quarters = null;
    List<VCommunity> numbers = null;
    List<VCommunity> units = null;
    List<VCommunity> floors = null;
    List<VCommunity> rooms = null;

    int residentStatus = -1;

    public static final String RE_SUMBIT_KEY = "RE_SUMBIT_KEY";
    public static final int RESIDENT_STATUS_REFUCE = 3;

    private boolean reSumbit = false;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_resident;
    }

    @Override
    protected void initView() {
        initListener();
        reSumbit = getIntent().getBooleanExtra(RE_SUMBIT_KEY, false);
    }


    private TextView communityValue,quartersValue,
            numberValue,unitValue,floorValue,
            roomNumValue,submit,roomNameValue,roomCodeValue,
            roomCheckStateValue,reasonsValue,reasons_key;
    private View empty_divide;
    private Group residentGruop;
    private void initListener() {
        communityValue = findViewById(R.id.community_value);
        quartersValue = findViewById(R.id.quarters_value);
        numberValue = findViewById(R.id.number_value);
        unitValue = findViewById(R.id.unit_value);
        floorValue = findViewById(R.id.floor_value);
        roomNumValue = findViewById(R.id.room_num_value);
        submit = findViewById(R.id.submit);
        residentGruop = findViewById(R.id.resident_gruop);
        roomNameValue = findViewById(R.id.room_name_value);
        roomCodeValue = findViewById(R.id.room_code_value);
        roomCheckStateValue = findViewById(R.id.room_check_state_value);
        reasons_key = findViewById(R.id.reasons_key);
        reasonsValue = findViewById(R.id.reasons_value);
        empty_divide = findViewById(R.id.empty_divide);
        setOnClickListener(R.id.community_value,
                R.id.quarters_value,R.id.number_value,
                R.id.unit_value,R.id.floor_value,R.id.room_num_value,R.id.submit);
    }

    public void initData(){

        getResidentInit();
        //获取社区
        getResidentCommunity(
                ResidentConfig.level_community,
                ResidentConfig.level_normal);
    }

    private void getResidentInit(){
        EasyHttp.post(this)
                .api(new GetResidentInitApi())
                .request(new HttpCallback<HttpData<VResident>>(this) {

                    @Override
                    public void onSucceed(HttpData<VResident> mdata) {
                        if(mdata.getData() != null){
                            VResident data = mdata.getData();
                            if(null != data){
                                //0未认证  /1待认证/2认证通过/3认证驳回
                                residentStatus = data.residentStatus;
                                if(reSumbit){
                                    //0未认证
                                    residentStatus = 0;
                                }

                                if(residentStatus == 0){
                                    //0未认证
                                    residentGruop.setVisibility(View.GONE);

                                }else if(residentStatus == 2 || residentStatus == 1){
                                    //2认证通过 /1待认证 暂不处理
                                    roomNameValue.setFocusable(false);
                                    roomNameValue.setFocusableInTouchMode(false);
                                    roomCodeValue.setFocusable(false);
                                    roomCodeValue.setFocusableInTouchMode(false);

                                    setUserBaseInfo(data);
                                    if(residentStatus == 2){
                                        roomCheckStateValue.setTextColor(Color.parseColor("#8CD130"));
                                        roomCheckStateValue.setText("已通过");
                                    }else{
                                        roomCheckStateValue.setTextColor(getResources().getColor(R.color.main));
                                        roomCheckStateValue.setText("待认证");
                                    }
                                    submit.setFocusable(false);
                                    submit.setClickable(false);
                                    submit.setVisibility(View.GONE);
                                    reasons_key.setVisibility(View.GONE);
                                    reasonsValue.setVisibility(View.GONE);
                                    empty_divide.setVisibility(View.GONE);
                                }else if (residentStatus == 3){
                                    //3认证驳回
                                    roomNameValue.setFocusable(true);
                                    roomNameValue.setFocusableInTouchMode(true);
                                    roomCodeValue.setFocusable(true);
                                    roomCodeValue.setFocusableInTouchMode(true);

                                    setUserBaseInfo(data);
                                    reasons_key.setVisibility(View.VISIBLE);
                                    reasonsValue.setText(data.residentExamineContent);
                                    roomCheckStateValue.setText("已驳回");
                                    roomCheckStateValue.setTextColor(getResources().getColor(R.color.color_ffec3937));
                                    residentGruop.setVisibility(View.VISIBLE);
                                    submit.setText("重新提交");

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

    private void getResidentCommunity(int level,long addressCode){
        HashMap<String, Object> map = new HashMap<>();
        map.put("level",level);
        map.put("addressCode",addressCode);
        EasyHttp.post(this)
                .api(new GetCommunityDataApi())
                .json(map)
                .request(new HttpCallback<HttpData<List<VCommunity>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<VCommunity>> data) {
                        if(data.getData() != null){
                            if(level == ResidentConfig.level_community){
                                //社区
                                communitys = data.getData();
                            } else if(level == ResidentConfig.level_quarters){
                                //小区
                                quarters = data.getData();
                            } else if(level == ResidentConfig.level_number){
                                //楼号
                                numbers = data.getData();
                            } else if(level == ResidentConfig.level_unit){
                                //单元
                                units = data.getData();
                            } else if(level == ResidentConfig.level_floor){
                                //楼层
                                floors = data.getData();
                            } else if(level == ResidentConfig.level_room){
                                //房屋门牌号
                                rooms = data.getData();
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void submitResidentCommunity(long zoneId,long zoneRegionId,
                                         long zoneFloorId,long zoneUnitId,
                                         long zoneLayerId,long zoneRoomId,
                                         String userName,String cardNum){
        HashMap<String, Object> map = new HashMap<>();
        map.put("zoneId",zoneId);
        map.put("zoneRegionId",zoneRegionId);
        map.put("zoneFloorId",zoneFloorId);
        map.put("zoneUnitId",zoneUnitId);
        map.put("zoneLayerId",zoneLayerId);
        map.put("zoneRoomId",zoneRoomId);
        map.put("userName",userName);
        map.put("cardNum",cardNum);
        EasyHttp.post(this)
                .api(new SubmitCommunityDataApi())
                .json(map)
                .request(new HttpCallback<HttpData<VSucess>>(this) {

                    @Override
                    public void onSucceed(HttpData<VSucess> mdata) {
                        if(mdata.getData() != null){
                            VSucess data = mdata.getData();
                            if(null != data){
                                boolean isstatus = data.status;

                                if(isstatus){
                                    ToastUtils.show("居民认证上报成功,认证需等待");
                                    ResidentActivity.this.finish();
                                }else{
                                    ToastUtils.show(data.message);
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



    /**
     * 设置用户基本信息
     * @param data
     */
    private void setUserBaseInfo(VResident data ){
        communityValue.setText(data.zoneName);
        quartersValue.setText(data.zoneRegionName);
        numberValue.setText(data.zoneFloorName);
        unitValue.setText(data.zoneUnitName);
        floorValue.setText(data.zoneLayerName);
        roomNumValue.setText(data.zoneRoomName);

        roomNameValue.setText(data.userName);
        roomCodeValue.setText(data.cardNum);
    }

    @SingleClick
    @Override
    public void onClick(View view) {

        if(residentStatus == -1 || residentStatus == 2){
            return;
        }
        switch (view.getId()){
            case R.id.community_value:
                //请选择社区
                if(isDataToast(communitys)){
                    return;
                }
                showSelect(communitys,ResidentConfig.level_community);
                break;
            case R.id.quarters_value:
                //请选择小区
                if(isDataToast(quarters)){
                    return;
                }
                showSelect(quarters,ResidentConfig.level_quarters);
                break;
            case R.id.number_value:
                //请选择楼号
                if(isDataToast(numbers)){
                    return;
                }
                showSelect(numbers,ResidentConfig.level_number);
                break;
            case R.id.unit_value:
                //请选择单元
                if(isDataToast(units)){
                    return;
                }
                showSelect(units,ResidentConfig.level_unit);
                break;
            case R.id.floor_value:
                //请选择楼层
                if(isDataToast(floors)){
                    return;
                }
                showSelect(floors,ResidentConfig.level_floor);
                break;
            case R.id.room_num_value:
                //请选择房屋门牌号
                if(isDataToast(rooms)){
                    return;
                }
                showSelect(rooms,ResidentConfig.level_room);
                break;
            case R.id.submit:
                if(residentStatus == RESIDENT_STATUS_REFUCE){
                    Intent intent = new Intent(getActivity(), ResidentActivity.class);
                    intent.putExtra(RE_SUMBIT_KEY, true);
                    startActivity(intent);
                }else{
                    //提交
                    sumbitInfo();
                }
                break;

        }
    }

    /**
     * 选择数据
     * @param models
     * @param level
     */
    private void showSelect( List<VCommunity> models,int level){

        if((null == models) && (models.size()==0))return;

        ArrayList<String> temps = new ArrayList<String>();
        for (int i=0,j=models.size();i<j;i++){
            temps.add(models.get(i).addressName);
        }

        new PickerHightDialog.Builder(getContext())
                // 标题可以不用填写
                .setTitle("请选择")
                //内容
                .setData(temps)
                // 确定按钮文本
                .setConfirm("确定")
                // 设置 null 表示不显示取消按钮
                .setCancel("取消")
                .setGravity(Gravity.BOTTOM)
                // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setListener(new PickerHightDialog.OnListener() {
                    @Override
                    public void onConfirm(String data,int position) {
                        VCommunity temp = models.get(position);
                        setData(temp,level);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                    }
                })
                .show();
    }

    /**
     * 设置数据
     * @param temp
     * @param level
     */
    private void setData(VCommunity temp,int level){
        if(null == temp)
            return;

        switch (level){
            case ResidentConfig.level_community :
                //社区
                communityVCommunity = temp;
                communityValue.setText(temp.addressName);

                quartersValue.setText("请选择小区");
                numberValue.setText("请选择楼号");
                unitValue.setText("请选择单元");
                floorValue.setText("请选择楼层");
                roomNumValue.setText("请选择房屋门牌号");

                quarters = null;
                numbers = null;
                units = null;
                floors = null;
                rooms = null;

                quartersVCommunity = null;
                numberVCommunity = null;
                unitVCommunity = null;
                floorVCommunity = null;
                roomNumVCommunity = null;

                //获取小区
                getResidentCommunity(
                        ResidentConfig.level_quarters,
                        temp.addressCode);
                break;
            case ResidentConfig.level_quarters :
                //小区
                quartersVCommunity = temp;
                quartersValue.setText(temp.addressName);

                numberValue.setText("请选择楼号");
                unitValue.setText("请选择单元");
                floorValue.setText("请选择楼层");
                roomNumValue.setText("请选择房屋门牌号");

                numbers = null;
                units = null;
                floors = null;
                rooms = null;

                numberVCommunity = null;
                unitVCommunity = null;
                floorVCommunity = null;
                roomNumVCommunity = null;

                //获取楼号
                getResidentCommunity(
                        ResidentConfig.level_number,
                        temp.addressCode);
                break;
            case ResidentConfig.level_number :
                //楼号
                numberVCommunity = temp;
                numberValue.setText(temp.addressName);

                unitValue.setText("请选择单元");
                floorValue.setText("请选择楼层");
                roomNumValue.setText("请选择房屋门牌号");

                units = null;
                floors = null;
                rooms = null;

                unitVCommunity = null;
                floorVCommunity = null;
                roomNumVCommunity = null;

                //获取单元
                getResidentCommunity(
                        ResidentConfig.level_unit,
                        temp.addressCode);
                break;
            case ResidentConfig.level_unit :
                //单元
                unitVCommunity = temp;
                unitValue.setText(temp.addressName);

                floorValue.setText("请选择楼层");
                roomNumValue.setText("请选择房屋门牌号");

                floors = null;
                rooms = null;

                floorVCommunity = null;
                roomNumVCommunity = null;

                //获取楼层
                getResidentCommunity(
                        ResidentConfig.level_floor,
                        temp.addressCode);
                break;
            case ResidentConfig.level_floor :
                //楼层
                floorVCommunity = temp;
                floorValue.setText(temp.addressName);

                roomNumValue.setText("请选择房屋门牌号");

                rooms = null;

                roomNumVCommunity = null;

                //获取房屋门牌号
                getResidentCommunity(
                        ResidentConfig.level_room,
                        temp.addressCode);
                break;
            case ResidentConfig.level_room :
                //房屋门牌号
                roomNumVCommunity = temp;
                roomNumValue.setText(temp.addressName);

                break;
            default:
                break;
        }

    }


    /**
     * 用户点击提交
     */
    private void sumbitInfo(){

        String roomNameDesc = roomNameValue.getText().toString();
        if((null == roomNameDesc) || (roomNameDesc.length()==0)){
            ToastUtils.show("请输入产权人姓名");
            return;
        }

        if (roomNameDesc != null && roomNameDesc.length() > 20) {
            ToastUtils.show("产权人姓名不能超过20位");
            return;
        }


        String roomCodeDesc = roomCodeValue.getText().toString();
        if((null == roomCodeDesc) || (roomCodeDesc.length()==0)){
            ToastUtils.show("请输入产权人身份证号");
            return;
        }

        if (roomCodeDesc != null && !StringUtils.isRealIDCard(roomCodeDesc)) {
            ToastUtils.show("身份证号码错误");
            return;
        }

        if(null == communityVCommunity){
            ToastUtils.show("请选择社区");
            return;
        }

        if(null == quartersVCommunity){
            ToastUtils.show("请选择小区");
            return;
        }

        if(null == numberVCommunity){
            ToastUtils.show("请选择楼号");
            return;
        }

        if(null == unitVCommunity){
            ToastUtils.show("请选择单元");
            return;
        }

        if(null == floorVCommunity){
            ToastUtils.show("请选择楼层");
            return;
        }

        if(null == roomNumVCommunity){
            ToastUtils.show("请选择房屋门牌号");
            return;
        }


        submitResidentCommunity(
                communityVCommunity.addressCode,
                quartersVCommunity.addressCode,
                numberVCommunity.addressCode,
                unitVCommunity.addressCode,
                floorVCommunity.addressCode,
                roomNumVCommunity.addressCode,
                roomNameDesc,
                roomCodeDesc
        );
    }

    /**
     * 集合校验-> false-合法 true-非法
     * @param communitys
     * @return
     */
    private boolean isDataToast(List<VCommunity> communitys){
        boolean isCheck = true;
        if((null != communitys) && (communitys.size() >0)){
            isCheck = false;
        }
        if(isCheck){
            ToastUtils.show("请逐级选择对应信息");
        }
        return isCheck;
    }


}
