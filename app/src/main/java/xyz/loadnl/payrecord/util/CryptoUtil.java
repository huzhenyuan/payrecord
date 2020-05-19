package xyz.loadnl.payrecord.util;


import android.util.Log;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;

import xyz.loadnl.payrecord.AppConst;

public class CryptoUtil {

    public static boolean verify(String pubKey, String message, String signature) {
        return verify(Hex.decode(pubKey), message.getBytes(), Hex.decode(signature));
    }

    private static boolean verify(byte[] publicKeyEncoded, byte[] message, byte[] signature) {
        Ed25519PublicKeyParameters publicKeyRebuild = new Ed25519PublicKeyParameters(publicKeyEncoded, 0);
        Signer verifierRebuild = new Ed25519Signer();
        verifierRebuild.init(false, publicKeyRebuild);
        verifierRebuild.update(message, 0, message.length);
        boolean shouldVerifyRebuild = verifierRebuild.verifySignature(signature);
        return shouldVerifyRebuild;
    }

    public static String sign(String priKey, String message) {
        byte[] sign = sign(Hex.decode(priKey), message.getBytes());
        return Hex.toHexString(sign);
    }

    private static byte[] sign(byte[] privateKeyEncoded, byte[] message) {
        Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(privateKeyEncoded, 0);
        Signer signer = new Ed25519Signer();
        signer.init(true, privateKey);
        signer.update(message, 0, message.length);
        try {
            byte[] signature = signer.generateSignature();
            return signature;
        } catch (CryptoException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void genKey() {
        SecureRandom RANDOM = new SecureRandom();
        Ed25519KeyPairGenerator keyPairGenerator = new Ed25519KeyPairGenerator();
        keyPairGenerator.init(new Ed25519KeyGenerationParameters(RANDOM));
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
        Ed25519PrivateKeyParameters privateKey = (Ed25519PrivateKeyParameters) asymmetricCipherKeyPair.getPrivate();
        Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) asymmetricCipherKeyPair.getPublic();

        byte[] privateKeyEncoded = privateKey.getEncoded();
        byte[] publicKeyEncoded = publicKey.getEncoded();

        String priKey = Hex.toHexString(privateKeyEncoded);
        String pubKey = Hex.toHexString(publicKeyEncoded);

        Log.i(AppConst.TAG, "Gen PubKey " + pubKey);
        Log.i(AppConst.TAG, "Gen PriKey " + priKey);
    }

    public static void main(String[] args) {
        System.out.println(sign("3F692D2FBEFD561C047F1CA1B74424CDE9BEA1ADBFF505F8BA39462B743FA191",
                "1259500420884070400"));
    }
}
