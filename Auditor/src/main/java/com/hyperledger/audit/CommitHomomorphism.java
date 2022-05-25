package com.hyperledger.audit;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName CommitHomomorphism.java
 * @Description TODO
 * @createTime 2021年11月11日 17:27:00
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyperledger.bank.Bank;
import com.hyperledger.bank.BankAudit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 同态计算，把每一比交易的值进行计算
 */
public interface CommitHomomorphism {

    //利用Pedersen承诺的同态性，计算账本上承诺的和，返回值应该是一个大整数

    /**
     *
     * @param allTransactionMap key为时间，里边的hashMap的key为bankID, value为BankAudit对象
     * @param str   银行名
     * @param arr
     * @return
     * @throws FileNotFoundException
     */
    static BigInteger comHomomorphismSum(HashMap<String, HashMap<String, BankAudit>> allTransactionMap, String str, BigInteger[] arr) {
        BigInteger cHSum = new BigInteger("1");


        /**
         * 先从交易文件中读取每一行交易，然后在计算
         */
        /*
        FileReader file1 = new FileReader("allTransactions.txt");
        BufferedReader br = new BufferedReader(file1);
        String tempInput = null;
        //循环读取每一行
        try {
            while ((tempInput = br.readLine()) != null) {
                //读进来的字符串转换去掉前面的时间字段
                String str1 = tempInput.replaceFirst("\\{\"time\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\",", "{");
                JSONObject jsonObject = JSON.parseObject(str1);
                JSONObject bankJson = jsonObject.getJSONObject(str);
//                System.out.println(bankJson);
                BankAudit bank = JSON.parseObject(String.valueOf(bankJson), BankAudit.class);
//                System.out.println(bank);
                if (bank.getIsFrom() == 0) {
                    cHSum = cHSum.multiply(bank.getCom()).mod(arr[0]);
                } else {
                    BigInteger newx = bank.getCom().modPow(BigInteger.valueOf(-1), arr[0]);
                    cHSum = cHSum.multiply(newx).mod(arr[0]);
                }
            }
            br.close();
            file1.close();
        } catch (IOException e) {
            System.out.println("读取失败！");
        }
        */

        /**
         * 首先得到所有的结果集
         */
        Collection<HashMap<String, BankAudit>> list = allTransactionMap.values();
        for (HashMap<String, BankAudit> bankAuditHashMap : list) {
            //这里是一个hashMap,遍历
            Set<Map.Entry<String, BankAudit>> entrySet = bankAuditHashMap.entrySet();
            for (Map.Entry<String, BankAudit> stringBankAuditEntry : entrySet) {
                String key = stringBankAuditEntry.getKey();
                if (key.equals(str)){
                    BankAudit bankAudit = stringBankAuditEntry.getValue();
                    //非支出行
                    if (bankAudit.getIsFrom() == 0){
                        cHSum = cHSum.multiply(bankAudit.getCom()).mod(arr[0]);
                    }else {
                        //支出行
                        BigInteger newX = bankAudit.getCom().modPow(BigInteger.valueOf(-1), arr[0]);
                        cHSum = cHSum.multiply(newX).mod(arr[0]);
                    }
                }
            }

        }

        return cHSum;
    }


    //计算比特承诺的和
    static BigInteger bitComHomomorphismSum(HashMap<String, HashMap<String, BankAudit>> allTransactionMap, String str, BigInteger[] arr) {
        BigInteger bCHSum = new BigInteger("1");

        /**
         * 先从交易文件中读取每一行交易，然后在计算
         */
        /**
        FileReader file1 = new FileReader("allTransactions.txt");
        BufferedReader br = new BufferedReader(file1);
        String tempInput = null;
        //循环读取每一行
        try {
            while ((tempInput = br.readLine()) != null) {
                //读进来的字符串转换去掉前面的时间字段
                String str1 = tempInput.replaceFirst("\\{\"time\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\",", "{");
                JSONObject bankJson = JSON.parseObject(str1).getJSONObject(str);
                BankAudit bank = JSON.parseObject(String.valueOf(bankJson), BankAudit.class);
                bCHSum = bCHSum.multiply(bank.getBitCom()).mod(arr[0]);
            }
            br.close();
            file1.close();
        } catch (IOException e) {
            System.out.println("读取失败！");
        }
         */

        /**
         * 首先得到所有的结果集
         */
        Collection<HashMap<String, BankAudit>> list = allTransactionMap.values();
        for (HashMap<String, BankAudit> bankAuditHashMap : list) {
            Set<Map.Entry<String, BankAudit>> entrySet = bankAuditHashMap.entrySet();
            for (Map.Entry<String, BankAudit> stringBankAuditEntry : entrySet) {
                String key = stringBankAuditEntry.getKey();
                if (key.equals(str)){
                    BankAudit bankAudit = stringBankAuditEntry.getValue();
                    bCHSum = bCHSum.multiply(bankAudit.getBitCom()).mod(arr[0]);
                }
            }
        }

        return bCHSum;
    }


    //计算隐藏值二次方的和
    static BigInteger doubleComHomomorphismSum(HashMap<String, HashMap<String, BankAudit>> allTransactionMap, String str, BigInteger[] arr) {
        BigInteger dCHSum = new BigInteger("1");

        /**
         * 先从交易文件中读取每一行交易，然后在计算
         */
        /**
        FileReader file1 = new FileReader("allTransactions.txt");
        BufferedReader br = new BufferedReader(file1);
        String tempInput = null;
        //循环读取每一行
        try {
            while ((tempInput = br.readLine()) != null) {
                //读进来的字符串转换去掉前面的时间字段
                String str1 = tempInput.replaceFirst("\\{\"time\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\",", "{");
                JSONObject bankJson = JSON.parseObject(str1).getJSONObject(str);
                BankAudit bank = JSON.parseObject(String.valueOf(bankJson), BankAudit.class);
                dCHSum = dCHSum.multiply(bank.getDoubleCom()).mod(arr[0]);
            }
            br.close();
            file1.close();
        } catch (IOException e) {
            System.out.println("读取失败！");
        }
*/

        /**
         * 首先得到所有的结果集
         */
        Collection<HashMap<String, BankAudit>> list = allTransactionMap.values();
        for (HashMap<String, BankAudit> bankAuditHashMap : list) {
            Set<Map.Entry<String, BankAudit>> entrySet = bankAuditHashMap.entrySet();
            for (Map.Entry<String, BankAudit> stringBankAuditEntry : entrySet) {
                String key = stringBankAuditEntry.getKey();
                if (key.equals(str)){
                    BankAudit bankAudit = stringBankAuditEntry.getValue();
                    dCHSum = dCHSum.multiply(bankAudit.getDoubleCom()).mod(arr[0]);
                }
            }
        }

        return dCHSum;
    }


    //计算隐藏值三次方的和
    static BigInteger tripleComHomomorphismSum(HashMap<String, HashMap<String, BankAudit>> allTransactionMap, String str, BigInteger[] arr) {
        BigInteger tCHSum = new BigInteger("1");

        /**
         * 先从交易文件中读取每一行交易，然后在计算
         */
        /**
        FileReader file1 = new FileReader("allTransactions.txt");
        BufferedReader br = new BufferedReader(file1);
        String tempInput = null;
        //循环读取每一行
        try {
            while ((tempInput = br.readLine()) != null) {
                //读进来的字符串转换去掉前面的时间字段
                String str1 = tempInput.replaceFirst("\\{\"time\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\",", "{");
                JSONObject bankJson = JSON.parseObject(str1).getJSONObject(str);
                BankAudit bank = JSON.parseObject(String.valueOf(bankJson), BankAudit.class);
                tCHSum = tCHSum.multiply(bank.getTripleCom()).mod(arr[0]);
            }
            br.close();
            file1.close();
        } catch (IOException e) {
            System.out.println("读取失败！");
        }
*/

        /**
         * 首先得到所有的结果集
         */
        Collection<HashMap<String, BankAudit>> list = allTransactionMap.values();
        for (HashMap<String, BankAudit> bankAuditHashMap : list) {
            Set<Map.Entry<String, BankAudit>> entrySet = bankAuditHashMap.entrySet();
            for (Map.Entry<String, BankAudit> stringBankAuditEntry : entrySet) {
                String key = stringBankAuditEntry.getKey();
                if (key.equals(str)){
                    BankAudit bankAudit = stringBankAuditEntry.getValue();
                    tCHSum = tCHSum.multiply(bankAudit.getTripleCom()).mod(arr[0]);
                }
            }
        }
        return tCHSum;
    }


    //计算隐藏值的四次方的和
    static BigInteger quadraComHomomorphismSum(HashMap<String, HashMap<String, BankAudit>> allTransactionMap, String str, BigInteger[] arr) {
        BigInteger qCHSum = new BigInteger("1");

        /**
         * 先从交易文件中读取每一行交易，然后在计算
         */
        /**
        FileReader file1 = new FileReader("allTransactions.txt");
        BufferedReader br = new BufferedReader(file1);
        String tempInput = null;
        //循环读取每一行
        try {
            while ((tempInput = br.readLine()) != null) {
                //读进来的字符串转换去掉前面的时间字段
                String str1 = tempInput.replaceFirst("\\{\"time\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\",", "{");
                JSONObject bankJson = JSON.parseObject(str1).getJSONObject(str);
                BankAudit bank = JSON.parseObject(String.valueOf(bankJson), BankAudit.class);
                qCHSum = qCHSum.multiply(bank.getQuadraCom()).mod(arr[0]);
            }
            br.close();
            file1.close();
        } catch (IOException e) {
            System.out.println("读取失败！");
        }
*/

        /**
         * 首先得到所有的结果集
         */
        Collection<HashMap<String, BankAudit>> list = allTransactionMap.values();
        for (HashMap<String, BankAudit> bankAuditHashMap : list) {
            Set<Map.Entry<String, BankAudit>> entrySet = bankAuditHashMap.entrySet();
            for (Map.Entry<String, BankAudit> stringBankAuditEntry : entrySet) {
                String key = stringBankAuditEntry.getKey();
                if (key.equals(str)){
                    BankAudit bankAudit = stringBankAuditEntry.getValue();
                    BigInteger temp = bankAudit.getQuadraCom();
//                    System.out.println(key + "四次承诺："+temp);
                    qCHSum = qCHSum.multiply(temp).mod(arr[0]);
                }
            }
        }

        return qCHSum;
    }
}
