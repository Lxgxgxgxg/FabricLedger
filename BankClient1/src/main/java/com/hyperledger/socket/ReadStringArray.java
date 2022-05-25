package com.hyperledger.socket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName ReadStringArray.java
 * @Description TODO
 * @createTime 2021年11月13日 19:44:00
 */
public class ReadStringArray {

    /**
     * 把文件中的多行信息读取，保存在string[]数组中，然后返回
     */
    static String[] ReadStrArray(String str){
        FileReader file = null;
        LinkedList<String> list = new LinkedList<>();
        String temp = null;
        try {
            file = new FileReader(str);
            BufferedReader br = new BufferedReader(file);
            while ((temp = br.readLine()) != null){
                list.add(temp);
            }
            br.close();
            file.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        String[] res = new String[list.size()];
        return list.toArray(res);
    }

}
