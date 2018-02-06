/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager;

import java.util.HashMap;
import javax.naming.NamingException;

/**
 *
 * @author Pierre-Yves
 */
public class Services {
    
    private LDAPConnector connector;
    private User user;
    private HashMap userMap;

    public Services() {
        
    }
    
    public LDAPConnector getConnector() {
        return connector;
    }
    
    public User Authentify(String uid, String password) {
        try {
            connector = new LDAPConnector(uid, password);
            connector.connect();
            user = new User(uid, password);
            userMap = RetrieveInfo();
            FillUserInfo();
            PrintInfo();
            System.out.println("Succesfully authenticated");
            return user;
        }
        catch (NamingException e) {
            System.out.println("Authentication failure");
            return null;
        }
    }
    
    public HashMap RetrieveInfo() {
        try {
            return connector.UserScopeSearch(user.getUid());
        }
        catch (NamingException e) {
            return null;
        }
        
    }
    
    private void PrintInfo() {
        System.out.println("Le login de l'utilisateur est : " + user.getUid());
        System.out.println("Le mot de passe de l'utilisateur est : " + user.getPassword());
        if (user.getLastName() != null) {
            System.out.println("Le nom de l'utilisateur est : " + user.getLastName());
        }
        if (user.getFirstName() != null) {
            System.out.println("Le prénom de l'utilisateur est : " + user.getFirstName());
        }
        if (user.getEmail() != null) {
            System.out.println("L'e-mail de l'utiliateur est : " + user.getEmail());
        }
        if (user.getPhoneNumber() != null) {
            System.out.println("Le numéro de téléphone de l'utilisateur est : " + user.getPhoneNumber());
        }
    }
    
    public void ModifyInfo(User userToModify) {
        HashMap newUserInfo = new HashMap();
        try {
            newUserInfo.put("UID", userToModify.getUid());
            newUserInfo.put("PWD",  userToModify.getPassword());
            if (!(userToModify.getLastName().isEmpty())) {
                newUserInfo.put("LASTNAME", userToModify.getLastName());
            }
            if (!(userToModify.getFirstName().isEmpty())) {
                newUserInfo.put("FIRSTNAME", userToModify.getFirstName());
            }
            if (!(userToModify.getEmail().isEmpty())) {
                newUserInfo.put("EMAIL", userToModify.getEmail());
            }
            if (!(userToModify.getPhoneNumber().isEmpty())) {
                newUserInfo.put("PHONENUMBER", userToModify.getPhoneNumber());
            }
            connector.ModifyLDAPInfo(newUserInfo);
        }
        catch (Exception e) {
            System.out.println("Erreur dans ModifyInfo");
        }
    }

    private void FillUserInfo() {
        if (userMap.containsKey("LASTNAME")) {
            user.setLastName(userMap.get("LASTNAME").toString());
        }
        if (userMap.containsKey("FIRSTNAME")) {
            user.setFirstName(userMap.get("FIRSTNAME").toString());
        } 
        if (userMap.containsKey("EMAIL")) {
            user.setEmail(userMap.get("EMAIL").toString());
        }
        if (userMap.containsKey("PHONENUMBER")) {
            user.setPhoneNumber(userMap.get("PHONENUMBER").toString());
        }
    }
    
}