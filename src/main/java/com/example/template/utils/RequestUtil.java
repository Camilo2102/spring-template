package com.example.template.utils;

public class RequestUtil {

    /**
     * Se encarga de obtener una parte en concreto de la uri
     * @param requestURI la uri desde la que se hizo la peticion
     * @param part la parte de la peticion que vamos a obtener
     * @return la cadena de texto que pertenece a esa parte
     */
    public static String getPartFromURI(String requestURI, int part){
        String[] partsURI = requestURI.split("/");
        return partsURI[part];
    }

    /**
     * Se encarga de obtener una parte en concreto de la uri
     * @param requestURI la uri desde la que se hizo la peticion
     * @return la cadena de texto que pertenece a esa parte
     */
    public static String getPartFromURIUntil(String requestURI, int part){
        String[] partsURI = requestURI.split("/");

        if (partsURI.length <= 1) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 1; i < part; i++) {
            result.append(partsURI[i]);
            if (i < part - 1) {
                result.append("/");
            }
        }

        return result.toString();
    }


}
