/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author Pierre-Yves
 */
public class User {

    private String uid;
    private String password;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    //private String distinguishedName;
    private String commonName;
    private String secureQuestion;
    private String secureAnswer;
    private String totpSecret;

    private boolean flagTwoFactorsAuth;
    private int codeTwoFactorsAuth;
    
    private LinkedHashMap userInfoTab;
    
    public User() {
        
    }
    
    public User(String uid, String password) {
        this.uid = uid;
        this.password = password;
    }
    
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSecureQuestion() {
        return secureQuestion;
    }

    public void setSecureQuestion(String secureQuestion) {
        this.secureQuestion = secureQuestion;
    }

    public String getSecureAnswer() {
        return secureAnswer;
    }

    public void setSecureAnswer(String secureAnswer) {
        this.secureAnswer = secureAnswer;
    }

    public boolean isFlagTwoFactorsAuth() {
        return flagTwoFactorsAuth;
    }

    public void setFlagTwoFactorsAuth(boolean flagTwoFactorsAuth) {
        this.flagTwoFactorsAuth = flagTwoFactorsAuth;
    }

    public int getCodeTwoFactorsAuth() {
        return codeTwoFactorsAuth;
    }

    public void setCodeTwoFactorsAuth(int codeTwoFactorsAuth) {
        this.codeTwoFactorsAuth = codeTwoFactorsAuth;
    }

    public HashMap getUserInfoTab() {
        return userInfoTab;
    }

    public void setUserInfoTab(HashMap userInfoTab) {
        this.userInfoTab = (LinkedHashMap) userInfoTab;
    }

    /*public String getDistinguishedName() {
    return distinguishedName;
    }
    
    public void setDistinguishedName(String distinguishedName) {
    this.distinguishedName = distinguishedName;
    }*/

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
    
    public String getTotpSecret() {
        return totpSecret;
    }

    public void setTotpSecret(String totpSecret) {
        this.totpSecret = totpSecret;
    }
    
    /*
    Retrieve the user info
    */
    public LinkedHashMap GetInfo() {
        try {
            LinkedHashMap<String,String> infoMap = new LinkedHashMap();
            infoMap.put("uid", this.getUid());
            infoMap.put("password",  this.getPassword());
            
            if (!(getFirstName().isEmpty())) {
                infoMap.put("lastName", this.getLastName());
            }
            if (!(getFirstName().isEmpty())) {
                infoMap.put("firstName", this.getFirstName());
            }
            if (!(getCommonName().isEmpty())) {
                infoMap.put("commonName", getCommonName());
            }
            if (!(getEmail().isEmpty())) {
                infoMap.put("eMail", getEmail());
            }
            if (!(getPhoneNumber().isEmpty())) {
                infoMap.put("phoneNumber", getPhoneNumber());
            }
            if (!(getSecureQuestion().isEmpty())) {
                infoMap.put("securityQuestion", getSecureQuestion());
            }
            if (!(getSecureAnswer().isEmpty())) {
                infoMap.put("securityAnswer", getSecureAnswer());
            }
            if (!(getTotpSecret().isEmpty())) {
                infoMap.put("totpSecret", getTotpSecret());
            }
            return  infoMap;
        }
        catch (Exception e) {
            System.out.println("Erreur dans GetInfo");
            return null;
        }
    }
    
}
