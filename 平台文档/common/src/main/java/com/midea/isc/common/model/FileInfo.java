package com.midea.isc.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public final class FileInfo implements Serializable {

    private Integer attachmentId;
    private String referenceTable;
    private String referenceId;
    private String application;
    private String displayName;
    private String fileName;
    private String fileType;
    private byte[] body;
}