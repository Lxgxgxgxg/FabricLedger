package com.hyperledger.connection;

import org.hyperledger.fabric.gateway.Contract;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Query.java
 * @Description TODO
 * @createTime 2021年11月12日 17:43:00
 */
public interface Query {

    /**
     * 这个函数是往区块链网络上提交信息的函数，因为审计员没有要提交的东西，所以此函数不写
     */

    static ArrayList<String> queryOneTransaction(String str, Contract contract){
        //查询现有资产
        //注意更换调用链码的具体函数
        byte[] queryAllAssets = new byte[0];
        ArrayList<String> list = new ArrayList<>();
        try {
            queryAllAssets = contract.evaluateTransaction("get", str);
            String result = new String(queryAllAssets, StandardCharsets.UTF_8);
            result = result.replaceAll("\\\\", "");
            list.add(result);
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }



    static ArrayList<String> queryAllTransaction(int str1, int str2,  Contract contract){
        ArrayList<String> list = new ArrayList<>();

        for (int i = str1; i <= str2; i++) {
            String temp = test(String.valueOf(i), contract);
//            map.put(String.valueOf(i), temp);
            list.add(temp);
        }

        return list;
    }


    static String test(String str, Contract contract){
        //查询现有资产
        //注意更换调用链码的具体函数
        byte[] queryAllAssets = new byte[0];
        try {
            queryAllAssets = contract.evaluateTransaction("get", str);
            String result = new String(queryAllAssets, StandardCharsets.UTF_8);
            result = result.replaceAll("\\\\", "");
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
