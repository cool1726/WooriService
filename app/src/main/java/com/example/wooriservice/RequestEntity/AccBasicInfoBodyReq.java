package com.example.wooriservice.RequestEntity;

import java.io.Serializable;

public class AccBasicInfoBodyReq implements Serializable {
    private String INQ_ACNO;
    private String INQ_BAS_DT;
    private String INQ_CUCD;
    private String ACCT_KND;

    public String getINQ_ACNO() {
        return INQ_ACNO;
    }

    public void setINQ_ACNO(String INQ_ACNO) {
        this.INQ_ACNO = INQ_ACNO;
    }

    public String getINQ_BAS_DT() {
        return INQ_BAS_DT;
    }

    public void setINQ_BAS_DT(String INQ_BAS_DT) {
        this.INQ_BAS_DT = INQ_BAS_DT;
    }

    public String getINQ_CUCD() { return INQ_CUCD; }

    public void setINQ_CUCD(String INQ_CUCD) { this.INQ_CUCD = INQ_CUCD; }

    public String getACCT_KND() { return ACCT_KND; }

    public void setACCT_KND(String ACCT_KND) { this.ACCT_KND = ACCT_KND; }
}