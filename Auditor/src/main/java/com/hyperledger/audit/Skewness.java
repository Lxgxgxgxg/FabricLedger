package com.hyperledger.audit;

import com.hyperledger.bank.BankAudit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import static com.hyperledger.audit.BigIntegerToBigDecimal.transferBigIntegertoBigDecimal;
import static com.hyperledger.audit.CommitHomomorphism.tripleComHomomorphismSum;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Skewness.java
 * @Description TODO
 * @createTime 2021年11月11日 21:27:00
 */
public interface Skewness {


    /**
     * 求银行交易的偏度
     * 参数列表：银行，银行每笔交易三次资产和、三次承诺随机数的和、账本
     */
    static BigDecimal calaulateSkewness(HashMap<String, HashMap<String, BankAudit>> allTransactionMap, String str, BigInteger bankTripleAssetsSum,
                                        BigInteger bankTripleComRandomSum, BigInteger bankDoubleAssetsSum, BigDecimal transactionAverage,
                                        BigInteger transactionTime, BigDecimal transactionVariance, BigInteger[] arr) {
        BigDecimal skewness = new BigDecimal(0.0);

        /**
         * 从账本获取三次资产的和
         */
        BigInteger tCHSum = tripleComHomomorphismSum(allTransactionMap, str, arr);
        if (tCHSum.equals((arr[2].modPow(bankTripleAssetsSum, arr[0])).multiply(arr[3].modPow(bankTripleComRandomSum, arr[0])).mod(arr[0]))){
            /**
             * 三次资产的值写进文件
             */
//            FileWriter file = new FileWriter(str + "TripleSum.txt");
//            file.write(bankTripleAssetsSum + "\n");
//            file.close();
            System.out.println("三次资产和审计通过！");
        } else {
            System.out.println("三次承诺验证不通过！");
            return null;
        }


        /**
         * 最重要的一步，验证通过之后，需要将三次资产和转换成BigDecimal类型
         */
        BigDecimal bankTripleAssetsSum1 = transferBigIntegertoBigDecimal(bankTripleAssetsSum);
        BigDecimal bTT = transferBigIntegertoBigDecimal(transactionTime);
        BigDecimal bDAS = transferBigIntegertoBigDecimal(bankDoubleAssetsSum);

        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(10);

        if ((transactionTime.intValue()) == (0)){
            return BigDecimal.valueOf(0.0);
        }

        /**
         * 计算偏度
         */
        //分子
//        BigDecimal molecular = ((bankTripleAssetsSum1.divide(bTT, 8, BigDecimal.ROUND_HALF_UP)).add(bTAV.pow(3).multiply(BigDecimal.valueOf(2)))
//                .subtract((bankDoubleAssetsSum.multiply(bTAV).multiply(BigDecimal.valueOf(3))).divide(bTT, 8, BigDecimal.ROUND_HALF_UP))).
//                setScale(8, BigDecimal.ROUND_HALF_UP);
        BigDecimal molecular = ((bankTripleAssetsSum1.divide(bTT, 8, BigDecimal.ROUND_HALF_UP)).add(transactionAverage.pow(3).multiply(BigDecimal.valueOf(2)))
                .subtract((bDAS.multiply(transactionAverage).multiply(BigDecimal.valueOf(3))).divide(bTT, 8, BigDecimal.ROUND_HALF_UP))).
                setScale(8, BigDecimal.ROUND_HALF_UP);
        //分母 方差的二分之三次方
        /**
         * 这个地方不好计算，因为API给的方法只是pow里边的参数是int，而不是double。
         */
//        BigDecimal denominator = bVariance.pow((1.5));
        /**
         * 退而求其次，我们银行的转账一般不会超过2的64次方，所以我们先把方差转换为double类型，然后利用Math的pow方法计算3/2次方。
         */
//
//
//        System.out.println(nf.format(molecular));       //输出的结果 0
//        System.out.println(molecular);                  //输出的结果 0E-24
        double bVariance1 = transactionVariance.doubleValue();
//        System.out.println(bVariance1);
//        System.out.println("----------------");
        double denominator = Math.pow(bVariance1, 1.5);
//        System.out.println(denominator);


        /**
         * 后面假如我们一直使用double的话，分子除以分母的话，是直接取整，没有余数，这样的话，精确度太差，所以我们再次把double类型
         * 的分母转换成BigDecimal类型.
         */
        BigDecimal denominator1 = BigDecimal.valueOf(denominator);  //这样，分母就是BigDecimal
//        System.out.println(denominator1);
        if ((denominator1.intValue()) == (0)){
            return BigDecimal.valueOf(0.0);
        }

//
//
//        /**
//         * 现在分子分母都是BigDecimal类型，所以直接进行除法运算
//         */
        skewness = molecular.divide(denominator1, 8, BigDecimal.ROUND_HALF_UP);
//        System.out.println(skewness);
        String str1 = nf.format(skewness);

        /**
         * 把偏度写入文件
         */
//        FileWriter file5 = new FileWriter(str+"Skewness.txt");  //不加true就是覆盖之前写的数据
//
//        file5.write(str1 + "\n");
//        file5.close();
        return skewness;
    }
}
