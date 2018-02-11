package com.haier.uhome.uplus.model;

/**
 * Created by suoxiaojing on 2017/8/2.
 */
public class Prize {
    private Integer id;
    private Integer activityId;
    private Integer prizeNumber;
    private String prizeName;
    private String prizeRemark;
    private Integer prizeType;
    private String prizeImg;
    private String prizeQuota;
    private Integer rate;

    public String getPrizeQuota() {
        return prizeQuota;
    }

    public void setPrizeQuota(String prizeQuota) {
        this.prizeQuota = prizeQuota;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getPrizeNumber() {
        return prizeNumber;
    }

    public void setPrizeNumber(Integer prizeNumber) {
        this.prizeNumber = prizeNumber;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeRemark() {
        return prizeRemark;
    }

    public void setPrizeRemark(String prizeRemark) {
        this.prizeRemark = prizeRemark;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeImg() {
        return prizeImg;
    }

    public void setPrizeImg(String prizeImg) {
        this.prizeImg = prizeImg;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
