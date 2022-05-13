package com.huaxixingfu.sqj.bean;

public class RealNameDataBean {

    /**
     * cardStartus	integer($int32)
     * title: 身份证号认证状态 0未认证 1认证通过 2认证未通过
     * cardStartusName	string
     * title: 身份证号认证状态名称
     *
     * userName	string
     * title: 姓名
     * cardNumber	string
     * title: 身份证号
     * cardFrontUrl	string
     * title: 身份证正面地址
     * cardBackUrl	string
     * title: 身份证背面地址
     * cardStartus	integer($int32)
     * title: 实名认证状态Code
     * cardStartusName	string
     * title: 实名认证状态
     * cardAuthAt	string($date-time)
     * title: 实名认证时间
     */
    private String userName;
    private String cardNumber;
    private String cardFrontUrl;
    private String cardBackUrl;
    private int cardStartus;
    private String cardStartusName;
    private String cardAuthAt;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardFrontUrl() {
        return cardFrontUrl;
    }

    public void setCardFrontUrl(String cardFrontUrl) {
        this.cardFrontUrl = cardFrontUrl;
    }

    public String getCardBackUrl() {
        return cardBackUrl;
    }

    public void setCardBackUrl(String cardBackUrl) {
        this.cardBackUrl = cardBackUrl;
    }

    public int getCardStartus() {
        return cardStartus;
    }

    public void setCardStartus(int cardStartus) {
        this.cardStartus = cardStartus;
    }

    public String getCardStartusName() {
        return cardStartusName;
    }

    public void setCardStartusName(String cardStartusName) {
        this.cardStartusName = cardStartusName;
    }

    public String getCardAuthAt() {
        return cardAuthAt;
    }

    public void setCardAuthAt(String cardAuthAt) {
        this.cardAuthAt = cardAuthAt;
    }
}
