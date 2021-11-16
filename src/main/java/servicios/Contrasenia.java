package servicios;

import java.util.Random;

public class Contrasenia {
    
public static char[] generatePswd()
    {
        int len = 8;
        String charsCaps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String nums = "0123456789";
        String symbols = "!@#$%^&*_=+-/.?<>)";
        String passSymbols = charsCaps + chars + nums + symbols;
        Random rnd = new Random();      
        char[] password = new char[len];
        for (int i = 0; i < len; i++) 
        {
            password[i] = passSymbols.charAt(rnd.nextInt(passSymbols.length()));  
        }
        return password;
    }
}
