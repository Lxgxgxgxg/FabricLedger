package com.hyperledger.commit.getRangeProof;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName GetAr.java
 * @Description TODO
 * @createTime 2021年11月05日 17:32:00
 */
public interface GetAr {

    /**
     * 得到Ar
     */
    static BigInteger[] getAr(BigInteger[] al, BigInteger[] arr){
        BigInteger[] Ar = new BigInteger[al.length];
        for(int i = 0; i< al.length;i++){
//            if (al[i].compareTo(BigInteger.valueOf(1)) == 0){
//                Ar[i] = BigInteger.valueOf(0);
//            }else {
//                Ar[i] = arr[1].subtract(BigInteger.valueOf(1));
//            }
            Ar[i] = (al[i].subtract(BigInteger.valueOf(1))).mod(arr[1]);
        }
        return Ar;
    }
}
