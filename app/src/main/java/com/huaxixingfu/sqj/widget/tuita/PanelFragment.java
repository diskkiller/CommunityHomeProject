package com.huaxixingfu.sqj.widget.tuita;


import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.widget.tuita.airpanel.UiTool;
import com.huaxixingfu.sqj.widget.tuita.recycler.RecyclerAdapter;

import java.io.File;
import java.util.List;

/**
 * 底部面板实现
 */
public class PanelFragment extends AppFragment {
    private View mFacePanel, mGalleryPanel, mRecordPanel,mAttachPanel,mBotpanel;
    private PanelCallback mCallback;


    public PanelFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_panel;
    }

    @Override
    protected void initView() {
        initFace();
        //initRecord(root);
        //initGallery(root);
        initAttach();
        initBot();
    }

    @Override
    protected void initData() {

    }





    // 开始初始化方法
    public void setup(PanelCallback callback) {
        mCallback = callback;
    }

    // 初始化表情
    private void initFace() {
        final View facePanel = mFacePanel = findViewById(R.id.lay_panel_face);

        View backspace = facePanel.findViewById(R.id.im_backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除逻辑
                PanelCallback callback = mCallback;
                if (callback == null)
                    return;

                // 模拟一个键盘删除点击
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL,
                        0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                callback.getInputEditText().dispatchKeyEvent(event);
            }
        });

        TabLayout tabLayout = (TabLayout) facePanel.findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) facePanel.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);

        // 每一表情显示48dp
        final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
        final int totalScreen = UiTool.getScreenWidth(getActivity());
        final int spanCount = totalScreen / minFaceSize;


        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 添加的
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content, container, false);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

                // 设置Adapter
                List<Face.Bean> faces = Face.all(getContext()).get(position).faces;
                FaceAdapter adapter = new FaceAdapter(faces, new RecyclerAdapter.AdapterListenerImpl<Face.Bean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {
                        if (mCallback == null)
                            return;
                        // 表情添加到输入框
                        EditText editText = mCallback.getInputEditText();
                        Face.inputFace(getContext(), editText.getText(), bean, (int)
                                (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));

                    }
                });
                recyclerView.setAdapter(adapter);

                // 添加
                container.addView(recyclerView);

                return recyclerView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // 移除的
                container.removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                // 拿到表情盘的描述
                return Face.all(getContext()).get(position).name;
            }
        });


    }



    private void initAttach() {
        final View attachPanel = mAttachPanel = findViewById(R.id.lay_panel_attach);
        final View im_add_pic = attachPanel.findViewById(R.id.im_add_pic);
        final View im_take_photo = attachPanel.findViewById(R.id.im_take_photo);

        final View im_call_video = attachPanel.findViewById(R.id.im_call_video);
        final View im_call_voice = attachPanel.findViewById(R.id.im_call_voice);
        im_add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanelCallback callback = mCallback;
                if (callback == null)
                    return;

                callback.onGotoGallery();
            }
        });
        im_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanelCallback callback = mCallback;
                if (callback == null)
                    return;

                callback.onGotoCamera();
            }
        });

        im_call_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanelCallback callback = mCallback;
                if (callback == null)
                    return;

                callback.onGotoCallVideo();
            }
        });

        im_call_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanelCallback callback = mCallback;
                if (callback == null)
                    return;

                callback.onGotoCallVoice();
            }
        });

    }

    private void initBot() {
        final View botPanel = mBotpanel = findViewById(R.id.lay_panel_bot);
    }


    public void showFace() {
        //mRecordPanel.setVisibility(View.GONE);
        //mGalleryPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.VISIBLE);
        mAttachPanel.setVisibility(View.GONE);
        mBotpanel.setVisibility(View.GONE);
    }

    public void showRecord() {
        //mRecordPanel.setVisibility(View.VISIBLE);
        //mGalleryPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.GONE);
        mAttachPanel.setVisibility(View.GONE);
        mBotpanel.setVisibility(View.GONE);
    }

    public void showGallery() {
        //mRecordPanel.setVisibility(View.GONE);
        //mGalleryPanel.setVisibility(View.VISIBLE);
        mFacePanel.setVisibility(View.GONE);
        mAttachPanel.setVisibility(View.GONE);
        mBotpanel.setVisibility(View.GONE);
    }

    public void showMore() {
        //mRecordPanel.setVisibility(View.GONE);
        //mGalleryPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.GONE);
        mBotpanel.setVisibility(View.GONE);
        mAttachPanel.setVisibility(View.VISIBLE);
    }

    public void showBotPanel() {
        //mRecordPanel.setVisibility(View.GONE);
        //mGalleryPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.GONE);
        mAttachPanel.setVisibility(View.GONE);
        mBotpanel.setVisibility(View.VISIBLE);
    }


    // 回调聊天界面的Callback
    public interface PanelCallback {
        EditText getInputEditText();

        // 返回需要发送的图片
        void onSendGallery(String[] paths);
        void onGotoGallery();
        void onGotoCamera();

        void onGotoCallVideo();
        void onGotoCallVoice();

        // 返回录音文件和时常
        void onRecordDone(File file, long time);
    }
}
