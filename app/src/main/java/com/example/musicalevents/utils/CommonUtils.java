package com.example.musicalevents.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase no podra tener clases hijos.No se puede heredar de ella gracias al modificador final.
 */
public final class CommonUtils {
    //TODOS SUS METODOS SON STATIC
    /**
     * metodo qye comprueba que la contraseña debe contener al menos un digito
     * debe contener al menos un caracter mayuscula
     * debe contener al menos un caracter minuscula
     * y su longitud minima es de 8 y maxima 20
     */

    public static boolean isPasswordValid(String password){
        String PASSWORDPATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(PASSWORDPATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
