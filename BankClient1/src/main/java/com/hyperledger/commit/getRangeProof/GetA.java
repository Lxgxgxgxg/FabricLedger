package com.hyperledger.commit.getRangeProof;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName GetA.java
 * @Description TODO
 * @createTime 2021年11月05日 17:32:00
 */
public interface GetA {


    /**
     *得到A
     */
    static BigInteger getA(BigInteger[] g, BigInteger[] h, BigInteger[] al, BigInteger[] ar, BigInteger Alpha, BigInteger[] arr){
        BigInteger A = new BigInteger("1");
        for (int i = 0; i < al.length; i++){
            A = A.multiply(g[i].modPow(al[i], arr[0])).multiply(h[i].modPow(ar[i], arr[0])).mod(arr[0]);
        }
        A = A.multiply(arr[3].modPow(Alpha, arr[0])).mod(arr[0]);
        return A;
    }
}
