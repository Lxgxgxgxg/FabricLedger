package com.hyperledger.audit;

import com.hyperledger.bank.BankAudit;
import com.hyperledger.bank.BankAuditResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName allBankSystemAudit.java
 * @Description TODO
 * @createTime 2021年11月12日 09:38:00
 *
 * 整个资产系统字段的审计计算
 */
public class allBankSystemAudit {

    /**
     * 审计整个系统的资产和
     * @param bankAuditResults 审计银行结果的对象数组
     * @return
     */
    public static BigInteger getAllBankSystemAssetsSum(BankAuditResult[] bankAuditResults){
        BigInteger sum = BigInteger.valueOf(0);
        for (int i = 0; i < bankAuditResults.length; i++) {
            sum = sum.add(bankAuditResults[i].getAssetsSum());
        }

        return sum;
    }


    public static BigDecimal getEveryBankMarketRate(BankAuditResult bankAuditResult, BigInteger allBankSystemSum){
        BigDecimal bankMarketRate = new BigDecimal("0");

        BigDecimal bankAssetsSum = BigIntegerToBigDecimal.transferBigIntegertoBigDecimal(bankAuditResult.getAssetsSum());
        BigDecimal allBankSystemSum1 = BigIntegerToBigDecimal.transferBigIntegertoBigDecimal(allBankSystemSum);
        bankMarketRate = bankAssetsSum.divide(allBankSystemSum1, 8, BigDecimal.ROUND_HALF_UP);

        return bankMarketRate;
    }

    public static BigDecimal getRealTimeTransactionAverageValue(BankAuditResult[] bankAuditResults, HashMap<String, String> initializeHashMap, String[] banks){

        int len = bankAuditResults.length;

        BigInteger transactionSum = new BigInteger("0");
        BigInteger transactionTimeSum = new BigInteger("0");

        for (int i = 0; i < len; i++) {
            BigInteger temp = new BigInteger(initializeHashMap.get(banks[i])).subtract(bankAuditResults[i].getAssetsSum()).abs();
            transactionSum = transactionSum.add(temp);
            transactionTimeSum = transactionTimeSum.add(BigInteger.valueOf(bankAuditResults[i].getTransactionTime()));
        }

        transactionTimeSum = transactionTimeSum.divide(BigInteger.valueOf(2));
        BigDecimal transactionSum1 = BigIntegerToBigDecimal.transferBigIntegertoBigDecimal(transactionSum);
        BigDecimal transactionTimeSum1 = BigIntegerToBigDecimal.transferBigIntegertoBigDecimal(transactionTimeSum);

        BigDecimal realTimeTransactionAverageValue = transactionSum1.divide(transactionTimeSum1, 8, BigDecimal.ROUND_HALF_UP);


        return realTimeTransactionAverageValue;
    }

    public static BigDecimal getHHIIndex(BankAuditResult[] bankAuditResults){
        BigDecimal hHIndex = new BigDecimal("0");

        for (int i = 0; i < bankAuditResults.length; i++) {
            hHIndex = hHIndex.add(bankAuditResults[i].getBankMarketRate().pow(2));
        }

        return hHIndex;
    }

}
