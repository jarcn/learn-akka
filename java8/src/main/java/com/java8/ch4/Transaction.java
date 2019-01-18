package com.java8.ch4;

/**
 * @author chenjia on 2019/1/18
 */
public class Transaction {

    private Trader trader;
    private int year;
    private int value;

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }

    public Trader getTrader() {
        return this.trader;
    }

    public int getYear() {
        return this.year;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "{" + this.trader + ", " +
          "year: " + this.year + ", " +
          "value:" + this.value + "}";
    }
}
