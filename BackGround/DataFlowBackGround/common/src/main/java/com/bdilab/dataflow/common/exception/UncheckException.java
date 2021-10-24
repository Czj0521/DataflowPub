package com.bdilab.dataflow.common.exception;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-17
 * @description:
 **/
public class UncheckException extends RuntimeException {
    Integer code;

    public UncheckException(String msg) {
        super(msg);
    }

    public UncheckException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }
}
