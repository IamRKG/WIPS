package com.ford.purchasing.wips.common.layer.util;

import com.ford.it.crypt.symmetric.CryptException;
import com.ford.it.crypt.symmetric.SymmetricCrypt;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.it.properties.PropertyManager;

/**
 * Encryption/Decryption related utilities
 */
public class CryptUtil {

    private static final String CLASS_NAME = CryptUtil.class.getName();

    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    private static final String keyString = PropertyManager.getInstance()
            .getString("WIPSMobile.Application.Encryption.key");

    private static SymmetricCrypt symmetricCrypt = null;

    private CryptUtil() {
    }

    private static SymmetricCrypt getSymmetricCrypt() throws CryptException {
        if (CryptUtil.symmetricCrypt == null) {
            final SymmetricCrypt symmetricCrypt = new SymmetricCrypt();
            symmetricCrypt.setSecretKeyFromString(keyString);
            CryptUtil.setSymmetricCrypt(symmetricCrypt);
        }
        return CryptUtil.symmetricCrypt;
    }

    private static void setSymmetricCrypt(final SymmetricCrypt symmetricCrypt) {
        CryptUtil.symmetricCrypt = symmetricCrypt;
    }

    public static String decrypt(final String encryptedValue) {
        final String methodName = "decrypt";
        String decryptedValue = "Invalid";
        try {
            decryptedValue = getSymmetricCrypt().decrypt(encryptedValue);
        } catch (final CryptException cryptException) {
            log.throwing(CLASS_NAME, methodName, cryptException);
        }
        return decryptedValue;
    }

    public static String encrypt(final String unencryptedValue) {
        final String methodName = "encrypt";
        String encryptedValue = "";
        try {
            encryptedValue = getSymmetricCrypt().encrypt(unencryptedValue);
        } catch (final CryptException cryptException) {
            log.throwing(CLASS_NAME, methodName, cryptException);
        }
        return encryptedValue;
    }

}
