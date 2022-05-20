package com.huaxixingfu.sqj.ui.activity.msg;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diskkiller.base.BaseAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.ZoneResidentBean;
import com.huaxixingfu.sqj.http.api.ResidentZoneListApi;
import com.huaxixingfu.sqj.http.api.ZoneResidentListApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.ResidentZoneListAdapter;
import com.huaxixingfu.sqj.ui.adapter.ZoneResidentListAdapter;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *
 */
public class ZoneResidentListActivity extends AppActivity implements View.OnClickListener {

    private RecyclerView rv_zone,rv_zone_list;
    private ZoneResidentListAdapter zoneResidentListAdapter;
    private ResidentZoneListAdapter residentZoneListAdapter;
    private List<List<ZoneResidentBean>> tempData = new ArrayList<>();
    private List<List<ZoneResidentBean>> completeData = new ArrayList<>();
    private List<TextView> topViews = new ArrayList<>();
    private int curPage;

    private TextView tv_community,tv_building,tv_unit,tv_floor;
    private boolean isResidentZoneList;
    private long userPrecinctSocpeId;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_zone_resident_list;
    }

    @Override
    protected void initView() {
        tv_community = findViewById(R.id.tv_community);
        tv_building = findViewById(R.id.tv_building);
        tv_unit = findViewById(R.id.tv_unit);
        tv_floor = findViewById(R.id.tv_floor);
        topViews.add(tv_community);
        topViews.add(tv_building);
        topViews.add(tv_unit);
        topViews.add(tv_floor);
        initRV();
    }

    @Override
    public void onLeftClick(View view) {
        if(isResidentZoneList){
            isResidentZoneList = false;
            rv_zone.setVisibility(View.VISIBLE);
            rv_zone_list.setVisibility(View.GONE);
            return;
        }
        if(topViews.get(curPage) != null)
            topViews.get(curPage).setVisibility(View.INVISIBLE);
        curPage--;
        //toast("curPage  "+curPage);
        if(curPage >= 0){
            if(curPage <= tempData.size()-1)
                zoneResidentListAdapter.setData(tempData.get(curPage));
        }else{
            finish();
        }
    }

    private void initRV() {
        rv_zone = findViewById(R.id.rv_zone);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv_zone.setLayoutManager(llm);
        zoneResidentListAdapter = new ZoneResidentListAdapter(getContext());

        zoneResidentListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if(zoneResidentListAdapter.getData().get(position).children != null && zoneResidentListAdapter.getData().get(position).children.size()>0){

                    if(topViews.get(curPage) != null){
                        topViews.get(curPage).setVisibility(View.VISIBLE);
                        topViews.get(curPage).setText(""+zoneResidentListAdapter.getData().get(position).name+" > ");
                        topViews.get(curPage).setTextColor(Color.parseColor("#FF6C2A"));
                    }

                    zoneResidentListAdapter.setData(zoneResidentListAdapter.getData().get(position).children);

                    if(completeData.size()<=0){
                        tempData.add(zoneResidentListAdapter.getData());
                    }
                    curPage++;

                    /*if(topViews.get(curPage) != null){
                        topViews.get(curPage).setVisibility(View.VISIBLE);
                        topViews.get(curPage).setText(zoneResidentListAdapter.getData().get(position).name);
                        topViews.get(curPage).setTextColor(Color.parseColor("#666666"));
                    }
                    */
                }
                else{
                    completeData = tempData;
                    topViews.get(curPage).setVisibility(View.VISIBLE);
                    topViews.get(curPage).setText(""+zoneResidentListAdapter.getData().get(position).name+" > ");
                    topViews.get(curPage).setTextColor(Color.parseColor("#FF6C2A"));
                    userPrecinctSocpeId = zoneResidentListAdapter.getData().get(position).userPrecinctSocpeId;
                    getZoneResidentList();
                }
            }
        });
        rv_zone.setAdapter(zoneResidentListAdapter);

        //=================================================================================

        rv_zone_list = findViewById(R.id.rv_zone_list);
        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        rv_zone_list.setLayoutManager(llm1);
        residentZoneListAdapter = new ResidentZoneListAdapter(getContext());

        residentZoneListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                toast(residentZoneListAdapter.getData().get(position).residentName);
                if(residentZoneListAdapter.getData().get(position).isFriend){
                    TempMessageActivity.show(getContext(),residentZoneListAdapter.getData().get(position).userId,
                            residentZoneListAdapter.getData().get(position).userId+"",
                            residentZoneListAdapter.getData().get(position).nickName,false);
                }else{
                    AddFriendApplyActivity.start(ZoneResidentListActivity.this,residentZoneListAdapter.getData().get(position).userId
                            ,residentZoneListAdapter.getData().get(position).nickName,
                            residentZoneListAdapter.getData().get(position).avatar,null);

                }
            }
        });
        rv_zone_list.setAdapter(residentZoneListAdapter);




        /*zoneResidentListAdapter.setOnChildClickListener(R.id.tv_see,new BaseAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(RecyclerView recyclerView, View childView, int position) {
                if(childView.getId() == R.id.tv_see){
                    ZoneResidentBean zoneResidentBean = zoneResidentListAdapter.getItem(position);
                }
            }
        });*/

    }

    public void initData() {
        getZoneList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(userPrecinctSocpeId != 0){
            getZoneResidentList();
        }
    }

    private void getZoneList(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "");
        EasyHttp.post(this)
                .api(new ZoneResidentListApi())
                .json(map)
                .request(new HttpCallback<HttpData<List<ZoneResidentBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<ZoneResidentBean>> data) {
                        if(data.getData() != null){
                            zoneResidentListAdapter.setData(data.getData());
                            tempData.add(data.getData());

                            topViews.get(curPage).setVisibility(View.VISIBLE);
                            topViews.get(curPage).setText("请选择");
                            topViews.get(curPage).setTextColor(Color.parseColor("#666666"));
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void getZoneResidentList(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("zoneUnitId", userPrecinctSocpeId);
        EasyHttp.post(this)
                .api(new ResidentZoneListApi())
                .json(map)
                .request(new HttpCallback<HttpData<List<ZoneResidentBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<ZoneResidentBean>> data) {
                        if(data.getData() != null){
                            isResidentZoneList = true;
                            rv_zone.setVisibility(View.GONE);
                            rv_zone_list.setVisibility(View.VISIBLE);
                            residentZoneListAdapter.setData(data.getData());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}
