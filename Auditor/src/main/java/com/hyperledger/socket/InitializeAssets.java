package com.hyperledger.socket;

import com.hyperledger.bank.BankAudit;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName InitializeAssets.java
 * @Description TODO
 * @createTime 2021年11月10日 21:42:00
 */
public class InitializeAssets {

    public static void initializeAssets(HashMap<String, HashMap<String, BankAudit>> allTransactionsMap, HashMap<String, String> initializeHashMap,
                                        BigInteger[] initializeAssetsRandom, String[] banks, String[] IP, Integer[] portNum, BigInteger[] arr){

        /**
         * 审计员与银行进行交互，进行资产的初始化
         */
        for (int i = 0; i < banks.length; i++) {
            Client.getInitializeAssets(initializeHashMap, "3", banks[i], IP[i], portNum[i]);
        }
        /*
            现在调试的时候先不初始化，这样很麻烦，我们直接给定每一个银行的金额为1000
         */
//        initializeHashMap.put("bank1", "1000");
//        initializeHashMap.put("bank2", "1000");
//        initializeHashMap.put("bank3", "1000");
//        initializeHashMap.put("bank4", "1000");


        /**
         * 承诺化
         */
        CommitInitializeAssets.commitInitializeAssets(allTransactionsMap, initializeHashMap, initializeAssetsRandom, arr);
        System.out.println("银行资产初始化完成!");
    }
}
