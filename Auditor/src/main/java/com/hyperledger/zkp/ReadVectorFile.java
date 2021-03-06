package com.hyperledger.zkp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName ReadVectorFile.java
 * @Description TODO
 * @createTime 2021年11月05日 17:29:00
 */
public interface ReadVectorFile {

    /**
     * 读文件的函数
     */
    static BigInteger[] readFile(String str){
        FileReader file = null;
        BigInteger[] g = new BigInteger[94];
        String str1 = null;
        try {
            file = new FileReader(str);
            BufferedReader br = new BufferedReader(file);
            int i = 0;
            while((str1 = br.readLine()) != null){
                g[i] = new BigInteger(str1);
                i++;
            }
            br.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return g;
    }
}
