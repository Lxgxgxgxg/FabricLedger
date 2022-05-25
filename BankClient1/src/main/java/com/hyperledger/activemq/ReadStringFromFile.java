package com.hyperledger.activemq;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName ReadStringFromFile.java
 * @Description TODO
 * @createTime 2021年11月09日 22:45:00
 */
public interface ReadStringFromFile {

    /**
     * 主要是从文件中读取字符串
     */
    static String readStringFromFile(String fileName){
        try {
            FileReader file = new FileReader(fileName);
            BufferedReader br = new BufferedReader(file);
            String str = br.readLine();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return BigInteger.valueOf(0);
        return null;
    }
}
