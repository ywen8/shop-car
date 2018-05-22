package com.yw.car.model;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/9.
 */

public class SPRimHeadlin implements SPModel, Serializable {
    private String adName;
    private String adCode;
    private int mediaType;
    private String adLink;

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public String getAdName() {
        return adName;
    }

    public String getAdCode() {
        return adCode;
    }

    public int getMediaType() {
        return mediaType;
    }

    public String getAdLink() {
        return adLink;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "adName", "ad_name", "adCode", "ad_code", "mediaType", "media_type", "adLink", "ad_link"
        };
    }
}
