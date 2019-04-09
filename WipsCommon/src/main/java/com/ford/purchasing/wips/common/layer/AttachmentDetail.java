//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import java.math.BigInteger;

public class AttachmentDetail {

    private BigInteger id;

    private String name;

    private String description;

    private String uploadDate;

    public BigInteger getId() {
        return this.id;
    }

    public void setId(final BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {

        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {

        this.description = description;
    }

    public String getUploadDate() {
        return this.uploadDate;
    }

    public void setUploadDate(final String uploadDate) {

        this.uploadDate = uploadDate;
    }

}
