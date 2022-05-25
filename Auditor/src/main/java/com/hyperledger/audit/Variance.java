package com.hyperledger.audit;

import com.hyperledger.bank.BankAudit;
import com.hyperledger.bank.BankAuditResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import static com.hyperledger.audit.BigIntegerToBigDecimal.transferBigIntegertoBigDecimal;
import static com.hyperledger.audit.CommitHomomorphism.doubleComHomomorphismSum;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Variance.java
 * @Description TODO
 * @createTime 2021年11月11日 21:05:00
 */
public interface Variance {

    /**
     * 求银行交易的方差，就是银行每笔的交易金额减去平均值，然后再求差的平方，再求平方和，在除去交易数
     * 参数列表：银行、银行二次资产和、二次承诺随机数和、账本
     */
    static BigDecimal calculateVariance(HashMap<String, HashMap<String, BankAudit>> allTransactionMap, String str, BigInteger bankDoubleAssetsSum,
                                        BigInteger bankDoubleComRandomSum, BigDecimal transactionAverage, BigInteger transactionTime,
                                        BankAuditResult bankAuditResult, BigInteger[] arr) {
        BigDecimal variance = new BigDecimal(0.0);
        //从账本获取二次承诺的和
        BigInteger dCHSum = doubleComHomomorphismSum(allTransactionMap, str, arr);
        //验证
        if (dCHSum.equals((arr[2].modPow(bankDoubleAssetsSum, arr[0])).multiply(arr[3].modPow(bankDoubleComRandomSum, arr[0])).mod(arr[0]))){
//            /**
//             * 把二次资产和写进文件
//             */
//            FileWriter file1 = new FileWriter(str+"DoubleSum.txt");  //不加true就是覆盖之前写的的系统和
//            file1.write(bankDoubleAssetsSum + "\n");
//            file1.close();
            System.out.println("二次资产和验证通过！");
        } else {
            System.out.println("二次承诺验证不通过！");
            return null;
        }


        /**
         * 此时还需要两个值才能计算方差
         *  1、参与交易的次数
         *  2、平均值
         *  我们从文件读取
         */
        /**
         * 读取平均值
         */
//        FileReader file2 = new FileReader(str + "TransactionAverageValue.txt");
//        BufferedReader br = new BufferedReader(file2);
//        String mInput = br.readLine();
//        BigDecimal bTAV = null;
//
//        try {
//            bTAV = new BigDecimal(mInput);
//        } catch (NumberFormatException e){
//            System.out.println("读取该银行平均值错误！");
//        }
//        br.close();
//        file2.close();


        /**
         * 读取交易次数
         */
//        FileReader file3 = new FileReader(str+"TransactionTime.txt");
//        BufferedReader br1 = new BufferedReader(file3);
//        String timeInput = br1.readLine();
//        BigDecimal bTT = null;
//        try{
//            bTT = new BigDecimal(timeInput);
////            bTT = new BigDecimal(timeInput).subtract(BigDecimal.valueOf(1));
//        } catch (NullPointerException e) {
//            System.out.println("读取银行参与交易次数错误！");
//        }
//
//        System.out.println(bTT);
//        br1.close();
//        file3.close();

        /**
         * 把银行给来的二次资产和转换成BigDecimal类型,次数也转了
         */
        BigDecimal bankDoubleAssetsSum1 = transferBigIntegertoBigDecimal(bankDoubleAssetsSum);
        /**
         * 计算方差,并写入文件
         */
        //分子
//        variance = ((bankDoubleAssetsSum1.divide(bTT, 8, BigDecimal.ROUND_HALF_UP)).subtract(bTAV.pow(2))).
//                setScale(8, BigDecimal.ROUND_HALF_UP);

        if ((transactionTime.intValue()) == (0)){
            variance = BigDecimal.valueOf(0.0);
        }else {
            variance = ((bankDoubleAssetsSum1.divide(new BigDecimal(transactionTime), 8, BigDecimal.ROUND_HALF_UP)).subtract(transactionAverage.pow(2))).
                    setScale(8, BigDecimal.ROUND_HALF_UP);
        }

//        FileWriter file4 = new FileWriter(str+"Variance.txt");  //不加true就是覆盖之前写的文件
//        file4.write(variance + "\n");
//        file4.close();


        return variance;
    }
}
