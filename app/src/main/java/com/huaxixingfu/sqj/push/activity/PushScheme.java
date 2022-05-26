package com.huaxixingfu.sqj.push.activity;

import android.content.Context;
import android.content.Intent;

import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.ui.fragment.FragmentLife;
import com.huaxixingfu.sqj.ui.fragment.FragmentMsgList;

/**
 * Created by lsm on 2022/5/25.
 * Describe: 推送的路由定义
 */
public class PushScheme {

    // 文本
    public static final int TEXT = 0;
    // 文件
    public static final int FILE = 0;
    // 图片
    public static final int PICTURE = 0;
    // 视频
    public static final int VIDEO = 0;
    // 语音
    public static final int VOICE = 0;
    // 视频通话
    public static final int VIDEO_CHAT = 10;
    // 语音通话
    public static final int VOICE_CHAT = 10;
    //通知公告,链接
    public static final int NOTICE = 20;
    //调研问卷
    public static final int QUESTIONNAIRE = 30;
    // 活动
    public static final int ACTIVITY = 31;
    // 投票
    public static final int VOTE = 32;
    // 自定义表单
    public static final int CUSTOMER_FORM = 33;
    // 图文圈子
    public static final int GRAPHIC_CIRCLE = 40;
    // 视频圈子
    public static final int VIDEO_CIRCLE = 50;

    public static boolean matchRouter(Context context, int type, String path) {
        boolean match = false;
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        switch (type) {
            case TEXT:
                match = true;
                intent.putExtra(HomeActivity.INTENT_KEY_IN_FRAGMENT_CLASS, FragmentMsgList.class);

                break;
            case VIDEO_CHAT: // VOICE_CHAT
                match = true;
                intent.putExtra(HomeActivity.INTENT_KEY_IN_FRAGMENT_CLASS, FragmentMsgList.class);

                break;
            case NOTICE:

                break;
            case QUESTIONNAIRE:

                break;
            case ACTIVITY:

                break;
            case VOTE:

                break;
            case CUSTOMER_FORM:

                break;

            case GRAPHIC_CIRCLE:
                match = true;
                intent.putExtra(HomeActivity.INTENT_KEY_IN_FRAGMENT_CLASS, FragmentLife.class);

                break;
            case VIDEO_CIRCLE:
                match = true;
                intent.putExtra(HomeActivity.INTENT_KEY_IN_FRAGMENT_CLASS, FragmentLife.class);

                break;
        }
        if (match){
            context.startActivity(intent);
        }
        return match;
    }

//    enum APNSMsgType: Int, Decodable {
//
//        case text = 0, file, picture, video, voice
//        case videochat = 10, voicechat
//        //通知公告,链接
//        case notice = 20, link
//        //调研问卷
//        case questionnaire = 30
//        // 活动
//        case activity = 31
//        // 投票
//        case vote = 32
//        // 自定义表单
//        case customerform = 33
//        // 图文圈子
//        case graphiccircle = 40
//        // 视频圈子
//        case videocircle = 50
//
//        var applink: String {
//            switch self {
//                case .text, .file, .picture, .video, .link, .voice, .videochat, .voicechat:
//                return "CommunityHome://chat" // 聊天首页
//                case .notice:
//                return "CommunityHome://chat-notice" // 社区公告
//                case .activity, .questionnaire, .vote, .customerform: // 任务消息
//                return "CommunityHome://chat-task"
//                case .graphiccircle, .videocircle: // 生活圈
//                return "CommunityHome://circle"
//            }
//        }
//    }


}
