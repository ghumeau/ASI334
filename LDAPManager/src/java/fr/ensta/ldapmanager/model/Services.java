/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.model;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
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
    
    public Services(User user) {
        this.user =user;
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
        if (!user.getLastName().isEmpty()) {
            System.out.println("Le nom de l'utilisateur est : " + user.getLastName());
        }
        if (!user.getFirstName().isEmpty()) {
            System.out.println("Le prénom de l'utilisateur est : " + user.getFirstName());
        }
        if (!user.getCommonName().isEmpty()) {
            System.out.println("Le nom complet de l'utilisateur est : " + user.getCommonName());
        }
        if (!user.getEmail().isEmpty()) {
            System.out.println("L'e-mail de l'utiliateur est : " + user.getEmail());
        }
        if (!user.getPhoneNumber().isEmpty()) {
            System.out.println("Le numéro de téléphone de l'utilisateur est : " + user.getPhoneNumber());
        }
        if (!user.getSecureQuestion().isEmpty()) {
            System.out.println("La question de sécurité de l'utilisateur est : " + user.getSecureQuestion());
        }
        if (!user.getSecureAnswer().isEmpty()) {
            System.out.println("La réponse de sécurité de l'utilisateur est : " + user.getSecureAnswer());
        }
        if (!user.getTotpSecret().isEmpty()) {
            System.out.println("Le code de sécurité TOTP de l'utilisateur est : " + user.getTotpSecret());
        }
        String totpbool = (user.isTotpFlag()) ? "TRUE" : "FALSE";
        System.out.println("Le flag TOTP de l'utilisateur est positionné à : " + totpbool);
    }
    
    public void ModifyInfo(User userToModify) {
        
        HashMap oldUserInfo = new HashMap();
        HashMap newUserInfo = new HashMap();
        HashMap attributeToModify = new HashMap();
        HashMap attributeToAdd = new HashMap();
        HashMap attributeToDelete = new HashMap();
        
        try {
            //on récupère la map contenant les anciennes infos de l'utilisateur
            String DN = DNSearch(userToModify.getUid());
            String password = userToModify.getPassword();
            connector = new LDAPConnector(DN, password);
            connector.connect();
            
            oldUserInfo = RetrieveInfo();
            oldUserInfo.put("uid",userToModify.getUid());
            oldUserInfo.put("password", password);
            
            newUserInfo.put("uid",  userToModify.getUid());
            
            newUserInfo.put("password",  userToModify.getPassword());
            
            newUserInfo.put("lastName", userToModify.getLastName());
            
            newUserInfo.put("firstName", userToModify.getFirstName());
            
            newUserInfo.put("commonName", userToModify.getCommonName());
            
            newUserInfo.put("eMail", userToModify.getEmail());
            
            newUserInfo.put("phoneNumber", userToModify.getPhoneNumber());
            
            newUserInfo.put("securityQuestion", userToModify.getSecureQuestion());
           
            newUserInfo.put("securityAnswer", userToModify.getSecureAnswer());

            newUserInfo.put("totpSecret", userToModify.getTotpSecret());
                
            newUserInfo.put("totpFlag", (userToModify.isTotpFlag()) ? "TRUE" : "FALSE");
            
            Iterator iter = newUserInfo.keySet().iterator();
            
            String value;
            
            while(iter.hasNext()) {
                value = iter.next().toString();
                
                if (oldUserInfo.containsKey(value)) {
                    if (!newUserInfo.get(value).toString().isEmpty()) {
                        attributeToModify.put(value, newUserInfo.get(value));
                    }
                    else {
                        attributeToDelete.put(value, newUserInfo.get(value));
                    }
                }
                else {
                    if (!newUserInfo.get(value).toString().isEmpty()) {
                        attributeToAdd.put(value, newUserInfo.get(value));
                    }
                } 
            }
            
            connector.ModifyLDAPInfo(attributeToModify, DN);
            connector.AddLDAPInfo(attributeToAdd, DN);
            connector.DeleteLDAPInfo(attributeToDelete, DN);
            connector.disconnect();
        }
        catch (Exception e) {
            System.out.println("Erreur dans ModifyInfo");
        }
    }
    
    public void ModifyPassword(User userToModify) {
        try {
            String DN = DNSearch(userToModify.getUid());
            String newPwd = userToModify.getPassword();
            String techAccountDN = DNSearch(technicalAccount);
            connector = new LDAPConnector(techAccountDN,techAccPwd);
            connector.connect();
            connector.SavePassword(DN, newPwd);
            connector.disconnect();
        }
        catch (Exception e) {
            System.out.println("Erreur dans ModifyPassword");
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
    
    public String GenerateTotpKey(User userToModify) {
        GoogleAuth ga = new GoogleAuth();
        String totpKey = ga.generateKey();
        userToModify.setTotpSecret(totpKey);
        ModifyTotpKey(userToModify);
        return ga.getQRBarcodeURL(userToModify.getUid(), "LDAPManager", totpKey);
    }
    
    private void ModifyTotpKey(User userToModify) {
        try {
            String DN = DNSearch(userToModify.getUid());
            String totpSecret = userToModify.getTotpSecret();
            String techAccountDN = DNSearch(technicalAccount);
            connector = new LDAPConnector(techAccountDN,techAccPwd);
            connector.connect();
            connector.SaveTotpKey(DN, totpSecret);
            connector.disconnect();
        }
        catch (Exception e) {
            System.out.println("Erreur dans ModifyTotpKey");
        }
    }
    
    public void DeleteTotpKey(User userToModify) {
        
    }
    
    public boolean CheckTotpCode(User user, String inputCode) {
        try {
            long t = new Date().getTime() / TimeUnit.SECONDS.toMillis(30);
            GoogleAuth ga = new GoogleAuth();
            if (ga.check_code(user.getTotpSecret(),Long.parseLong(inputCode), t)) {
                System.out.println("totpCode OK");
                return true;
            }
            else {
                System.out.println("totpCode NOK");
                return false;
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            System.out.println("Erreur dans CheckTotpCode : " + ex.toString());
            return false;
        }
    }
    
    public LDAPConnector getConnector() {
        return connector;
    }
    
}
