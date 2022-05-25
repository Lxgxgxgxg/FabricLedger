package com.hyperledger.test;

import com.hyperledger.audit.AuditMainFunction;

import java.util.Scanner;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Test.java
 * @Description TODO
 * @createTime 2021年11月10日 20:08:00
 */
public class Test {

    public static void main(String[] args) {
        /**
         * 进入审计员的程序
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("进入审计员程序");
        System.out.println("============================");
        while (true){
            System.out.println("1.auditor");
            System.out.println("2.break");

            String selection = scanner.nextLine();
            if (selection.equals("1")){
                Selection.auditorMainFunction();
            }else if (selection.equals("2")){
                break;
            }else {
                System.out.println("请输入正确的选项参数！");
            }
        }
    }
}
