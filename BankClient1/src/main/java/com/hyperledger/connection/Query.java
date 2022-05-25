package com.hyperledger.connection;

import org.hyperledger.fabric.gateway.Contract;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Query.java
 * @Description TODO
 * @createTime 2021年11月15日 16:15:00
 */
public class Query {

    public static void query(ArrayList<String> oneTransactionInformation, String str, Contract contract){
        //查询现有资产
        //注意更换调用链码的具体函数
        byte[] queryAllAssets = new byte[0];
        try {
            queryAllAssets = contract.evaluateTransaction("get",str);
//            System.out.println("所有资产："+new String(queryAllAssets, StandardCharsets.UTF_8));
            String result = new String(queryAllAssets, StandardCharsets.UTF_8);
            result = result.replaceAll("\\\\","");
            FileWriter file = new FileWriter("oneTransaction.txt");
            file.write(result+"\n");
            file.close();
            System.out.println("交易信息已成功获取！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
