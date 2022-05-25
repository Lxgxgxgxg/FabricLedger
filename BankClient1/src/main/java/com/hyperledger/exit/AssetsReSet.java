package com.hyperledger.exit;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName AssetsReSet.java
 * @Description TODO
 * @createTime 2021年11月08日 15:43:00
 */
public interface AssetsReSet {

    /**
     * 资产重置
     */
    static void assetsReset(String str1, String str2){
        FileWriter file1 = null;
        try {
            file1 = new FileWriter(str1);
            file1.write(str2+"\n");
            file1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
