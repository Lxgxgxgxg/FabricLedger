package com.hyperledger.ecc;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Curve.java
 * @Description TODO
 * @createTime 2021年11月04日 19:52:00
 */


import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * 基于自己生成的大整数的循环群
 */
public class Curve {

    static BigInteger p;
    static BigInteger q;
    static BigInteger g;
    static BigInteger h;

    public Curve() throws IOException {
        //产生pqg，q是阶数，p是2q+1的大素数
        SecureRandom random = new SecureRandom();
        while (true){
            q = BigInteger.probablePrime(40, random);
            p = q.multiply(BigInteger.valueOf(2));
            p = p.add(BigInteger.valueOf(1));
            if (p.isProbablePrime(1)){
                break;
            }
        }

        //g=x^2modp
        BigInteger x = new BigInteger(40, random);
        //大于 返回1，小于返回-1 确定x是2到 p-2
        while(x.compareTo(p.subtract(BigInteger.valueOf(2))) == 1 || x.compareTo(BigInteger.valueOf(2)) == -1){
            x = new BigInteger(40, random);
        }

        g = x.modPow(BigInteger.valueOf(2), p);

        BigInteger y = new BigInteger(40, random);

        while(y.compareTo(q.subtract(BigInteger.valueOf(2))) == 1 || y.compareTo(BigInteger.valueOf(2)) == -1){
            y = new BigInteger(40, random);
        }

        // generate h: h = g^y where y is any random number between 2 and q-2.
        h = g.modPow(y, p);
        //g 和y是该椭圆曲线的两个生成元


        //写入文件
        //写入文件
        FileWriter file = new FileWriter("ecc.txt");
        file.write(p +"\n" + q + "\n" + g + "\n" + h);
        file.close();
        // display
//        System.out.println("p: " + p);
//        System.out.println("q: " + q);
//        System.out.println("g: " + g);
//        System.out.println("h: " + h);
    }

}
