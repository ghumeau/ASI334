/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.model;

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
    
    public User AuthenticationSequence(String uid, String password) {
        //On vérifie en premier lieu si l'utilisateur est présent dans l'annuaire
        String DN = DNSearch(uid);
        if (!(DN.isEmpty())) {
            return Authentify(uid, password, DN);
        }
        else {
            return null;
        }
    }
    
   //DNSearch 
    private String DNSearch(String uid) {
        try {
            connector = new LDAPConnector();
            connector.connect();
            //connector.UserSearch(uid);
            //user = new User();
            //user.setUid(uid);
            String DN = RetrieveDNInfo(uid);
            connector.disconnect();
            return DN;
        }
        catch (NullPointerException e) {
            System.out.println("Utilisateur inexistant");
            return "";
        }
        catch (NamingException e) {
             System.out.println("Problème de connexion ou de déconnexion");
             return "";
        }
        
    }
    
    private User Authentify(String uid, String password, String DN) {
        try {
            connector = new LDAPConnector(DN, password);
            connector.connect();
            user = new User(uid, password);
            userMap = RetrieveInfo();
            FillUserInfo();
            PrintInfo();
            System.out.println("Succesfully authenticated");
            connector.disconnect();
            return user;
        } catch (NamingException e) {
            System.out.println("Authentication failure");
            return null;
        }
    }
    
    private HashMap RetrieveInfo() {
        try {
            return connector.UserScopeSearch(user.getUid());
        }
        catch (NamingException e) {
            return null;
        }
        
    }
    
    private String RetrieveDNInfo(String uid) {
        try {
            return connector.UserDNSearch(uid);
        }
        catch (NamingException e) {
            return null;
        }
        
    }
    
    public void PrintInfo() {
        System.out.println("Le login de l'utilisateur est : " + user.getUid());
        System.out.println("Le mot de passe de l'utilisateur est : " + user.getPassword());
        if (user.getLastName() != null) {
            System.out.println("Le nom de l'utilisateur est : " + user.getLastName());
        }
        if (user.getFirstName() != null) {
            System.out.println("Le prénom de l'utilisateur est : " + user.getFirstName());
        }
        if (user.getCommonName() != null) {
            System.out.println("Le nom complet de l'utilisateur est : " + user.getCommonName());
        }
        if (user.getEmail() != null) {
            System.out.println("L'e-mail de l'utiliateur est : " + user.getEmail());
        }
        if (user.getPhoneNumber() != null) {
            System.out.println("Le numéro de téléphone de l'utilisateur est : " + user.getPhoneNumber());
        }
        if (user.getSecureQuestion()!= null) {
            System.out.println("La question de sécurité de l'utilisateur est : " + user.getSecureQuestion());
        }
        if (user.getSecureAnswer()!= null) {
            System.out.println("La réponse de sécurité de l'utilisateur est : " + user.getSecureAnswer());
        }
        if (user.getTotpSecret()!= null) {
            System.out.println("Le code de sécurité TOTP de l'utilisateur est : " + user.getTotpSecret());
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
            if (!(userToModify.getCommonName().isEmpty())) {
                newUserInfo.put("COMMONNAME", userToModify.getCommonName());
            }
            if (!(userToModify.getEmail().isEmpty())) {
                newUserInfo.put("EMAIL", userToModify.getEmail());
            }
            if (!(userToModify.getPhoneNumber().isEmpty())) {
                newUserInfo.put("PHONENUMBER", userToModify.getPhoneNumber());
            }
            if (!(userToModify.getSecureQuestion().isEmpty())) {
                newUserInfo.put("SECURITYQUESTION", userToModify.getSecureQuestion());
            }
            if (!(userToModify.getSecureAnswer().isEmpty())) {
                newUserInfo.put("SECURITYANSWER", userToModify.getSecureAnswer());
            }
            if (!(userToModify.getTotpSecret().isEmpty())) {
                newUserInfo.put("TOTPSECRET", userToModify.getTotpSecret());
            }
            String DN = DNSearch(userToModify.getUid());
            connector = new LDAPConnector(DN, userToModify.getPassword());
            connector.connect();
            connector.ModifyLDAPInfo(newUserInfo, DN);
            connector.disconnect();
        }
        catch (Exception e) {
            System.out.println("Erreur dans ModifyInfo");
        }
    }
    
    public HashMap RetrieveSecurityInfo(String uid) {
        try {
            connector = new LDAPConnector();
            connector.connect();
            HashMap securityInfo = connector.UserSecurityInfo(uid);
            connector.disconnect();
            if (securityInfo.isEmpty()) {
                System.out.println("utilisateur inexistant");
            }
            return securityInfo;
        }
        catch (NamingException e) {
             System.out.println("Problème de connexion ou de déconnexion");
             return null;
        }
    }

    private void FillUserInfo() {
        if (userMap.containsKey("LASTNAME")) {
            user.setLastName(userMap.get("LASTNAME").toString());
        }
        if (userMap.containsKey("FIRSTNAME")) {
            user.setFirstName(userMap.get("FIRSTNAME").toString());
        }
        if (userMap.containsKey("COMMONNAME")) {
            user.setCommonName(userMap.get("COMMONNAME").toString());
        }
        if (userMap.containsKey("EMAIL")) {
            user.setEmail(userMap.get("EMAIL").toString());
        }
        if (userMap.containsKey("PHONENUMBER")) {
            user.setPhoneNumber(userMap.get("PHONENUMBER").toString());
        }
        if (userMap.containsKey("SECURITYQUESTION")) {
            user.setSecureQuestion(userMap.get("SECURITYQUESTION").toString());
        }
        if (userMap.containsKey("SECURITYANSWER")) {
            user.setSecureAnswer(userMap.get("SECURITYANSWER").toString());
        }
        if (userMap.containsKey("TOTPSECRET")) {
            user.setTotpSecret(userMap.get("TOTPSECRET").toString());
        }
    }
    
    public LDAPConnector getConnector() {
        return connector;
    }
    
}
