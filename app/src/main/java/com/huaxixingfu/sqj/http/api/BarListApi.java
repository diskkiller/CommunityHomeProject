package com.huaxixingfu.sqj.http.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 导航栏
 */
public final class BarListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.BAR_LIST;
    }

    public final static class Bean implements Parcelable {

//        public int sysBarType;
//        public long appGuideId;
//        public String sysBarTitle;
//        public String appGuideImageUrl;
//        public String sysBarImageUrl;
//        public String sysBarTypeName;
//        public String sysBarUrl;
//        public boolean isLoginFlag;
//        public String newsColumnCode;

        public Integer sysBarId;
        public String sysBarTitle;
        public String sysBarImageUrl;
        public Integer sysBarType;
        public String sysBarTypeName;
        public String sysBarUrl;
        public Boolean isLoginFlag;
        public String sysBarCode;
        public Integer sysBarPosition;
        public String sysBarPositionName;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.sysBarId);
            dest.writeString(this.sysBarTitle);
            dest.writeString(this.sysBarImageUrl);
            dest.writeValue(this.sysBarType);
            dest.writeString(this.sysBarTypeName);
            dest.writeString(this.sysBarUrl);
            dest.writeValue(this.isLoginFlag);
            dest.writeString(this.sysBarCode);
            dest.writeValue(this.sysBarPosition);
            dest.writeString(this.sysBarPositionName);
        }

        public void readFromParcel(Parcel source) {
            this.sysBarId = (Integer) source.readValue(Integer.class.getClassLoader());
            this.sysBarTitle = source.readString();
            this.sysBarImageUrl = source.readString();
            this.sysBarType = (Integer) source.readValue(Integer.class.getClassLoader());
            this.sysBarTypeName = source.readString();
            this.sysBarUrl = source.readString();
            this.isLoginFlag = (Boolean) source.readValue(Boolean.class.getClassLoader());
            this.sysBarCode = source.readString();
            this.sysBarPosition = (Integer) source.readValue(Integer.class.getClassLoader());
            this.sysBarPositionName = source.readString();
        }

        public Bean() {
        }

        protected Bean(Parcel in) {
            this.sysBarId = (Integer) in.readValue(Integer.class.getClassLoader());
            this.sysBarTitle = in.readString();
            this.sysBarImageUrl = in.readString();
            this.sysBarType = (Integer) in.readValue(Integer.class.getClassLoader());
            this.sysBarTypeName = in.readString();
            this.sysBarUrl = in.readString();
            this.isLoginFlag = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.sysBarCode = in.readString();
            this.sysBarPosition = (Integer) in.readValue(Integer.class.getClassLoader());
            this.sysBarPositionName = in.readString();
        }

        public static final Parcelable.Creator<Bean> CREATOR = new Parcelable.Creator<Bean>() {
            @Override
            public Bean createFromParcel(Parcel source) {
                return new Bean(source);
            }

            @Override
            public Bean[] newArray(int size) {
                return new Bean[size];
            }
        };
    }
}