package com.huaxixingfu.sqj.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.http.api.MailListApi;

import java.util.List;

/**
 *    desc   : 通信录列表
 */
public final class GroupAddMemberVerAdapter extends BaseQuickAdapter<MailListApi.Bean,BaseViewHolder> {

    List<MailListApi.Bean> data;
    private LinearLayoutManager mLayoutManager;

    public GroupAddMemberVerAdapter(@Nullable List<MailListApi.Bean> data) {
        super(R.layout.sqj_item_add_member_group, data);
        this.data = data;

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MailListApi.Bean contactsListBean) {
        //群中已添加的成员不可再次选中
        baseViewHolder.setGone(R.id.v_not_sel_bg,!contactsListBean.isAdd);

        baseViewHolder.setText(R.id.tv_nickname, contactsListBean.chatFriendNiceName);
        boolean isSelect = contactsListBean.isSelect;
        boolean isAdd = contactsListBean.isAdd;
        ImageView iv_select = baseViewHolder.getView(R.id.iv_select);
        if (isSelect || isAdd) {
            iv_select.setImageResource(R.mipmap.share_to_family_selected);
        } else {
            iv_select.setImageResource(R.mipmap.share_to_family_unselected);
        }
    }

    public void setLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

    /**
     * 滚动RecyclerView到索引位置
     *
     * @param index
     */
    public void scrollToSection(String index) {
        if (data == null || data.isEmpty()) return;
        if (TextUtils.isEmpty(index)) return;
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(index.substring(0, 1), data.get(i).getSection().substring(0, 1))) {
                if (mLayoutManager != null) {
                    mLayoutManager.scrollToPositionWithOffset(i + getHeaderLayoutCount(), 0);
                    return;
                }
            }
        }
    }
}