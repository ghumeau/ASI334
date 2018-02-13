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
    static final String serverIP = "localhost";
    //Port number of the LDAP server
    static final String serverPort = "1389";
    //LDAP domain
    static final String baseDN = "dc=ensta,dc=fr";
    
    
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
     * @param DN
     * @param password
    ********************/
    //public LDAPConnector(String user, String password, String DN){
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
    ********************/
    public void connect() throws NamingException {

        contexte = new InitialDirContext(environnement);
        
    }
    
    /********************
     * Disconnect from the LDAP server
    ********************/
    public void disconnect() throws NamingException {
        
        contexte.close();
    
    }
    
    /********************
     * Search the DN of a specified user
     * @param uid
     * @return 
     * @throws javax.naming.NamingException
    ********************/
    public String UserDNSearch(String uid) throws NullPointerException, NamingException {
       
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> answer = contexte.search(baseDN, "UID=" + uid.trim(), controls);
        SearchResult sr = answer.next();
        
        return sr.getNameInNamespace();
        
    }
    
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
                    infoTab.put("LASTNAME", value[1]);
                    break;

                    case "givenName":
                    infoTab.put("FIRSTNAME", value[1]);
                    break;
                    
                    case "cn":
                    infoTab.put("COMMONNAME", value[1]);
                    break;

                    case "mail":
                    infoTab.put("EMAIL", value[1]);
                    break;

                    case "phoneNumber":
                    infoTab.put("PHONENUMBER", value[1]);
                    break;
                    
                    case "description":
                        infoTab.put("SECURITYQUESTION", value[1]);
                        break;
                        
                    case "street":
                        infoTab.put("SECURITYANSWER", value[1]);
                        break;
                    
                    case "title":
                        infoTab.put("TOTPSECRET", value[1]);
                        break;
                        
                }
            }
        }
        return infoTab;
    }
    
    public void ModifyLDAPInfo(HashMap newInfo, String DN) {
        
        try
        {

        ModificationItem[] mods = new ModificationItem[newInfo.size() - 1];
        Attribute[] attrMods = new BasicAttribute[newInfo.size() - 1];

        attrMods[0] = new BasicAttribute("userPassword", newInfo.get("PWD"));
        
        int i = 1;

        if (newInfo.containsKey("LASTNAME")) {
            attrMods[i] = new BasicAttribute("sn", newInfo.get("LASTNAME"));
            i++;
        }
        if (newInfo.containsKey("FIRSTNAME")) {
            attrMods[i] = new BasicAttribute("givenName", newInfo.get("FIRSTNAME"));
            i++;
        }
        if (newInfo.containsKey("COMMONNAME")) {
            attrMods[i] = new BasicAttribute("cn", newInfo.get("COMMONNAME"));
            i++;
        }
        if (newInfo.containsKey("EMAIL")) {
            attrMods[i] = new BasicAttribute("mail", newInfo.get("EMAIL"));
            i++;
        }
        if (newInfo.containsKey("PHONENUMBER")) {
            attrMods[i] = new BasicAttribute("telephoneNumber", newInfo.get("PHONENUMBER"));
            i++;
        }
        if (newInfo.containsKey("SECURITYQUESTION")) {
            attrMods[i] = new BasicAttribute("description", newInfo.get("SECURITYQUESTION"));
            i++;
        }
        if (newInfo.containsKey("SECURITYANSWER")) {
            attrMods[i] = new BasicAttribute("street", newInfo.get("SECURITYANSWER"));
            i++;
        }
        if (newInfo.containsKey("TOTPSECRET")) {
            attrMods[i] = new BasicAttribute("title", newInfo.get("TOTPSECRET"));
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
                    case "description":
                        secInfoTab.put("SECURITYQUESTION", value[1]);
                        break;
                        
                    case "street":
                        secInfoTab.put("SECURITYANSWER", value[1]);
                        break;                        
                }
            }
        }
        return secInfoTab;
    }
    
     public DirContext getContexte() {
        return contexte;
    }
    
    /*public void UserRemove(String uidUser){
        String entryDN = "uid="+uidUser.trim()+",ou=Professors,ou=People,dc=ensta,dc=fr"; 
        try {
            this.contexte.destroySubcontext(entryDN);
            System.out.println("Suppression de l'utilisateur : SUCCES");
        } catch (Exception e) {
            System.out.println("Suppression de l'utilisateur : ECHEC");
            e.printStackTrace();
        }
    }*/
    
    /*public void UserAdd(String uidUser)
    {
	try
	{ 
            Attributes add = new BasicAttributes();
            Attribute add0 = new BasicAttribute("givenName", "First Name");
            Attribute add1 = new BasicAttribute("sn", "Last Name");
            Attribute add2 = new BasicAttribute("cn", "Common Name");
            Attribute add3 = new BasicAttribute("telephoneNumber", "phone");
            Attribute add4 = new BasicAttribute("mail", "e-mail");
            Attribute add5 = new BasicAttribute("userPassword", "password");
            Attribute add6 = new BasicAttribute("facsimileTelephoneNumber", "fax");	
            Attribute add7 = new BasicAttribute("objectclass", "inetOrgPerson");	
            Attribute add8 = new BasicAttribute("uid", uidUser.trim());	

            add.put(add0);
            add.put(add1);
            add.put(add2);
            add.put(add3);
            add.put(add4);
            add.put(add5);
            add.put(add6);
            add.put(add7);
            add.put(add8);

            this.contexte.createSubcontext("uid="+uidUser.trim()+",ou=Students,ou=People,dc=ensta,dc=fr", add); 
            System.out.println("Ajout de l'utilisateur : SUCCES");
	}
	catch(Exception e)
	{
            System.out.println("Ajout de l'utilisateur : ECHEC");
            e.printStackTrace();	
	}
    }*/
    
}

