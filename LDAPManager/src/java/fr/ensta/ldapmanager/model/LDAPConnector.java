/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.model;

import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;


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
     * Initialize the connection to the LDAP server
     * @throws javax.naming.NamingException
    ********************/
    public void connect() throws NamingException {

        contexte = new InitialDirContext(environnement);
        
    }
    
    /********************
     * Disconnect from the LDAP server
     * @throws javax.naming.NamingException
    ********************/
    public void disconnect() throws NamingException {
        
        contexte.close();
    
    }
    
    /********************
     * Search the DN of a specified user
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
     * Retrieve the info of a specified user in the LDAP
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

                    case "phoneNumber":
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
     * Store the new info of a specified user in the LDAP
     * @param newInfo HashMap containing the new info to store in the LDAP server
     * @param DN Distinguished Name of the user to modify
    ********************/
    public void ModifyLDAPInfo(HashMap newInfo, String DN) {
        
        try
        {

        ModificationItem[] mods = new ModificationItem[newInfo.size() - 1];
        Attribute[] attrMods = new BasicAttribute[newInfo.size() - 1];

        attrMods[0] = new BasicAttribute("userPassword", newInfo.get("PWD"));
        
        int i = 1;

        if (newInfo.containsKey("lastName")) {
            attrMods[i] = new BasicAttribute("sn", newInfo.get("lastName"));
            i++;
        }
        if (newInfo.containsKey("firstName")) {
            attrMods[i] = new BasicAttribute("givenName", newInfo.get("firstName"));
            i++;
        }
        if (newInfo.containsKey("commonName")) {
            attrMods[i] = new BasicAttribute("cn", newInfo.get("commonName"));
            i++;
        }
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
        if (newInfo.containsKey("totpSecret")) {
            attrMods[i] = new BasicAttribute("totpsecret", newInfo.get("totpSecret"));
            i++;
        }
        if (newInfo.containsKey("totpFlag")) {
            attrMods[i] = new BasicAttribute("totpflag", newInfo.get("totpFlag"));
            i++;
        }

        for (int j=0; j < newInfo.size()-1; j++) {
            mods[j] = new ModificationItem (DirContext.REPLACE_ATTRIBUTE, attrMods[j]);
        }

        this.contexte.modifyAttributes(DN, mods);
        System.out.println("Modification de l'utilisateur : SUCCES");
        }
        catch(Exception e)
        {
        System.out.println("Modification de l'utilisateur : ECHEC");
        e.printStackTrace();
        }
    }
    
    /********************
     * Retrieve the security info of a specified user
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
     * Context Getter
     * @return the LDAPConnector context
    ********************/
     public DirContext getContexte() {
        return contexte;
    }
    
}

