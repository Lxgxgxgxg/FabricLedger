package com.hyperledger.bank;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName BankAuditResult.java
 * @Description TODO
 * @createTime 2021年11月11日 19:42:00
 */
public class BankAuditResult {

    private BigInteger assetsSum;
    private int transactionTime;
    private BigDecimal transactionAverage;
    private BigDecimal transactionVariance;
    private BigDecimal transactionSkewness;
    private BigDecimal transactionKurtosis;
    private BigInteger allBankSystemAssetsSum;
    private BigDecimal bankMarketRate;
    private BigDecimal realTimeTransactionAverageValue;
    private BigDecimal HHIIndex;


    public BankAuditResult() {
    }

    public BankAuditResult(BigInteger assetsSum, int transactionTime, BigDecimal transactionAverage, BigDecimal transactionVariance,
                           BigDecimal transactionSkewness, BigDecimal transactionKurtosis, BigInteger allBankSystemAssetsSum,
                           BigDecimal bankMarketRate, BigDecimal realTimeTransactionAverageValue, BigDecimal HHIIndex) {
        this.assetsSum = assetsSum;
        this.transactionTime = transactionTime;
        this.transactionAverage = transactionAverage;
        this.transactionVariance = transactionVariance;
        this.transactionSkewness = transactionSkewness;
        this.transactionKurtosis = transactionKurtosis;
        this.allBankSystemAssetsSum = allBankSystemAssetsSum;
        this.bankMarketRate = bankMarketRate;
        this.realTimeTransactionAverageValue = realTimeTransactionAverageValue;
        this.HHIIndex = HHIIndex;
    }

    public BigInteger getAssetsSum() {
        return assetsSum;
    }

    public void setAssetsSum(BigInteger assetsSum) {
        this.assetsSum = assetsSum;
    }

    public int getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(int transactionTime) {
        this.transactionTime = transactionTime;
    }

    public BigDecimal getTransactionAverage() {
        return transactionAverage;
    }

    public void setTransactionAverage(BigDecimal transactionAverage) {
        this.transactionAverage = transactionAverage;
    }

    public BigDecimal getTransactionVariance() {
        return transactionVariance;
    }

    public void setTransactionVariance(BigDecimal transactionVariance) {
        this.transactionVariance = transactionVariance;
    }

    public BigDecimal getTransactionSkewness() {
        return transactionSkewness;
    }

    public void setTransactionSkewness(BigDecimal transactionSkewness) {
        this.transactionSkewness = transactionSkewness;
    }

    public BigDecimal getTransactionKurtosis() {
        return transactionKurtosis;
    }

    public void setTransactionKurtosis(BigDecimal transactionKurtosis) {
        this.transactionKurtosis = transactionKurtosis;
    }

    public BigInteger getAllBankSystemAssetsSum() {
        return allBankSystemAssetsSum;
    }

    public void setAllBankSystemAssetsSum(BigInteger allBankSystemAssetsSum) {
        this.allBankSystemAssetsSum = allBankSystemAssetsSum;
    }

    public BigDecimal getBankMarketRate() {
        return bankMarketRate;
    }

    public void setBankMarketRate(BigDecimal bankMarketRate) {
        this.bankMarketRate = bankMarketRate;
    }

    public BigDecimal getRealTimeTransactionAverageValue() {
        return realTimeTransactionAverageValue;
    }

    public void setRealTimeTransactionAverageValue(BigDecimal realTimeTransactionAverageValue) {
        this.realTimeTransactionAverageValue = realTimeTransactionAverageValue;
    }

    public BigDecimal getHHIIndex() {
        return HHIIndex;
    }

    public void setHHIIndex(BigDecimal HHIIndex) {
        this.HHIIndex = HHIIndex;
    }

    @Override
    public String toString() {
        return "BankAuditResult{" +
                "assetsSum=" + assetsSum +
                ", transactionTime=" + transactionTime +
                ", transactionAverage=" + transactionAverage +
                ", transactionVariance=" + transactionVariance +
                ", transactionSkewness=" + transactionSkewness +
                ", transactionKurtosis=" + transactionKurtosis +
                ", allBankSystemAssetsSum=" + allBankSystemAssetsSum +
                ", bankMarketRate=" + bankMarketRate +
                ", realTimeTransactionAverageValue=" + realTimeTransactionAverageValue +
                ", HHIIndex=" + HHIIndex +
                '}';
    }
}
