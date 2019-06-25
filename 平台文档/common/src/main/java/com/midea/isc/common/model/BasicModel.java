package com.midea.isc.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.midea.isc.common.param.BasicParam;

public class BasicModel extends BasicParam {
    private java.lang.String attribute1;

    private java.lang.String attribute2;

    private java.lang.String attribute3;

    private java.lang.String attribute4;

    private java.lang.String attribute5;

    private java.util.Date creationDate;

    private java.lang.String createdBy;

    private java.util.Date lastUpdateDate;

    private java.lang.String lastUpdatedBy;

    private java.lang.String attribute1Cond;

    private java.lang.String attribute2Cond;

    private java.lang.String attribute3Cond;

    private java.lang.String attribute4Cond;

    private java.lang.String attribute5Cond;

    private java.lang.String createdByCond;

    private java.util.Date creationDateFrom;

    private java.util.Date creationDateTo;

    private java.lang.String lastUpdatedByCond;

    private java.util.Date lastUpdateDateFrom;

    private java.util.Date lastUpdateDateTo;

    public java.lang.String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(java.lang.String attribute1) {
        this.attribute1 = attribute1;
    }

    public java.lang.String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(java.lang.String attribute2) {
        this.attribute2 = attribute2;
    }

    public java.lang.String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(java.lang.String attribute3) {
        this.attribute3 = attribute3;
    }

    public java.lang.String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(java.lang.String attribute4) {
        this.attribute4 = attribute4;
    }

    public java.lang.String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(java.lang.String attribute5) {
        this.attribute5 = attribute5;
    }

    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    public java.util.Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.util.Date creationDate) {
        this.creationDate = creationDate;
    }

    public java.lang.String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(java.lang.String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public java.util.Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(java.util.Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @JsonIgnore
    public java.lang.String getAttribute1Cond() {
        return attribute1Cond;
    }

    @JsonProperty
    public void setAttribute1Cond(java.lang.String attribute1Cond) {
        this.attribute1Cond = attribute1Cond;
    }

    @JsonIgnore
    public java.lang.String getAttribute2Cond() {
        return attribute2Cond;
    }

    @JsonProperty
    public void setAttribute2Cond(java.lang.String attribute2Cond) {
        this.attribute2Cond = attribute2Cond;
    }

    @JsonIgnore
    public java.lang.String getAttribute3Cond() {
        return attribute3Cond;
    }

    @JsonProperty
    public void setAttribute3Cond(java.lang.String attribute3Cond) {
        this.attribute3Cond = attribute3Cond;
    }

    @JsonIgnore
    public java.lang.String getAttribute4Cond() {
        return attribute4Cond;
    }

    @JsonProperty
    public void setAttribute4Cond(java.lang.String attribute4Cond) {
        this.attribute4Cond = attribute4Cond;
    }

    @JsonIgnore
    public java.lang.String getAttribute5Cond() {
        return attribute5Cond;
    }

    @JsonProperty
    public void setAttribute5Cond(java.lang.String attribute5Cond) {
        this.attribute5Cond = attribute5Cond;
    }

    @JsonIgnore
    public java.lang.String getCreatedByCond() {
        return createdByCond;
    }

    @JsonProperty
    public void setCreatedByCond(java.lang.String createdByCond) {
        this.createdByCond = createdByCond;
    }

    @JsonIgnore
    public java.util.Date getCreationDateFrom() {
        return creationDateFrom;
    }

    @JsonProperty
    public void setCreationDateFrom(java.util.Date creationDateFrom) {
        this.creationDateFrom = creationDateFrom;
    }

    @JsonIgnore
    public java.util.Date getCreationDateTo() {
        return creationDateTo;
    }

    @JsonProperty
    public void setCreationDateTo(java.util.Date creationDateTo) {
        this.creationDateTo = creationDateTo;
    }

    @JsonIgnore
    public java.lang.String getLastUpdatedByCond() {
        return lastUpdatedByCond;
    }

    @JsonProperty
    public void setLastUpdatedByCond(java.lang.String lastUpdatedByCond) {
        this.lastUpdatedByCond = lastUpdatedByCond;
    }

    @JsonIgnore
    public java.util.Date getLastUpdateDateFrom() {
        return lastUpdateDateFrom;
    }

    @JsonProperty
    public void setLastUpdateDateFrom(java.util.Date lastUpdateDateFrom) {
        this.lastUpdateDateFrom = lastUpdateDateFrom;
    }

    @JsonIgnore
    public java.util.Date getLastUpdateDateTo() {
        return lastUpdateDateTo;
    }

    @JsonProperty
    public void setLastUpdateDateTo(java.util.Date lastUpdateDateTo) {
        this.lastUpdateDateTo = lastUpdateDateTo;
    }

}
