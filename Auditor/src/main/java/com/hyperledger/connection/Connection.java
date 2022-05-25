package com.hyperledger.connection;

import org.hyperledger.fabric.gateway.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

/**
 * @author Lxgxgxgxg
 * @version 1.0.0
 * @ClassName Connection.java
 * @Description TODO
 * @createTime 2021年11月12日 17:42:00
 */
public class Connection {

    /**
     * 获取网络
     * @param
     */
    public static Network getNetwork() {
        Network network = null;
        try {
            //获取相应参数
            Properties properties = new Properties();
            InputStream inputStream = Connection.class.getResourceAsStream("/fabric.config.properties");
            properties.load(inputStream);

            String networkConfigPath = properties.getProperty("networkConfigPath");
            String channelName = properties.getProperty("channelName");
            String contractName = properties.getProperty("contractName");
            //使用org1中的user1初始化一个网关wallet账户用于连接网络
            String certificatePath = properties.getProperty("certificatePath");
            X509Certificate certificate = readX509Certificate(Paths.get(certificatePath));

            String privateKeyPath = properties.getProperty("privateKeyPath");
            PrivateKey privateKey = getPrivateKey(Paths.get(privateKeyPath));

            Wallet wallet = Wallets.newInMemoryWallet();
            wallet.put("user1", Identities.newX509Identity("Org2MSP", certificate, privateKey));

            //根据connection.json 获取Fabric网络连接对象
            Gateway.Builder builder = Gateway.createBuilder()
                    .identity(wallet, "user1")
                    .networkConfig(Paths.get(networkConfigPath));
            //连接网关
            Gateway gateway = builder.connect();
//            FileWriter file4 = new FileWriter("netWork.txt");
//            file4.write(network+"\n");
//            file4.close();
            network = gateway.getNetwork(channelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return network;
    }

    /**
     * 获取合约
     * @param
     */
    public static Contract getContract(Network network) {
        Contract contract = network.getContract("mycc");
        return  contract;
    }

    private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
        try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
            return Identities.readX509Certificate(certificateReader);
        }
    }


    private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException {
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
            return Identities.readPrivateKey(privateKeyReader);
        }
    }
}
