package com.huaxixingfu.sqj.bean;

public class PersonDataBean {

    /**
     * userAvatarUrl	string
     * title: 头像
     * userNickName	string
     * title: 昵称
     * userSexCode	string
     * title: 性别Code
     * userSexCodeName	string
     * title: 性别
     * residentBir	integer($int32)
     * title: 出生日期
     * userNationCode	string
     * title: 民族Code
     * userNationCodeName	string
     * title: 民族
     * userSignName	string
     * title: 个性签名
     * residentPoliticsFace	string
     * title: 政治面貌Code
     * residentPoliticsFaceName	string
     * title: 政治面貌
     * residentCareerInfo	string
     * title: 职业信息
     * cardStartus	integer($int32)
     * title: 身份证号认证状态 0未认证 1认证通过 2认证未通过
     * cardStartusName	string
     * title: 身份证号认证状态名称
     * userResidentCertStatus	integer($int32)
     * title: 居民认证-状态（0未认证/1待认证/2认证通过/3认证驳回）
     * userResidentCertStatusName	string
     * title: 居民认证-状态名称
     * userPhone	string
     * title: 手机号
     */
    private int userId;
    private String userAvatarUrl;
    private String userNickName;
    private String userSexCode;
    private String userSexCodeName;
    private String residentBir;
    private String userNationCode;
    private String userNationCodeName;
    private String userSignName;
    private String residentPoliticsFace;
    private String residentPoliticsFaceName;
    private String residentCareerInfo;
    private int cardStartus;
    private String cardStartusName;
    private int userResidentCertStatus;
    private String userResidentCertStatusName;
    private String userPhone;
    private int userBir;

    public int getUserBir() {
        return userBir;
    }

    public void setUserBir(int userBir) {
        this.userBir = userBir;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }
    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
    public String getUserNickName() {
        return userNickName;
    }

    public void setUserSexCode(String userSexCode) {
        this.userSexCode = userSexCode;
    }
    public String getUserSexCode() {
        return userSexCode;
    }

    public void setUserSexCodeName(String userSexCodeName) {
        this.userSexCodeName = userSexCodeName;
    }
    public String getUserSexCodeName() {
        return userSexCodeName;
    }

    public void setResidentBir(String residentBir) {
        this.residentBir = residentBir;
    }
    public String getResidentBir() {
        return residentBir;
    }

    public void setUserNationCode(String userNationCode) {
        this.userNationCode = userNationCode;
    }
    public String getUserNationCode() {
        return userNationCode;
    }

    public void setUserNationCodeName(String userNationCodeName) {
        this.userNationCodeName = userNationCodeName;
    }
    public String getUserNationCodeName() {
        return userNationCodeName;
    }

    public void setUserSignName(String userSignName) {
        this.userSignName = userSignName;
    }
    public String getUserSignName() {
        return userSignName;
    }

    public void setResidentPoliticsFace(String residentPoliticsFace) {
        this.residentPoliticsFace = residentPoliticsFace;
    }
    public String getResidentPoliticsFace() {
        return residentPoliticsFace;
    }

    public void setResidentPoliticsFaceName(String residentPoliticsFaceName) {
        this.residentPoliticsFaceName = residentPoliticsFaceName;
    }
    public String getResidentPoliticsFaceName() {
        return residentPoliticsFaceName;
    }

    public void setResidentCareerInfo(String residentCareerInfo) {
        this.residentCareerInfo = residentCareerInfo;
    }
    public String getResidentCareerInfo() {
        return residentCareerInfo;
    }

    public void setCardStartus(int cardStartus) {
        this.cardStartus = cardStartus;
    }
    public int getCardStartus() {
        return cardStartus;
    }

    public void setCardStartusName(String cardStartusName) {
        this.cardStartusName = cardStartusName;
    }
    public String getCardStartusName() {
        return cardStartusName;
    }

    public void setUserResidentCertStatus(int userResidentCertStatus) {
        this.userResidentCertStatus = userResidentCertStatus;
    }
    public int getUserResidentCertStatus() {
        return userResidentCertStatus;
    }

    public void setUserResidentCertStatusName(String userResidentCertStatusName) {
        this.userResidentCertStatusName = userResidentCertStatusName;
    }
    public String getUserResidentCertStatusName() {
        return userResidentCertStatusName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public String getUserPhone() {
        return userPhone;
    }

    @Override
    public String toString() {
        return "PersonDataBean{" +
                "userId=" + userId +
                ", userAvatarUrl='" + userAvatarUrl + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", userSexCode='" + userSexCode + '\'' +
                ", userSexCodeName='" + userSexCodeName + '\'' +
                ", residentBir='" + residentBir + '\'' +
                ", userNationCode='" + userNationCode + '\'' +
                ", userNationCodeName='" + userNationCodeName + '\'' +
                ", userSignName='" + userSignName + '\'' +
                ", residentPoliticsFace='" + residentPoliticsFace + '\'' +
                ", residentPoliticsFaceName='" + residentPoliticsFaceName + '\'' +
                ", residentCareerInfo='" + residentCareerInfo + '\'' +
                ", cardStartus=" + cardStartus +
                ", cardStartusName='" + cardStartusName + '\'' +
                ", userResidentCertStatus=" + userResidentCertStatus +
                ", userResidentCertStatusName='" + userResidentCertStatusName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }
}
