/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.model;

import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


public class LDAPConnector {
    
    private Hashtable environnement;
    private DirContext contexte;
    //IP address of the LDAP server
    private static final String serverIP = "localhost";
    //Port number of the LDAP server
    //private static final String serverPort = "1389";
    private static final String serverPort = "389";
    //LDAP domain
    private static final String baseDN = "dc=ensta,dc=fr";
    
    
    /********************
     * Anonymous connector
    ********************/
    public LDAPConnector() {

        environnement = new Hashtable();
        environnement.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environnement.put(Context.PROVIDER_URL, "ldap://"+serverIP+":"+serverPort+"/");
        environnement.put(Context.SECURITY_AUTHENTICATION, "none");
        
    }

    /********************
     * Connector with user credentials
     * @param DN Distinguished Name of the user who tries to connect
     * @param password password of the user who tries to connect
    ********************/
    public LDAPConnector(String DN, String password){
        
        //User login
        String serverLogin = DN;
        //User password
        String serverPass = password;

        environnement = new Hashtable();
        environnement.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environnement.put(Context.PROVIDER_URL, "ldap://"+serverIP+":"+serverPort+"/");
        environnement.put(Context.SECURITY_AUTHENTICATION, "simple");
        environnement.put(Context.SECURITY_PRINCIPAL, serverLogin);
        environnement.put(Context.SECURITY_CREDENTIALS, serverPass);
          
    }
   
    /********************
     * Initializes the connection to the LDAP server
     * @throws javax.naming.NamingException
    ********************/
    public void connect() throws NamingException {

        contexte = new InitialDirContext(environnement);
        
    }
    
    /********************
     * Disconnects from the LDAP server
     * @throws javax.naming.NamingException
    ********************/
    public void disconnect() throws NamingException {
        
        contexte.close();
    
    }
    
    /********************
     * Searches the DN of a specified user
     * @param uid id of the user
     * @return the DN of the user
     * @throws javax.naming.NamingException
    ********************/
    public String UserDNSearch(String uid) throws NullPointerException, NamingException {
       
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> answer = contexte.search(baseDN, "UID=" + uid.trim(), controls);
        SearchResult sr = answer.next();
        
        return sr.getNameInNamespace();
        
    }
    
    /********************
     * Retrieves the info of a specified user in the LDAP
     * @param uidUser id of the user
     * @return a HashMap containing the user info retrieved from the LDAP server
     * @throws javax.naming.NamingException
    ********************/
    public HashMap UserScopeSearch(String uidUser) throws NamingException {
        HashMap infoTab = new HashMap();
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> answer = this.contexte.search(baseDN, "UID=" + uidUser.trim(), controls);
        
        if (answer.hasMore()) {
            Attributes attrs = answer.next().getAttributes();
        
            NamingEnumeration e;
        
            e = attrs.getIDs();
        
            while (e.hasMoreElements()) {
                String attr = (String) e.nextElement();
                String [] value = attrs.get(attr).toString().split(": ");
        
                switch (value[0])
                {
                    case "sn":
                        infoTab.put("lastName", value[1]);
                        break;

                    case "givenName":
                        infoTab.put("firstName", value[1]);
                        break;
                    
                    case "cn":
                        infoTab.put("commonName", value[1]);
                        break;

                    case "mail":
                        infoTab.put("eMail", value[1]);
                        break;

                    case "telephoneNumber":
                        infoTab.put("phoneNumber", value[1]);
                        break;
                    
                    case "securityquestion":
                        infoTab.put("securityQuestion", value[1]);
                        break;
                        
                    case "securityanswer":
                        infoTab.put("securityAnswer", value[1]);
                        break;
                    
                    case "totpsecret":
                        infoTab.put("totpSecret", value[1]);
                        break;
                        
                    case "totpflag":
                        infoTab.put("totpFlag", value[1]);
                        break;
                        
                }
            }
        }
        return infoTab;
    }
    
    /********************
     * Stores the new info of a specified user in the LDAP
     * @param newInfo HashMap containing the new info to store in the LDAP server
     * @param DN Distinguished Name of the user to modify
    ********************/
    public void ModifyLDAPInfo(HashMap newInfo, String DN) {
        
        try
        {
        
        //Info that cannot be modified are removed from the map
        newInfo.remove("uid");
        newInfo.remove("lastName");
        newInfo.remove("firstName");
        newInfo.remove("commonName");
        newInfo.remove("totpSecret");
                

        ModificationItem[] mods = new ModificationItem[newInfo.size()];
        Attribute[] attrMods = new BasicAttribute[newInfo.size()];

        int i = 0;
        
        attrMods[i] = new BasicAttribute("userPassword", newInfo.get("password"));
        i++;

        if (newInfo.containsKey("eMail")) {
            attrMods[i] = new BasicAttribute("mail", newInfo.get("eMail"));
            i++;
        }
        
        if (newInfo.containsKey("phoneNumber")) {
            attrMods[i] = new BasicAttribute("telephoneNumber", newInfo.get("phoneNumber"));
            i++;
        }

        if (newInfo.containsKey("securityQuestion")) {
            attrMods[i] = new BasicAttribute("securityquestion", newInfo.get("securityQuestion"));
            i++;
        }

        if (newInfo.containsKey("securityAnswer")) {
            attrMods[i] = new BasicAttribute("securityanswer", newInfo.get("securityAnswer"));
            i++;
        }

        attrMods[i] = new BasicAttribute("totpflag", newInfo.get("totpFlag"));

        for (int j=0; j < newInfo.size(); j++) {
            mods[j] = new ModificationItem (DirContext.REPLACE_ATTRIBUTE, attrMods[j]);
        }

        contexte.modifyAttributes(DN, mods);
            System.out.println("Modification des attributs de l'utilisateur : SUCCES");
        }
        catch(Exception e)
        {
            System.out.println("Modification des attributs de l'utilisateur : ECHEC");
        }
    }
    
     /********************
     * Adds info of a specified user in the LDAP
     * @param newInfo HashMap containing the new info to store in the LDAP server
     * @param DN Distinguished Name of the user to modify
    ********************/
    public void AddLDAPInfo(HashMap newInfo, String DN) {
        
        try
        {

        ModificationItem[] mods = new ModificationItem[newInfo.size()];
        Attribute[] attrMods = new BasicAttribute[newInfo.size()];

        int i = 0;

        if (newInfo.containsKey("eMail")) {
            attrMods[i] = new BasicAttribute("mail", newInfo.get("eMail"));
            i++;
        }
        
        if (newInfo.containsKey("phoneNumber")) {
            attrMods[i] = new BasicAttribute("telephoneNumber", newInfo.get("phoneNumber"));
            i++;
        }

        if (newInfo.containsKey("securityQuestion")) {
            attrMods[i] = new BasicAttribute("securityquestion", newInfo.get("securityQuestion"));
            i++;
        }

        if (newInfo.containsKey("securityAnswer")) {
            attrMods[i] = new BasicAttribute("securityanswer", newInfo.get("securityAnswer"));
            i++;
        }

        for (int j=0; j < newInfo.size(); j++) {
            mods[j] = new ModificationItem (DirContext.ADD_ATTRIBUTE, attrMods[j]);
        }

        contexte.modifyAttributes(DN, mods);
        
            System.out.println("Ajout d'attributs à l'utilisateur : SUCCES");
        }
        catch(Exception e)
        {
            System.out.println("Ajout d'attributs à l'utilisateur : ECHEC");
        }
    }
    
    /********************
     * Deletes info of a specified user in the LDAP
     * @param newInfo HashMap containing the new info to store in the LDAP server
     * @param DN Distinguished Name of the user to modify
    ********************/
    public void DeleteLDAPInfo(HashMap newInfo, String DN) {
        
        try
        {

        ModificationItem[] mods = new ModificationItem[newInfo.size()];
        Attribute[] attrMods = new BasicAttribute[newInfo.size()];

        int i = 0;
        
        if (newInfo.containsKey("eMail")) {
            attrMods[i] = new BasicAttribute("mail");
            i++;
        }
        
        if (newInfo.containsKey("phoneNumber")) {
            attrMods[i] = new BasicAttribute("telephoneNumber");
            i++;
        }

        if (newInfo.containsKey("securityQuestion")) {
            attrMods[i] = new BasicAttribute("securityquestion");
            i++;
        }

        if (newInfo.containsKey("securityAnswer")) {
            attrMods[i] = new BasicAttribute("securityanswer");
            i++;
        }
        
        if (newInfo.containsKey("totpSecret")) {
            attrMods[i] = new BasicAttribute("totpsecret");
            i++;
        }

        for (int j=0; j < newInfo.size(); j++) {
            mods[j] = new ModificationItem (DirContext.REMOVE_ATTRIBUTE, attrMods[j]);
        }

        contexte.modifyAttributes(DN, mods);
            System.out.println("Suppression des attributs de l'utilisateur : SUCCES");
        }
        catch(Exception e)
        {
            System.out.println("Suppression des attributs de l'utilisateur : ECHEC");
        }
    }
    
    /********************
     * Retrieves the security info of a specified user
     * @param uid id of the user
     * @return a HashMap containing the security info of a user
     * @throws javax.naming.NamingException
    ********************/
    public HashMap UserSecurityInfo(String uid) throws NamingException {
        
        HashMap secInfoTab = new HashMap();
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> answer = this.contexte.search(baseDN, "UID=" + uid.trim(), controls);
        
        if (answer.hasMore()) {
            Attributes attrs = answer.next().getAttributes();
        
            NamingEnumeration e;
        
            e = attrs.getIDs();
        
            while (e.hasMoreElements()) {
                String attr = (String) e.nextElement();
                String [] value = attrs.get(attr).toString().split(": ");
        
                switch (value[0])
                {   
                    case "securityquestion":
                        secInfoTab.put("securityQuestion", value[1]);
                        break;
                        
                    case "securityanswer":
                        secInfoTab.put("securityAnswer", value[1]);
                        break;                        
                }
            }
        }
        return secInfoTab;
    }
 
    /********************
     * Saves the new password in case of reset
     * @param DN Distinguished Name of the user to modify
     * @param newPassword new password to store in the LDAP server
    ********************/
    public void SavePassword(String DN, String newPassword) {
        
        try {
            ModificationItem[] mods = new ModificationItem[1];
            Attribute[] attrMods = new BasicAttribute[1];

            attrMods[0] = new BasicAttribute("userPassword", newPassword);

            mods[0] = new ModificationItem (DirContext.REPLACE_ATTRIBUTE, attrMods[0]);

            contexte.modifyAttributes(DN, mods);
            System.out.println("Modification du mot de passe de l'utilisateur : SUCCES");
        }
        catch(Exception e) {
            System.out.println("Modification du mot de passe de l'utilisateur : ECHEC");
        }
    }
    
    /********************
     * Saves the TOTP key of a specified user
     * @param DN Distinguished Name of the user to modify
     * @param totpSecret TOTP secret to store in the LDAP server
    ********************/
    public void SaveTotpKey(String DN, String totpSecret) {
        
        try {
            ModificationItem[] mods = new ModificationItem[1];
            Attribute[] attrMods = new BasicAttribute[1];

            attrMods[0] = new BasicAttribute("totpsecret", totpSecret);

            mods[0] = new ModificationItem (DirContext.REPLACE_ATTRIBUTE, attrMods[0]);

            contexte.modifyAttributes(DN, mods);
            System.out.println("Enregistrement du secret TOTP de l'utilisateur : SUCCES");
        }
        catch(Exception e) {
            System.out.println("Enregistrement du secret TOTP de l'utilisateur : ECHEC");
        }
    }
    
    /********************
     * Context Getter
     * @return the LDAPConnector context
    ********************/
     public DirContext getContexte() {
        return contexte;
    }
    
}

