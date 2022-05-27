package com.huaxixingfu.sqj.ui.activity.msg;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.hjq.permissions.Permission;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.Permissions;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.tencent.liteav.basic.IntentUtils;
import com.tencent.liteav.trtccalling.model.util.BrandUtil;
import com.tencent.liteav.trtccalling.model.util.PermissionUtil;

public class TempMessageActivity extends AppActivity {
    // 接收者Id，可以是群，也可以是人的Id
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    // 是否是群
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;
    private View tootView;
    private ImageView iv_background;

    private long targetUid;
    private String sessionId;
    private String nickName;

    /**
     * 通过Session发起聊天
     *
     * @param context 上下文
     *
     */
    public static void show(Context context,long targetUid,String sessionId, String nickName,boolean mIsGroup) {
        if (context == null)
            return;
        Intent intent = new Intent(context, TempMessageActivity.class);
        intent.putExtra(IntentKey.TARGETUID, targetUid);
        intent.putExtra(IntentKey.SESSIONID, sessionId);
        intent.putExtra(IntentKey.NICKNAME, nickName);
        intent.putExtra(IntentKey.KEY_IS_GROUP, mIsGroup);
        context.startActivity(intent);
    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        setTitle("");

        targetUid = getLong(IntentKey.TARGETUID, 0);
        sessionId = getString(IntentKey.SESSIONID);
        nickName = getString(IntentKey.NICKNAME);
        mIsGroup = getBoolean(IntentKey.KEY_IS_GROUP);

        Fragment fragment;
       /* if (mIsGroup){
            fragment = new ChatGroupFragment();
        }
        else*/
        fragment = new TempChatUserFragment();

        // 从Activity传递参数到Fragment中去
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        bundle.putLong(IntentKey.TARGETUID, targetUid);
        bundle.putString(IntentKey.SESSIONID, sessionId);
        bundle.putString(IntentKey.NICKNAME, nickName);
        bundle.putBoolean(IntentKey.KEY_IS_GROUP, mIsGroup);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, fragment)
                .commit();
    }

    @Permissions({Permission.CAMERA,Permission.RECORD_AUDIO})
    @Override
    protected void initData() {

        if (!PermissionUtil.mHasPermissionOrHasHinted) {
            checkAndRequestPermission();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_RESULT_CODE) {
            if (PermissionUtil.hasPermission(this)) {
                PermissionUtil.mHasPermissionOrHasHinted = true;
            } else {
                PermissionUtil.mHasPermissionOrHasHinted = false;
                ToastUtils.showLong("Cannot open CallView when app is in background");
            }
        }
    }

    private static final int PERMISSION_RESULT_CODE    = 1100;
    /**
     * 申请后台打开应用的权限
     * 不同厂商的权限名称不一致,例如小米:后台弹出界面; 华为:悬浮窗; 其他:锁屏界面弹框控制等.
     */
    private void checkAndRequestPermission() {
        if (!PermissionUtil.hasPermission(this)) {
            //vivo的后台权限界面跳转
            if (BrandUtil.isBrandVivo()) {
                Intent localIntent;
                if (((Build.MODEL.contains("Y85")) && (!Build.MODEL.contains("Y85A")))
                        || (Build.MODEL.contains("vivo Y53L"))) {
                    localIntent = new Intent();
                    localIntent.setClassName("com.vivo.permissionmanager",
                            "com.vivo.permissionmanager.activity.PurviewTabActivity");
                    localIntent.putExtra("packagename", getPackageName());
                    localIntent.putExtra("tabId", "1");
                    IntentUtils.safeStartActivity(getActivity(), localIntent);
                } else {
                    localIntent = new Intent();
                    localIntent.setClassName("com.vivo.permissionmanager",
                            "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity");
                    localIntent.setAction("secure.intent.action.softPermissionDetail");
                    localIntent.putExtra("packagename", getPackageName());
                    IntentUtils.safeStartActivity(getActivity(), localIntent);
                }
                return;
            } else if (BrandUtil.isBrandXiaoMi()) {
                final Dialog dialog = new Dialog(this, R.style.logoutDialogStyle);
                dialog.setContentView(R.layout.app_show_tip_dialog_confirm);
                TextView tvMessage = dialog.findViewById(R.id.tv_message);
                Button btnOk = dialog.findViewById(R.id.btn_negative);
                tvMessage.setText("为了应用在后台时响应通话请求,请到设置中打开应用“后台拉起界面”权限");
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
                //弹出一次提示后,应用未杀死前不再进行提示了
                PermissionUtil.mHasPermissionOrHasHinted = true;
                return;
            }
            //其他厂商
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSION_RESULT_CODE);
            }
        } else {
            //已经有权限
            PermissionUtil.mHasPermissionOrHasHinted = true;
        }
    }
}
