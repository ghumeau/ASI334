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
    
    private static final String technicalAccount = "taccount";
    private static final String techAccPwd = "password";

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
            newUserInfo.put("uid", userToModify.getUid());
            newUserInfo.put("password",  userToModify.getPassword());
            if (!(userToModify.getLastName().isEmpty())) {
                newUserInfo.put("lastName", userToModify.getLastName());
            }
            if (!(userToModify.getFirstName().isEmpty())) {
                newUserInfo.put("firstName", userToModify.getFirstName());
            }
            if (!(userToModify.getCommonName().isEmpty())) {
                newUserInfo.put("commonName", userToModify.getCommonName());
            }
            if (!(userToModify.getEmail().isEmpty())) {
                newUserInfo.put("eMail", userToModify.getEmail());
            }
            if (!(userToModify.getPhoneNumber().isEmpty())) {
                newUserInfo.put("phoneNumber", userToModify.getPhoneNumber());
            }
            if (!(userToModify.getSecureQuestion().isEmpty())) {
                newUserInfo.put("securityQuestion", userToModify.getSecureQuestion());
            }
            if (!(userToModify.getSecureAnswer().isEmpty())) {
                newUserInfo.put("securityAnswer", userToModify.getSecureAnswer());
            }
            if (!(userToModify.getTotpSecret().isEmpty())) {
                newUserInfo.put("totpSecret", userToModify.getTotpSecret());
            }
            newUserInfo.put("totpFlag", (userToModify.isTotpFlag()) ? "TRUE" : "FALSE");
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
            String DN = DNSearch(technicalAccount);
            connector = new LDAPConnector(DN,techAccPwd);
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
        if (userMap.containsKey("lastName")) {
            user.setLastName(userMap.get("lastName").toString());
        }
        if (userMap.containsKey("firstName")) {
            user.setFirstName(userMap.get("firstName").toString());
        }
        if (userMap.containsKey("commonName")) {
            user.setCommonName(userMap.get("commonName").toString());
        }
        if (userMap.containsKey("eMail")) {
            user.setEmail(userMap.get("eMail").toString());
        }
        if (userMap.containsKey("phoneNumber")) {
            user.setPhoneNumber(userMap.get("phoneNumber").toString());
        }
        if (userMap.containsKey("securityQuestion")) {
            user.setSecureQuestion(userMap.get("securityQuestion").toString());
        }
        if (userMap.containsKey("securityAnswer")) {
            user.setSecureAnswer(userMap.get("securityAnswer").toString());
        }
        if (userMap.containsKey("totpSecret")) {
            user.setTotpSecret(userMap.get("totpSecret").toString());
        }
        if (userMap.containsKey("totpFlag")) {
            if ((userMap.get("totpFlag").toString()).equals("TRUE")) {
               user.setTotpFlag(true);
            }
            else {
                user.setTotpFlag(false);
            }
        }
    }
    
    public LDAPConnector getConnector() {
        return connector;
    }
    
}
