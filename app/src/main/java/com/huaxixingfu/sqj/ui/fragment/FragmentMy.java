package com.huaxixingfu.sqj.ui.fragment;

import static com.huaxixingfu.sqj.utils.Encryption.ByteArrayToHexStr;
import static com.huaxixingfu.sqj.utils.Encryption.HexStrToByteArray;
import static com.huaxixingfu.sqj.utils.MySm4Util.Sm4EnOrDecrypt_EcbMode;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.SettingBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.TitleBarFragment;
import com.huaxixingfu.sqj.bean.PersonDataBean;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.http.api.PersonalDataApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.ui.activity.me.AccountSecurityActivity;
import com.huaxixingfu.sqj.ui.activity.me.FamilyDateActivity;
import com.huaxixingfu.sqj.ui.activity.me.FeedbackActivity;
import com.huaxixingfu.sqj.ui.activity.me.PersonalDataActivity;
import com.huaxixingfu.sqj.ui.activity.me.SettingActivity;
import com.huaxixingfu.sqj.utils.Encryption;
import com.huaxixingfu.sqj.utils.MySm4Util;
import com.huaxixingfu.sqj.utils.MyTime;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Key;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentMy extends TitleBarFragment<HomeActivity> {

    private PersonDataBean personDataBean;

    public static FragmentMy newInstance() {
        return new FragmentMy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_my;
    }

    private void getPersonDate(){
        EasyHttp.post(this)
                .api(new PersonalDataApi())
                .request(new HttpCallback<HttpData<PersonDataBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<PersonDataBean> data) {
                        if(data.getData() != null){
                            personDataBean = data.getData();
                            setUserDate();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    private LinearLayout llHeader;
    private TextView tvUserDate,tvLogout,tvTitle;
    private SettingBar user_setting_family,user_account_security,user_feedback,user_setting;
    private ImageView icIcon;

    public void initView() {
        llHeader = findViewById(R.id.ll_header);
        tvUserDate = findViewById(R.id.tv_user_date);
        tvLogout = findViewById(R.id.tv_logout);
        tvTitle = findViewById(R.id.tv_title);
        icIcon = findViewById(R.id.ic_icon);
        user_setting_family = findViewById(R.id.sb_user_setting_family);
        user_account_security = findViewById(R.id.sb_user_account_security);
        user_feedback = findViewById(R.id.sb_user_feedback);
        user_setting = findViewById(R.id.sb_user_setting);

       setOnClickListener(R.id.ll_header,R.id.tv_user_date,
               R.id.tv_logout,R.id.sb_user_setting_family,
               R.id.sb_user_account_security,R.id.sb_user_feedback,R.id.sb_user_setting);
    }

    @Override
    protected void initData() {
        getPersonDate();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(SPManager.instance(getContext()).isLogin()){
            getPersonDate();
        }else{
            tvTitle.setText("未登录");
            tvUserDate.setVisibility(View.GONE);
            tvLogout.setVisibility(View.GONE);
            Glide.with(getActivity()).load(R.drawable.avatar_placeholder_ic).into(icIcon);
        }
    }


    private void setUserDate(){
        if(personDataBean != null ){
            SPManager.instance(getActivity()).putModel(Constants.USERINFO,personDataBean);
            if(StringUtils.isNotEmpty(personDataBean.getUserAvatarUrl()))
                Glide.with(getActivity()).load(personDataBean.getUserAvatarUrl()).into(icIcon);
            tvTitle.setText(personDataBean.getUserNickName());
            tvUserDate.setVisibility(View.VISIBLE);
            tvLogout.setVisibility(View.VISIBLE);
        }
    }


    @SingleClick
    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId != R.id.sb_user_setting && !SPManager.instance(getContext()).isLogin()){
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }

        if (viewId == R.id.tv_user_date) {

            startActivity(new Intent(getActivity(), PersonalDataActivity.class));

        }else if (viewId == R.id.sb_user_setting_family) {
            startActivity(new Intent(getActivity(), FamilyDateActivity.class));

        }else if(viewId == R.id.sb_user_account_security){
            startActivity(new Intent(getActivity(), AccountSecurityActivity.class));

        }else if(viewId == R.id.sb_user_feedback){
            startActivity(new Intent(getActivity(), FeedbackActivity.class));
        }else if(viewId == R.id.sb_user_setting){
            startActivity(new Intent(getActivity(), SettingActivity.class));

        }else if(viewId == R.id.tv_logout){
            //ActivityManager.getInstance().loginOut(getActivity());



            System.out.println("★★★★★DES★★★★★");
            //以下数据均为16进制
            String desKey = "FF34567890ABCDEF";
            String desData = "3030303030303030";
            System.out.println("密钥：" + desKey);
            System.out.println("数据：" + desData);
            byte[] keyBytes = HexStrToByteArray(desKey);
            byte[] dataBytes = HexStrToByteArray(desData);

            /*//加密
            byte[] encrypt = DesEnOrDecrypt_EcbMode(dataBytes, keyBytes, Cipher.ENCRYPT_MODE);
            String hexCipher = ByteArrayToHexStr(encrypt);
            System.out.println("使用bcprov-jdk15to18-1.66.jar实现DES加密：" + hexCipher);
            //解密
            byte[] cipherBytes = HexStrToByteArray(hexCipher);
            byte[] decryptBytes = DesEnOrDecrypt_EcbMode(cipherBytes, keyBytes, Cipher.DECRYPT_MODE);
            String hexPlain = ByteArrayToHexStr(decryptBytes);
            System.out.println("使用bcprov-jdk15to18-1.66.jar实现DES解密：" + hexPlain);
            //加密
            encrypt = DesEnOrDecrypt_EcbMode_My(dataBytes, keyBytes, Cipher.ENCRYPT_MODE);
            hexCipher = ByteArrayToHexStr(encrypt);
            System.out.println("手动实现DES加密：" + hexCipher);
            //解密
            cipherBytes = HexStrToByteArray(hexCipher);
            decryptBytes = DesEnOrDecrypt_EcbMode_My(cipherBytes, keyBytes, Cipher.DECRYPT_MODE);
            hexPlain = ByteArrayToHexStr(decryptBytes);
            System.out.println("手动实现DES解密：" + hexPlain);*/

            System.out.println("\n★★★★★SM4★★★★★");
            //以下数据均为16进制
            String sm4Key = "FA17113405706F714D7B973DB89F0B47";
            String sm4Data = "123456";
            System.out.println("密钥：" + sm4Key);
            System.out.println("数据：" + sm4Data);
            keyBytes = HexStrToByteArray(sm4Key);
            dataBytes = HexStrToByteArray(sm4Data);
            //加密
            byte[] encrypt = new byte[0];
            try {
                encrypt = Sm4EnOrDecrypt_EcbMode(dataBytes, keyBytes, Cipher.ENCRYPT_MODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String hexCipher = ByteArrayToHexStr(encrypt);
            System.out.println("使用bcprov-jdk15to18-1.66.jar实现SM4加密：" + hexCipher);
            //解密
            byte[] cipherBytes = HexStrToByteArray(hexCipher);
            byte[] decryptBytes = new byte[0];
            try {
                decryptBytes = Sm4EnOrDecrypt_EcbMode(cipherBytes, keyBytes, Cipher.DECRYPT_MODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String hexPlain = ByteArrayToHexStr(decryptBytes);
            System.out.println("使用bcprov-jdk15to18-1.66.jar实现SM4解密：" + hexPlain);
            byte[] out = new byte[16];
            MySm4Util mySm4Util = new MySm4Util();
            mySm4Util.EnOrDecrypt(dataBytes, 16, keyBytes, out, 1);
            System.out.println("手动实现SM4加密：" + ByteArrayToHexStr(out));
            byte[] in = new byte[16];
            mySm4Util.EnOrDecrypt(out, 16, keyBytes, in, 0);
            System.out.println("手动实现SM4解密：" + ByteArrayToHexStr(in));


        }
    }

    /**
     * SM4算法的ECB模式的加密、解密
     * 注意：ECB模式下没有IV（偏移量）
     * 注意：SM4的密钥长度必须为16个字节，数据长度必须为16的整数倍
     *
     * @param dataBytes 如果是加密则代表明文，如果是解密则代表密文
     * @param keyBytes  密钥，固定为16个字节
     * @param mode      加/解密
     * @return 如果是加密返回的是密文，如果是解密则返回的是明文
     * @throws Exception
     */
    public static byte[] Sm4EnOrDecrypt_EcbMode(byte[] dataBytes, byte[] keyBytes, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance("SM4/ECB/NoPadding", new BouncyCastleProvider());
        Key key = new SecretKeySpec(keyBytes, "SM4");
        cipher.init(mode, key);
        return cipher.doFinal(dataBytes);
    }
}
