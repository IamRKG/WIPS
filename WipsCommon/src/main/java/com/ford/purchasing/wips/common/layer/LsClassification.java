//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

@SuppressWarnings("javadoc")
public class LsClassification {

    private String code;
    private String description;
    private String account;
    private String subDivision;
    private String department;
    private String financeAnalyst;
    private String prePayInd;
    private String prePayAccType;
    private String nationalCompany;
    private String plant;

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(final String account) {
        this.account = account;
    }

    public String getSubDivision() {
        return this.subDivision;
    }

    public void setSubDivision(final String subDivision) {
        this.subDivision = subDivision;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public String getFinanceAnalyst() {
        return this.financeAnalyst;
    }

    public void setFinanceAnalyst(final String financeAnalyst) {
        this.financeAnalyst = financeAnalyst;
    }

    public String getPrePayInd() {
        return this.prePayInd;
    }

    public void setPrePayInd(final String prePayInd) {
        this.prePayInd = prePayInd;
    }

    public String getPrePayAccType() {
        return this.prePayAccType;
    }

    public void setPrePayAccType(final String prePayAccType) {
        this.prePayAccType = prePayAccType;
    }

    public String getNationalCompany() {
        return this.nationalCompany;
    }

    public void setNationalCompany(final String nationalCompany) {
        this.nationalCompany = nationalCompany;
    }

    public String getPlant() {
        return this.plant;
    }

    public void setPlant(final String plant) {
        this.plant = plant;
    }

}
