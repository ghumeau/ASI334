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
    
    /********************
     * Constructor with user
     * @param user User to set
    ********************/
    public Services(User user) {
        this.user =user;
    }
    
    /********************
     * Authentication sequence with user credentials
     * @param uid id of the user who tries to connect
     * @param password password of the user who tries to connect
     * @return the authentified user / return null if the authentication fails
    ********************/
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
    
    /********************
     * Searches the DN of the specified user ID
     * @param uid id of the user
     * @return the distinguished name
    ********************/
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
    
    /********************
     * Authentifies the specified user against the LDAP server
     * @param uid id of the user who tries to connect
     * @param password password of the user who tries to connect
     * @param DN distinguished name of the user who tries to connect
     * @return the user if the authentication succeed else returns null
    ********************/
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
    
    /********************
     * Retrieves info of a user in the LDAP server
     * @return a HashMap containing the user info retrieved in the LDAP server
    ********************/
    private HashMap RetrieveInfo() {
        try {
            return connector.UserScopeSearch(user.getUid());
        }
        catch (NamingException e) {
            return null;
        }
        
    }
    
    /********************
     * Retrieves the DN of a user in the LDAP server
     * @param uid id of the user
     * @return the DN of a user retrieved in the LDAP server / if the user does not exist returns null
    ********************/
    private String RetrieveDNInfo(String uid) {
        try {
            return connector.UserDNSearch(uid);
        }
        catch (NamingException e) {
            return null;
        }
        
    }
    
    /********************
     * Prints the user info
    ********************/
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
    
    /********************
     * Modifies the info of a user in the LDAP server
     * @param userToModify user to modify
    ********************/
    public void ModifyInfo(User userToModify) {
        
        HashMap oldUserInfo;
        HashMap newUserInfo = new HashMap();
        HashMap attributeToModify = new HashMap();
        HashMap attributeToAdd = new HashMap();
        HashMap attributeToDelete = new HashMap();
        
        try {
            //first, the old info of the user are retrieved in a map from the LDAP server
            String DN = DNSearch(userToModify.getUid());
            String password = userToModify.getPassword();
            connector = new LDAPConnector(DN, password);
            connector.connect();
            
            oldUserInfo = RetrieveInfo();
            oldUserInfo.put("uid",userToModify.getUid());
            oldUserInfo.put("password", password);
            
            //then, the new info of the user are stored in a map
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
            
            
            //finally the old map and the new map are compared to determine wether the attribute is replaced, added or deleted in the LDAP server
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
    
    /********************
     * Modifies the password of a user in the LDAP server
     * @param userToModify user whom password will be modified 
    ********************/
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
    
    /********************
     * Retrieves the security info of a user (security question and answer) in the LDAP server
     * @param uid id of the user
     * @return a HashMap containing the user security info retrieved in the LDAP server
    ********************/
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

    /********************
     * Fills the user attributes with the info retrieved from the LDAP server
    ********************/
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
    
    /********************
     * Generates the TOTP key in case of double authentication
     * @param userToModify the user whom TOTP key needs to be generated
     * @return the QR code URL
    ********************/
    public String GenerateTotpKey(User userToModify) {
        GoogleAuth ga = new GoogleAuth();
        String totpKey = ga.generateKey();
        userToModify.setTotpSecret(totpKey);
        //ModifyTotpKey(userToModify);
        return ga.getQRBarcodeURL(userToModify.getUid(), "LDAPManager", totpKey);
    }
    
    /********************
     * Verifies the TOTP key of a user
     * @param userToCheck user whom TOTP key needs to be checked
     * @param inputCode the code filled by the user
     * @return a boolean
    ********************/
    public boolean CheckTotpCode(User userToCheck, String inputCode) {
        try {
            long t = new Date().getTime() / TimeUnit.SECONDS.toMillis(30);
            GoogleAuth ga = new GoogleAuth();
            if (ga.check_code(userToCheck.getTotpSecret(),Long.parseLong(inputCode), t)) {
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
