package com.hyperledger.zkp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloudant.client.api.query.Index;
import com.hyperledger.bank.Bank;
import com.hyperledger.bank.BankBalanceAndBitZKP;
import com.hyperledger.commit.ReadVectorFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName ZKPProof.java
 * @Description TODO
 * @createTime 2021年11月08日 15:46:00
 *
 * 该类主要是实现零知识证明的验证
 */
public class ZKPProof {

    //零知识证明的主函数，并从链上拉取json字符串中获取每一个字段

    public static boolean ReadFileAndZKP(String[] banks, LinkedList<String> assetsList, HashMap<String, String> allTransactionsMap,
                                         BigInteger[] arr) throws IOException {
        /**
         * 首先，审计员从链上拉取下来一条交易记录之后，此交易记录保存在LinkedList中文件中，然后审计员对这一条交易进行正确性检验，首先就是平衡证明，
         * 即从hashmap文件中读取第一行，然后进行验证。验证通过之后，把这条交易记录保存到allTransaction.txt文件和最终用于审计的hashmap中，以便后续的审计。
         */

//        System.out.println(assetsList);
        String tempInput = assetsList.get(0);
        String str1 = tempInput.replaceFirst("\\{\"time\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\",", "{");

//        FileReader file1 = new FileReader("oneTransaction.txt");
//        BufferedReader br = new BufferedReader(file1);
//        String tempInput = br.readLine();
//        String str1 = tempInput.replaceFirst("\\{\"time\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\",", "{");

        int n = banks.length;
//        JSONObject bankJson1 = JSON.parseObject(str1).getJSONObject("bank1");
//        JSONObject bankJson2 = JSON.parseObject(str1).getJSONObject("bank2");
//        JSONObject bankJson3 = JSON.parseObject(str1).getJSONObject("bank3");
//        JSONObject bankJson4 = JSON.parseObject(str1).getJSONObject("bank4");
//
//        Bank bank1 = JSON.parseObject(String.valueOf(bankJson1), Bank.class);
//        Bank bank2 = JSON.parseObject(String.valueOf(bankJson2), Bank.class);
//        Bank bank3 = JSON.parseObject(String.valueOf(bankJson3), Bank.class);
//        Bank bank4 = JSON.parseObject(String.valueOf(bankJson4), Bank.class);
//        JSONObject zkpFieldjson = JSON.parseObject(str1).getJSONObject("balProofParam");
//        BankBalanceAndBitZKP zkpField = JSON.parseObject(String.valueOf(zkpFieldjson), BankBalanceAndBitZKP.class);

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

        String strTemp = tempInput.substring(9, 28);
//        System.out.println(strTemp);
        StringBuilder mapValue = new StringBuilder("{");
        StringBuilder[] oneBankInformation = new StringBuilder[n];
        for (int i = 0; i < n; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            if (bank[i].equals(bankFrom)){
                oneBankInformation[i] = stringBuilder.append("\"").append(banks[i]).append("\"").append(":{\"bitCom\":").append(bankBitCom[i]).
                        append(",\"com\":").append(bankCom[i]).append(",\"doubleCom\":").append(bankDoubleCom[i]).append(",\"isFrom\":").
                        append(BigInteger.valueOf(1)).append(",\"quadraCom\":").append(bankQuadraCom[i]).append(",\"tripleCom\":").append(bankTripleCom[i]).append("},");
            }else {
                oneBankInformation[i] = stringBuilder.append("\"").append(banks[i]).append("\"").append(":{\"bitCom\":").append(bankBitCom[i]).
                        append(",\"com\":").append(bankCom[i]).append(",\"doubleCom\":").append(bankDoubleCom[i]).append(",\"isFrom\":").
                        append(BigInteger.valueOf(0)).append(",\"quadraCom\":").append(bankQuadraCom[i]).append(",\"tripleCom\":").append(bankTripleCom[i]).append("},");
            }
//            System.out.println(oneBankInformation[i]);
            mapValue.append(oneBankInformation[i]);
        }
//        System.out.println(mapValue);
        mapValue = mapValue.reverse().replace(0,1,"}").reverse();
//        System.out.println(mapValue);
        allTransactionsMap.put(strTemp, mapValue.toString());
//        System.out.println(allTransactionsMap);
        return true;
    }
}
