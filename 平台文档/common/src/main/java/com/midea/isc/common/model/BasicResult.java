package com.midea.isc.common.model;

import com.midea.isc.common.sys.IscException;

public final class BasicResult<T> {

    private String resultCode;

    private String resultMsg;

    private T data;

    public BasicResult() {

    }

    public BasicResult(String code) {
        this.resultCode = code;
    }

    public BasicResult(String code, String msg) {
        this.resultCode = code;
        this.resultMsg = msg;
    }

    public BasicResult(String code, String msg, T data) {
        this.resultCode = code;
        this.resultMsg = msg;
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return "{\"resultCode\":\"" + resultCode + "\",\"resultMsg\":" + resultMsg + "}";
    }

    /**
     * @Description 此方法用于协助系统异常或者断路器返回值的判断，如果是自定义异常建议自己判断处理
     * @author WANGXK7
     * @date 2019年4月19日 上午10:04:20
     * @return
     * @throws IscException
     * @lastModifier
     */
    public T checkResult() throws IscException {
        if (!this.resultCode.equals("ISC-000")) {
            throw new IscException(this.resultCode, this.resultMsg);
        }

        return this.data;
    }
}