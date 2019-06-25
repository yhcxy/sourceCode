package com.midea.isc.attachment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midea.isc.common.model.BasicModel;

@JsonIgnoreProperties(value = { "attribute1", "attribute2", "attribute3", "attribute4",
        "attribute5" }, ignoreUnknown = true)
public class AttachmentList extends BasicModel {

    private java.lang.Integer attachmentId;

    private java.lang.String referenceTable;

    private java.lang.String referenceId;

    private java.lang.String application;

    private java.lang.String displayName;

    private java.lang.String fileName;

    private java.lang.String fileDirectory;

    private java.lang.Long fileSize;

    private java.lang.String fileType;

    public java.lang.Integer getAttachmentId() {
        return this.attachmentId;
    }

    public void setAttachmentId(java.lang.Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public java.lang.String getReferenceTable() {
        return this.referenceTable;
    }

    public void setReferenceTable(java.lang.String referenceTable) {
        this.referenceTable = referenceTable;
    }

    public java.lang.String getReferenceId() {
        return this.referenceId;
    }

    public void setReferenceId(java.lang.String referenceId) {
        this.referenceId = referenceId;
    }

    public java.lang.String getApplication() {
        return this.application;
    }

    public void setApplication(java.lang.String application) {
        this.application = application;
    }

    public java.lang.String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.String getFileName() {
        return this.fileName;
    }

    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    public java.lang.String getFileDirectory() {
        return this.fileDirectory;
    }

    public void setFileDirectory(java.lang.String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public java.lang.Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(java.lang.Long fileSize) {
        this.fileSize = fileSize;
    }

    public java.lang.String getFileType() {
        return this.fileType;
    }

    public void setFileType(java.lang.String fileType) {
        this.fileType = fileType;
    }
}
