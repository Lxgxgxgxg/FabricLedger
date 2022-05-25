package com.hyperledger.audit;

import com.hyperledger.bank.BankAudit;
import com.hyperledger.bank.BankAuditResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName EveryBankAudit.java
 * @Description TODO
 * @createTime 2021年11月12日 09:39:00
 */
public class EveryBankAudit {

    /**
     * 抽调出来代码公共的部分，提高代码的服用率,想用map保存每一个银行每一次审计的结果
     */
    /**
     *
     * @param allTransactionsMap 所有交易零知识证明之后保存的结果集，包括初始化资金承诺之后的值
     * @param initializeHashMap  初始化得到每一个银行资金的结果集
     * @param hashMapComAndRSum  审计时得到的银行和具体资产和、几次方和相对应致盲因子的结果集
     * @param bank                  当前审计的银行
     * @param initializeAssetsRandom    在对初始资金进行承诺时，使用到一个随机数，这个数组保存的就是每个银行使用到的随机数
     * @param arr
     */
    public static BankAuditResult auditEveryBank(HashMap<String, HashMap<String, BankAudit>> allTransactionsMap, HashMap<String, String> initializeHashMap,
                                                 HashMap<String, BigInteger> hashMapComAndRSum, String bank, BigInteger[] initializeAssetsRandom, BigInteger[] arr){

        BankAuditResult bankAuditResult = new BankAuditResult();

        System.out.println("正在审计中......");
        BigInteger bankAssetsSum = hashMapComAndRSum.get(bank+"comSum");
        BigInteger bankRandomSum = hashMapComAndRSum.get(bank+"r1");
        boolean res = Sum.bankAssetsSum(allTransactionsMap, bank, bankAssetsSum, bankRandomSum, initializeAssetsRandom, bankAuditResult, arr);
        if (res){
            System.out.println(bank+"资产和审计通过，其值为：" + bankAuditResult.getAssetsSum());
        }else {
            System.out.println("资产和审计不通过！");
            return null;
        }


        BigInteger bankBitComSum = hashMapComAndRSum.get(bank+"transactionTime");
        BigInteger bankBitComRandomSum = hashMapComAndRSum.get(bank+"r2");
        boolean res1 = Sum.bankTransactionTime(allTransactionsMap, bank, bankBitComSum, bankBitComRandomSum, bankAuditResult, arr);
        Sum.bankTransactionAverageValue(new BigInteger(initializeHashMap.get(bank)), bankAssetsSum, bankBitComSum, bankAuditResult);
        /**
         * 改变新的初始值，因为下次审计的时候，初始值就是现在的值
         */
        if (res1){
            System.out.println(bank+"资产交易次数审计通过，其值为：" + bankAuditResult.getTransactionTime()+",资产交易的平均值为："+bankAuditResult.getTransactionAverage());
        }else {
            System.out.println("资产交易次数审计不通过！");
            return null;
        }

//        System.out.println("输入银行1的交易金额平方和和交易金额平方承诺的致盲因子和：");
        BigInteger bankDoubleAssetsSum = hashMapComAndRSum.get(bank+"doubleComSum");
        BigInteger bankDoubleComRandomSum = hashMapComAndRSum.get(bank+"x");
        BigDecimal variance = Variance.calculateVariance(allTransactionsMap, bank, bankDoubleAssetsSum, bankDoubleComRandomSum, bankAuditResult.getTransactionAverage(),
                bankBitComSum, bankAuditResult, arr);
        bankAuditResult.setTransactionVariance(variance);
        System.out.println(bank+"资产交易方差为 " + variance);

//        System.out.println("输入银行1的交易金额立方和和交易金额立方承诺的致盲因子和：");
        BigInteger bankTripleAssetsSum = hashMapComAndRSum.get(bank+"tripleComSum");
        BigInteger bankTripleComRandomSum = hashMapComAndRSum.get(bank+"y");
        BigDecimal skewness = Skewness.calaulateSkewness(allTransactionsMap, bank, bankTripleAssetsSum, bankTripleComRandomSum, bankDoubleAssetsSum,
                bankAuditResult.getTransactionAverage(), bankBitComSum, variance, arr);
        bankAuditResult.setTransactionSkewness(skewness);
        System.out.println(bank+"资产交易偏度为 " + skewness);


//        System.out.println("输入银行1的交易金额四次方和和交易金额四次方承诺的致盲因子和：");
        BigInteger bankQuadraAssetsSum = hashMapComAndRSum.get(bank+"quadraComSum");
        BigInteger bankQuadraComRandomSum = hashMapComAndRSum.get(bank+"z");
        BigDecimal kurtosis = Kurtosis.calculateKurtosis(allTransactionsMap, bank, bankQuadraAssetsSum, bankQuadraComRandomSum, bankTripleAssetsSum, bankDoubleAssetsSum,
                bankAuditResult.getTransactionAverage(), bankBitComSum, variance, arr);
        bankAuditResult.setTransactionKurtosis(kurtosis);
        System.out.println(bank+"资产交易峰度= " + kurtosis);


        return bankAuditResult;

    }
}
