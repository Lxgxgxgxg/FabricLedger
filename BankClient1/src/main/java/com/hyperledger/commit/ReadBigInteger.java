package com.hyperledger.commit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName ReadBigInteger.java
 * @Description TODO
 * @createTime 2021年11月05日 17:18:00
 */
public interface ReadBigInteger {


    /**
     * 读取一个大数的函数
     */
    static BigInteger readBigInteger(String str){
        try {
            FileReader file = new FileReader(str);
            BufferedReader br = new BufferedReader(file);
            BigInteger num = new BigInteger(br.readLine());
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigInteger.valueOf(0);
    }
}
