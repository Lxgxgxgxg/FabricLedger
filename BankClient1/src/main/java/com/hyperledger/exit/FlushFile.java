package com.hyperledger.exit;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName FlushFile.java
 * @Description TODO
 * @createTime 2021年11月08日 15:42:00
 */

import java.io.FileWriter;
import java.io.IOException;

/**
 * 退出程序的时候，对文件中的保存的记录清零
 */
public interface FlushFile {

    static void flushFile(String str){
        try {
            FileWriter file = new FileWriter(str);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
