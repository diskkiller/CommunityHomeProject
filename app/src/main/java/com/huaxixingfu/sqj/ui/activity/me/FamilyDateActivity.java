package com.huaxixingfu.sqj.ui.activity.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.FamilyDataApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.FamilyAdapter;

import java.util.List;

public class FamilyDateActivity extends AppActivity{

    private TextView tvAddress;
    private RecyclerView recycler;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_family_date;
    }

    public void initView() {
        tvAddress = findViewById(R.id.address);
        recycler = findViewById(R.id.recycler);
    }


    public void initData() {
        getFamilyDate();
    }

    private void getFamilyDate(){
        EasyHttp.post(this)
                .api(new FamilyDataApi())
                .request(new HttpCallback<HttpData<FamilyDataApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<FamilyDataApi.Bean> data) {
                        if(data.getData() != null){
                            String address = data.getData().address;
                            if((null != address) && (address.length() >0)){
                                tvAddress.setText(address);
                            }
                            List<FamilyDataApi.Bean.VFamily> list = data.getData().list;

                            if((null != list) && (list.size()>0)){
                                FamilyAdapter adapter = new FamilyAdapter(R.layout.sqj_item_family);
                                adapter.setList(list);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                                        LinearLayoutManager.VERTICAL,
                                        false);
                                recycler.setLayoutManager(linearLayoutManager);
                                recycler.setAdapter(adapter);
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
    public void onClick(View v) {
    }
}
