package com.txedo.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ReadRSAKey {
    protected final static Logger LOGGER = Logger.getLogger(ReadRSAKey.class);
  
    public final static String RESOURCES_DIR = "";

    public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        LOGGER.info("BouncyCastle provider added.");

        String log4jConfPath = "maven-archetype-quickstart\\log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);

        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        try {
            PrivateKey priv = generatePrivateKey(factory, RESOURCES_DIR + "id_rsa");
            LOGGER.info(String.format("Instantiated private key: %s", priv));
        
            PublicKey pub = generatePublicKey(factory, RESOURCES_DIR + "id_rsa.pub");
            LOGGER.info(String.format("Instantiated public key: %s", pub));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private static PrivateKey generatePrivateKey(KeyFactory factory, String filename) throws InvalidKeySpecException, FileNotFoundException, IOException {
        PemFile pemFile = new PemFile(filename);
        byte[] content = pemFile.getPemObject().getContent();
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
        return factory.generatePrivate(privKeySpec);
    }
    
    private static PublicKey generatePublicKey(KeyFactory factory, String filename) throws InvalidKeySpecException, FileNotFoundException, IOException {
        PemFile pemFile = new PemFile(filename);
        byte[] content = pemFile.getPemObject().getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        return factory.generatePublic(pubKeySpec);
    }

}
