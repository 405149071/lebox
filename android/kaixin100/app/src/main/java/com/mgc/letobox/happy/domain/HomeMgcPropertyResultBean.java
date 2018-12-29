package com.mgc.letobox.happy.domain;

import java.io.Serializable;

/**
 * Created by liu hong liang on 2016/11/14.
 */

public class HomeMgcPropertyResultBean implements  Serializable {
    private float property;
    private float ttProperty;

    public float getProperty() {
        return property;
    }

    public void setProperty(float property) {
        this.property = property;
    }

    public float getTtProperty() {
        return ttProperty;
    }

    public void setTtProperty(float ttProperty) {
        this.ttProperty = ttProperty;
    }

    public float getTtPropertyDay() {
        return ttPropertyDay;
    }

    public void setTtPropertyDay(float ttPropertyDay) {
        this.ttPropertyDay = ttPropertyDay;
    }

    private float ttPropertyDay;

    public float getTtAmount() {
        return ttAmount;
    }

    public void setTtAmount(float ttAmount) {
        this.ttAmount = ttAmount;
    }

    private float ttAmount;

}
