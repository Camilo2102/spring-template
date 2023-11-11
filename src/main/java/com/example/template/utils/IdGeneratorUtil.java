package com.example.template.utils;

import java.util.UUID;

public class IdGeneratorUtil {

    /**
     * Genera id unicos
     * @return Id unico
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Genera un id unico con base en la cantidad de datos ingeresados
     * @param length numero de caracteres del id
     * @return el id con la longitud ingresada
     */
    public static String generateUUID(int length) {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, Math.min(uuid.length(), length));
    }
}
