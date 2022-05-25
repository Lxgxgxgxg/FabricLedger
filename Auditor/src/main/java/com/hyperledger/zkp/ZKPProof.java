package com.hyperledger.zkp;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName ZKPProof.java
 * @Description TODO
 * @createTime 2021年11月12日 20:33:00
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyperledger.bank.Bank;
import com.hyperledger.bank.BankAudit;
import com.hyperledger.bank.BankBalanceAndBitZKP;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 这里主要是审计员对从链上获取到的所有的交易信息进行零知识证明，然后保存到相应审计计算的结果集中去
 */
public class ZKPProof {

    //零知识证明的主函数，并从链上拉取json字符串中获取每一个字段,这里传进来的参数是一个hashMap，里边保存的就是链
    // 上获取到的交易信息

    public static void ZKPMain(ArrayList<String> getAllTransactionList, HashMap<String, HashMap<String, BankAudit>> allTransactionMap,
                               String[] banks, BigInteger[] arr){
        
        int len = getAllTransactionList.size();

        Boolean[] zkpResult = new Boolean[len];
        for (int i = 0; i < len; i++) {
            zkpResult[i] = ReadFileAndZKP(banks, getAllTransactionList.get(i), allTransactionMap, arr);
        }

        /**
         * 验证零知识证明的结果
         */
        for (int i = 0; i < len; i++) {
            if (!zkpResult[i]){
                System.out.println("第"+ i + "条资产交易零知识证明不通过！");
            }
        }
        System.out.println("资产交易零知识证明通过！");
    }

    public static boolean ReadFileAndZKP(String[] banks, String getOneTransactionString, HashMap<String, HashMap<String, BankAudit>> allTransactionMap,
                                         BigInteger[] arr) {

        /**
         * 现在的是从链上获取到的结果集就保存在getAllTransactionList中，现在我们需要去零知识证明这些交易信息，通过之后保存到allTransactionMap
         */
        String keyTime = getOneTransactionString.substring(9, 28);
        String str1 = getOneTransactionString.replaceFirst("\\{\"time\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\",", "{");

        //银行的个数
        int n = banks.length;

        JSONObject[] bankJson = new JSONObject[n];
        Bank[] bank = new Bank[n];
        for (int i = 0; i < n; i++) {
            bankJson[i] = JSON.parseObject(str1).getJSONObject(banks[i]);
            bank[i] = JSON.parseObject(String.valueOf(bankJson[i]), Bank.class);
        }
        JSONObject zkpFieldjson = JSON.parseObject(str1).getJSONObject("balProofParam");
        BankBalanceAndBitZKP zkpField = JSON.parseObject(String.valueOf(zkpFieldjson), BankBalanceAndBitZKP.class);

        //首先判断那个银行是支出行，就是依靠isFrom == 1
        Bank bankFrom = null;
        for (int i = 0; i < n; i++) {
            if (bank[i].getisFrom() == 1){
                bankFrom = bank[i];
                break;
            }
        }

        /**
         * 资产平衡证明，即转出的金额等于转入放入金额
         */
        BigInteger P = null;
        if (bankFrom != null) {
            P = bankFrom.getCom();
        }else {
            return false;
        }
        BigInteger Q = BigInteger.valueOf(1);
        for (int i = 0; i < n; i++) {
            if (!bank[i].equals(bankFrom)){
                Q = Q.multiply(bank[i].getCom()).mod(arr[0]);
            }
        }
        BigInteger c1 = zkpField.getC1();
        BigInteger d = zkpField.getD();
        BigInteger d1 = zkpField.getD1();
        BigInteger d2 = zkpField.getD2();
        boolean result1 = BalanceProof.EqualityOfMessageInPedersenCoomiment(P, Q, c1, d, d1, d2, arr);

        /**
         * 证明比特承诺的隐藏值前后相等
         */
        BigInteger E = bankFrom.getBitCom();
        BigInteger F = BigInteger.valueOf(1);
        for (int i = 0; i < n; i++) {
            if (!bank[i].equals(bankFrom)){
                F = F.multiply(bank[i].getBitCom()).mod(arr[0]);
            }
        }
        BigInteger c2 = zkpField.getC2();
        BigInteger m = zkpField.getM();
        BigInteger m1 = zkpField.getM1();
        BigInteger m2 = zkpField.getM2();
        boolean result2 = BalanceProof.EqualityOfMessageInPedersenCoomiment(E, F, c2, m, m1, m2, arr);

        /**
         * bank中的bitCom承诺中的隐藏值非0即1证明
         */
        boolean[] bankBitResult = new boolean[n];
        BigInteger[] bankBitCom = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            bankBitCom[i] = bank[i].getBitCom();
            BigInteger bankC1 = bank[i].getBitComTemp1();
            BigInteger bankC2 = bank[i].getBitComTemp2();
            BigInteger bankX = bank[i].getChallenge();
            BigInteger hash_bankX = bank[i].getHash_challenge();
            BigInteger bankF = bank[i].getBitComResponse1();
            BigInteger bankZ1 = bank[i].getBitComResponse2();
            BigInteger bankZ2 = bank[i].getBitComResponse3();
            bankBitResult[i] = If0Else1Proof.bit01Proof(bankBitCom[i], bankC1, bankC2, bankX, hash_bankX, bankF, bankZ1, bankZ2, arr);
        }

        /**
         * 证明所有bitCom乘积的隐藏值为2
         */
        BigInteger bankNum = zkpField.getTwoObject();
        BigInteger r2Sum = zkpField.getAllR2_Sum();
        boolean twoResult = TwoObjectProof.twoObjectCompare(bankBitCom, bankNum, r2Sum, arr);

        /**
         * 证明g的s次方
         */
        boolean[] bank_g_s_result = new boolean[n];
        for (int i = 0; i < n; i++) {
            BigInteger bankTemp1 = bank[i].getTemp1();
            BigInteger bankTempC = bank[i].getTemp1C();
            BigInteger bankTempS = bank[i].getTemp1S();
            bank_g_s_result[i] = BalanceProof.SigmaProtocols(bankTemp1, bankTempC, bankTempS, arr);
        }

        /**
         * 验证comarr[17]是comarr[0]的1/0+s次方
         */
        boolean[] bank_com_1or0_s_result = new boolean[n];
        BigInteger[] bankCom = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            bankCom[i] = bank[i].getCom();
            BigInteger bankComTemp1 = bank[i].getComTemp1();
            BigInteger bankComTemp1C = bank[i].getComTemp1C();
            BigInteger bankComTemp1S = bank[i].getComTemp1S();
            bank_com_1or0_s_result[i] = BalanceProof.com1Or0AddsProtocols(bankCom[i], bankComTemp1, bankComTemp1C, bankComTemp1S, arr);
        }

        /**
         * 指数相等证明
         */
        boolean[] indexResult = new boolean[n];
        for (int i = 0; i < n; i++) {
            BigInteger bankCom2 = bank[i].getBitComMulTemp1();
            BigInteger bankCom3 = bank[i].getComTemp1();
            BigInteger bankT = bank[i].getIndexEqualTemp1();
            BigInteger bankS1 = bank[i].getIndexEqualTemp2();
            BigInteger bankS2 = bank[i].getIndexEqualTemp3();
            BigInteger bankS3 = bank[i].getIndexEqualTemp4();
            indexResult[i] = IndexEqualsProof.indexEqualsProof(bankCom[i], bankCom2, bankCom3, bankT, bankS1, bankS2, bankS3, arr);
        }

        /**
         * 证明资产只转给一个人，即bitCom乘g的s次方，并把com相应的1+s或者0+s次方，证明支出的承诺隐藏值和其他银行承诺乘积的隐藏值相等
         */
        BigInteger bankComTemp = bankFrom.getComTemp1();
        BigInteger otherBankComTemp = BigInteger.valueOf(1);
        for (int i = 0; i < n; i++) {
            if (!bank[i].equals(bankFrom)){
                otherBankComTemp = otherBankComTemp.multiply(bank[i].getComTemp1()).mod(arr[0]);
            }
        }
        BigInteger h2 = zkpField.getH2();
//        BigInteger n = zkpField.getN();
        BigInteger n_x = zkpField.getN();
        BigInteger n1 = zkpField.getN1();
        BigInteger n2 = zkpField.getN2();
        boolean assetsToOneBank = BalanceProof.EqualityOfMessageInPedersenCoomiment(bankComTemp, otherBankComTemp, h2, n_x, n1, n2, arr);

        /**
         * 下面的代码证明doubleCom的隐藏值是com隐藏值的平方
         */
        boolean[] bankDoubleIndexProofResult = new boolean[n];
        BigInteger[] bankComSIndex = new BigInteger[n];
        BigInteger[] bankDoubleCom = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            bankDoubleCom[i] = bank[i].getDoubleCom();
            BigInteger bankComSAddVIndex = bank[i].getComSAddVIndex();
            bankComSIndex[i] = bank[i].getComSIndex();
            BigInteger bankHBaseIndex = bank[i].gethBaseIndex();
            BigInteger bankHash_h = bank[i].getHash_h();
            BigInteger bank1H_Response = bank[i].getH_Response();
            bankDoubleIndexProofResult[i] = generateComIndexProof.generateComIndexProof(bankDoubleCom[i], bankComSAddVIndex, bankComSIndex[i], bankHBaseIndex,
                    bankHash_h, bank1H_Response, arr);
        }

        /**
         * 下面的代码证明tripleCom的隐藏值是com隐藏值的立方
         */
        boolean[] bankTripleIndexProofResult = new boolean[n];
        BigInteger[] bankTripleCom = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            bankTripleCom[i] = bank[i].getTripleCom();
            BigInteger bankComSAddVSquareIndex = bank[i].getComSAddVSquareIndex();

            BigInteger bankHBaseIndex1 = bank[i].gethBaseIndex1();
            BigInteger bankHash_h1 = bank[i].getHash_h1();
            BigInteger bankH_Response1 = bank[i].getH_Response1();

            bankTripleIndexProofResult[i] = generateComIndexProof.generateComIndexProof(bankTripleCom[i], bankComSAddVSquareIndex, bankComSIndex[i],
                    bankHBaseIndex1, bankHash_h1, bankH_Response1, arr);
        }

        /**
         * 下面的代码证明quadraCom的隐藏值是com隐藏值的四次方
         */
        boolean[] bankQuadraIndexProofResult = new boolean[n];
        BigInteger[] bankQuadraCom = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            bankQuadraCom[i] = bank[i].getQuadraCom();
            BigInteger bankComSAddVTripleIndex = bank[i].getComSAddVTripleIndex();
            BigInteger bankHBaseIndex2 = bank[i].gethBaseIndex2();
            BigInteger bankHash_h2 = bank[i].getHash_h2();
            BigInteger bankH_Response2 = bank[i].getH_Response2();
            bankQuadraIndexProofResult[i] = generateComIndexProof.generateComIndexProof(bankQuadraCom[i], bankComSAddVTripleIndex, bankComSIndex[i],
                    bankHBaseIndex2, bankHash_h2, bankH_Response2, arr);
        }

        /**
         * 下面开始v的四次方的证明
         */
        boolean[] bankTResult = new boolean[n];
        boolean[] bankPResult = new boolean[n];
        boolean[] bankInnerProductResult = new boolean[n];
        for (int i = 0; i < n; i++) {
            BigInteger bankA = bank[i].getA();
            BigInteger bankS = bank[i].getS();
            BigInteger bankYHash = bank[i].getyHash();
            BigInteger bankZHash = bank[i].getzHash();
            BigInteger bankXHash = bank[i].getxHash();
            BigInteger bankT1 = bank[i].getT1();
            BigInteger bankT2 = bank[i].getT2();
            String bankStrLx = bank[i].getLx().substring(0, bank[i].getLx().length()-1);
            String[] newBankStrLx = bankStrLx.split(",");
            BigInteger[] BankLx = new BigInteger[newBankStrLx.length];
            for (int j = 0;j<newBankStrLx.length;j++){
                BankLx[j] = new BigInteger(newBankStrLx[j]);
            }
            String bankStrRx = bank[i].getRx().substring(0, bank[i].getRx().length()-1);
            String[] newBankStrRx = bankStrRx.split(",");
            BigInteger[] BankRx = new BigInteger[newBankStrRx.length];
            for (int j = 0;j<newBankStrRx.length;j++){
                BankRx[j] = new BigInteger(newBankStrRx[j]);
            }
            BigInteger bankT = bank[i].getT();
            BigInteger bankTaoX = bank[i].getTaox();
            BigInteger bankMiu = bank[i].getMiu();
            BigInteger bankDeerTa = GetDeerTa.getDeerTa(bankYHash, bankZHash, BankLx, arr);
            BigInteger[] G = ReadVectorFile.readFile("gVector.txt");
            BigInteger[] H = ReadVectorFile.readFile("hVector.txt");
            BigInteger[] newBankH = HVectorConvert.hVectorConvert(H, bankYHash, arr);

            bankTResult[i] = RangeProof.isTresult(bankZHash, bankXHash, bankT, bankQuadraCom[i], bankTaoX, bankT1, bankT2, bankDeerTa, arr);
//        System.out.println("BANK1="+bank1TResult);
            bankPResult[i] = RangeProof.verifierP(bankA, bankS, G, newBankH, BankLx, BankRx, bankYHash, bankZHash, bankXHash, bankMiu, arr);
//        System.out.println("BANK1="+bank1PResult);
            bankInnerProductResult[i] = RangeProof.verLx_Rx_T(BankLx, BankRx, bankT, arr);
        }


        boolean[] result = new boolean[n];

        for (int i = 0; i < n; i++) {
            result[i] = bankBitResult[i] && bank_g_s_result[i] && bank_com_1or0_s_result[i] && indexResult[i] && bankDoubleIndexProofResult[i]
                    && bankTripleIndexProofResult[i] && bankQuadraIndexProofResult[i] && bankTResult[i] && bankPResult[i] && bankInnerProductResult[i];
        }

        /**
         * 验证不通过直接返回，不把这条交易添加进保存审计字段的Map
         */
        for (int i = 0; i < n; i++) {
            if (!result[i]){
                return false;
            }
        }

        if (!(result1 || result2 || twoResult || assetsToOneBank)){
            return false;
        }

        /**
         * 下面把验证通过之后的交易信息写进allTransactionMap中，只需要写进需要审计的字段，也就是bank类中的前六个字段。
         * 其中key就是交易的额时间
         * value就是用于审计的六个字段
         */

        //新建hashMap保存每一个银行的审计字段
        HashMap<String, BankAudit> bankAuditHashMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            BankAudit bankAudit = new BankAudit();
            bankAudit.setCom(bank[i].getCom());
            bankAudit.setBitCom(bank[i].getBitCom());
            bankAudit.setIsFrom(bank[i].getisFrom());
            bankAudit.setDoubleCom(bank[i].getDoubleCom());
            bankAudit.setTripleCom(bank[i].getTripleCom());
            bankAudit.setQuadraCom(bank[i].getQuadraCom());

            bankAuditHashMap.put(banks[i], bankAudit);
        }

        allTransactionMap.put(keyTime, bankAuditHashMap);

        return true;
    }
}
