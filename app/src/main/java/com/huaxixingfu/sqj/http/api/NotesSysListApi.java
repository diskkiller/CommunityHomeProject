package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 首页-公告
 */
public final class NotesSysListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.SYSTEM_NOTIES_LIST;
    }

    public final static class Bean {
       public List<NotesListApi.Bean> content;
    }
}