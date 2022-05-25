package com.hyperledger.bank;

import java.math.BigInteger;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName RandomClass.java
 * @Description TODO
 * @createTime 2021年11月04日 20:53:00
 */
public class RandomClass {

    /**
     * 致盲因子类
     */

    private BigInteger r1;
    private BigInteger r2;
    private BigInteger x;
    private BigInteger y;
    private BigInteger z;

    public RandomClass() {
    }

    public RandomClass(BigInteger r1, BigInteger r2, BigInteger x, BigInteger y, BigInteger z) {
        this.r1 = r1;
        this.r2 = r2;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BigInteger getR1() {
        return r1;
    }

    public void setR1(BigInteger r1) {
        this.r1 = r1;
    }

    public BigInteger getR2() {
        return r2;
    }

    public void setR2(BigInteger r2) {
        this.r2 = r2;
    }

    public BigInteger getX() {
        return x;
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    public BigInteger getZ() {
        return z;
    }

    public void setZ(BigInteger z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Random{" +
                "r1=" + r1 +
                ", r2=" + r2 +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
