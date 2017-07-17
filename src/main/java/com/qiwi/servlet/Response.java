package com.qiwi.servlet;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by etrofimov on 12.07.17.
 */
public class Response {
    private Status code;
    private BigDecimal balance;

    public Response(Status code) {
        this.code = code;
        this.balance = new BigDecimal(0);
    }

    public Response(Status code, BigDecimal balance) {
        this.code = code;
        this.balance = balance.setScale(2, RoundingMode.CEILING);
    }

    public Status getCode() {
        return this.code;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }
}
