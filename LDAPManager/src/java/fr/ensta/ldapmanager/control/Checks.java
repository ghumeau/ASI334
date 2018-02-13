/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.control;

import java.util.*;

/**
 *
 * @author guillaume.humeau
 */
public class Checks {
    
    public enum Argument{
        UID,
        PWD,
        NAME,
        MAIL;
    }
    
    //Validation de la syntaxe des UIDs saisis.
    public static boolean syntaxe(String st, Argument arg) {
        // Création de la liste des caractères valides
        List<String> validChar = new ArrayList<String>();
        int i;
        for (i = 65; i<=90; i++){validChar.add(Character.toString((char) i));}           // majuscules
        for (i = 97; i<=122; i++){validChar.add(Character.toString((char) i));}          // minuscules
        switch (arg){
            case NAME:
                validChar.add(Character.toString((char) 32));                            // espace
                validChar.add(Character.toString((char) 45));                            // tiret
                for (i = 192; i<=255; i++){validChar.add(Character.toString((char) i));} // accents
                break;
            case PWD:
            case UID:
                for (i = 48; i<=57; i++){validChar.add(Character.toString((char) i));}   // chiffres
                break;
            case MAIL:
                String[] mail = st.split("@");
                if (mail.length!=2){return false;}
                if (mail[1].split(".").length==1){return false;}
                validChar.add(Character.toString((char) 45));                            // tiret
                validChar.add(Character.toString((char) 46));                            // point
                validChar.add(Character.toString((char) 64));                            // @
                for (i = 48; i<=57; i++){validChar.add(Character.toString((char) i));}   // chiffres
        }
        
        for (i = 0; i<st.length(); i++){                  // Verification de la validité des caractères saisis
                if(!validChar.contains(st.substring(i,i+1))){return false;}
        }
        
        return true;
    }
    
    public static boolean isEmpty(String st){
        return st.length()==0;
    }
    
}
