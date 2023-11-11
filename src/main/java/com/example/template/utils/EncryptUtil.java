package com.example.template.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.Arrays;

public class EncryptUtil {

    /**
     *
     * @param value valor a encryptar
     * @return devuelve el valor encripotado
     */
    public static String encryptValue(String value) {
        return BCrypt.withDefaults().hashToString(6, value.toCharArray());
    }

    /**
     *
     * @param value valor inicial
     * @param hashedValue valor con el hash aplicado
     * @return el estado de la comprobacion
     */
    public static boolean checkValues(String value, String hashedValue) {
        return BCrypt.verifyer().verify(value.toCharArray(), hashedValue.toCharArray()).verified;
    }
}
