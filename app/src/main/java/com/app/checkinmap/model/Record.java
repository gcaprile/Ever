package com.app.checkinmap.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ASUS-PC on 03/10/2017.
 */

public class Record implements Serializable {

    @SerializedName("Name")
    private String mName;

    @SerializedName("attributes")
    private Attribute mAttributes;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Attribute getAttributes() {
        return mAttributes;
    }

    public void setAttributes(Attribute attributes) {
        this.mAttributes = attributes;
    }

    public static class Attribute{
        @SerializedName("type")
        private String mType;

        @SerializedName("url")
        private String mUrl;

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            this.mType = type;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String url) {
            this.mUrl = url;
        }
    }
}
