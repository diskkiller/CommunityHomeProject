package com.huaxixingfu.sqj.ui.activity.msg;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.github.promeg.pinyinhelper.Pinyin;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.CreatGroupAdapter;
import com.huaxixingfu.sqj.ui.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestMyGroupListActivity extends AppActivity {

    private RecyclerView rv_apply,rv_creat,rv_join;
    private CreatGroupAdapter creatGroupAdapter;
    private TestAdapter testAdapter1,testAdapter2,testAdapter3;
    private ArrayList<MailListApi.Bean> contactsListBeans = new ArrayList<>();;
    private ArrayList<String> testData1 = new ArrayList<>();;
    private ArrayList<String> testData2 = new ArrayList<>();;
    private ArrayList<String> testData3 = new ArrayList<>();;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_my_group;
    }

    @Override
    protected void initView() {
        rv_apply = findViewById(R.id.rv_apply);
        rv_creat = findViewById(R.id.rv_creat);
        rv_join = findViewById(R.id.rv_join);
        initRV();
    }

    public void initData() {
        //getMailList();
        analogData();
    }

    /**
     * 模拟数据
     */
    private void analogData() {
        for (int i = 0; i <3; i++) {
            String str = "我是第" + i + "条目";
            testData1.add(str);
        }
        for (int i = 0; i <5; i++) {
            String str = "我是第" + i + "条目";
            testData2.add(str);
        }
        for (int i = 0; i <4; i++) {
            String str = "我是第" + i + "条目";
            testData3.add(str);
        }
    }

    private void getMailList(){
        EasyHttp.post(this)
                .api(new MailListApi())
                .json("")
                .request(new HttpCallback<HttpData<List<MailListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<MailListApi.Bean>> data) {
                        if(data.getData() != null){
                            List<MailListApi.Bean> datas = data.getData();
                            if((null != datas) && (datas.size()>0)){
                                for (int i = 0,j= datas.size(); i < j; i++) {
                                    MailListApi.Bean item =  datas.get(i);
                                    item.first = String.valueOf(Pinyin.toPinyin(item.chatFriendNiceName, "").charAt(0));
                                }

                                contactsListBeans.clear();
                                contactsListBeans.addAll(datas);
                                Collections.sort(contactsListBeans, new Comparator<MailListApi.Bean>() {
                                    @Override
                                    public int compare(MailListApi.Bean o1, MailListApi.Bean o2) {
                                        return o1.getSection().charAt(0) - o2.getSection().charAt(0);
                                    }
                                });
                                /*if (contactsListBeans.size() > 0) {
                                    //增加header，增加对应的占位数据，用以处理ItemDecoration
                                    ArrayList<MailListApi.Bean> contactsListBeans1 = new ArrayList<>();
                                    MailListApi.Bean bean = new MailListApi.Bean();
                                    bean.first = "header";
                                    contactsListBeans1.add(bean);
                                    contactsListBeans1.addAll(contactsListBeans);
                                    rv_maillist.addItemDecoration(new SectionItemDecoration(getContext(), contactsListBeans1));
                                }*/

                                creatGroupAdapter.setList(contactsListBeans);

                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    private void initRV() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        LinearLayoutManager llm3 = new LinearLayoutManager(getContext());
        rv_apply.setLayoutManager(llm);
        rv_creat.setLayoutManager(llm2);
        rv_join.setLayoutManager(llm3);


        testAdapter1 = new TestAdapter(testData1);
        testAdapter1.setLayoutManager(llm);
        testAdapter1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                testData1.remove(position);
                testAdapter1.notifyItemRemoved(position);
                testAdapter1.notifyItemRangeChanged(position, testAdapter1.getItemCount());


            }
        });
        rv_apply.setAdapter(testAdapter1);


        testAdapter2 = new TestAdapter(testData2);
        testAdapter2.setLayoutManager(llm2);
        testAdapter2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

                testData2.clear();
                for (int i = 0; i <6; i++) {
                    String str = "我是第" + i + "条目";
                    testData2.add(str);
                }
                testAdapter2.notifyItemInserted(testData2.size()+1);
            }
        });
        rv_creat.setAdapter(testAdapter2);

        testAdapter3 = new TestAdapter(testData3);
        testAdapter3.setLayoutManager(llm3);
        testAdapter3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {


            }
        });
        rv_join.setAdapter(testAdapter3);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }


}
