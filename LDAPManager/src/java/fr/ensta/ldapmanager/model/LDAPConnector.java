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
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;


public class LDAPConnector {
    
    private Hashtable environnement;
    private DirContext contexte;
    private Attributes attrs;

    public LDAPConnector(String user, String password){
        
        //Adresse du serveur sur lequel se trouve l'annuaire LDAP
        String serverIP = "localhost";
        //Port du serveur sur lequel se trouve l'annuaire LDAP
        String serverPort = "1389";
        //Login de connexion à l'annuaire LDAP : Le login doit être sous forme de "distinguished name"
        //ce qui signifie qu'il doit être affiché sous la forme de son arborescence LDAP
        String serverLogin = "uid="+ user.trim() +",OU=Professors,OU=People,DC=ensta,DC=fr";
        //Mot de passe de connexion à l'annuaire LDAP
        String serverPass = password;

        //On remplit un tableau avec les parametres d'environement et de connexion au LDAP
        this.environnement = new Hashtable();
        this.environnement.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        this.environnement.put(Context.PROVIDER_URL, "ldap://"+serverIP+":"+serverPort+"/");
        this.environnement.put(Context.SECURITY_AUTHENTICATION, "simple");
        this.environnement.put(Context.SECURITY_PRINCIPAL, serverLogin);
        this.environnement.put(Context.SECURITY_CREDENTIALS, serverPass);
        
        
    }
    
    public DirContext getContexte() {
        return contexte;
    }
   
    public void connect() throws NamingException {

            this.contexte = new InitialDirContext(this.environnement);
        
    }
    
    public void disconnect(){
        
        try {
            this.contexte.close();
            System.out.println("Deconnexion au serveur : SUCCES");
        } catch (NamingException e) {
            System.out.println("Deconnexion au serveur : ECHEC");
            e.printStackTrace();
        }
    
    }
    
    public void UserSearch(String critères){
        
        try {
            //On recupere l'attribut 
            this.attrs = this.contexte.getAttributes(critères);
            System.out.println("Recherche de l'utilisateur : SUCCES");
            System.out.println(this.attrs.get("uid"));
        } catch (NamingException e) {
            System.out.println("Recherche de l'utilisateur : ECHEC");
            e.printStackTrace();
        }
        
    }
    
    public HashMap UserScopeSearch(String uidUser) throws NamingException {
        HashMap infoTab = new HashMap();
	String baseDN = "dc=ensta,dc=fr";
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> answer = this.contexte.search(baseDN, "UID=" + uidUser.trim(), controls);
        
        if (answer.hasMore()) {             
            Attributes attrs = answer.next().getAttributes();
            
            NamingEnumeration e;
            
            e = attrs.getIDs();
            
            while (e.hasMoreElements())
                {
                    String attr = (String) e.nextElement();
                    String [] valeur = attrs.get(attr).toString().split(": ");
                    
                    switch (valeur[0])
                    {
                        case "sn":
                            infoTab.put("LASTNAME", valeur[1]);
                            break;
                            
                        case "givenName":
                            infoTab.put("FIRSTNAME", valeur[1]);
                            break;
                        
                        case "mail":
                            infoTab.put("EMAIL", valeur[1]);
                            break;
                            
                        case "phoneNumber":
                            infoTab.put("PHONENUMBER", valeur[1]);
                            break;
                    }
                }
        }
                
        return infoTab;
    }
    
    public void ModifyLDAPInfo(HashMap newInfo)
    {
    String entryDN = "uid="+newInfo.get("UID").toString().trim()+",ou=Professors,ou=People,dc=ensta,dc=fr";
    try
    {
    
    ModificationItem[] mods = new ModificationItem[newInfo.size() - 1];
    Attribute[] attrMods = new BasicAttribute[newInfo.size() - 1];
    
    attrMods[0] = new BasicAttribute("userPassword", newInfo.get("PWD"));
    
    if (newInfo.containsKey("LASTNAME")) {
        attrMods[1] = new BasicAttribute("sn", newInfo.get("LASTNAME"));  
    }
    if (newInfo.containsKey("FIRSTNAME")) {
        attrMods[2] = new BasicAttribute("givenName", newInfo.get("FIRSTNAME"));
    }
    if (newInfo.containsKey("EMAIL")) {
        attrMods[3] = new BasicAttribute("mail", newInfo.get("EMAIL"));
    }
    if (newInfo.containsKey("PHONENUMBER")) {
        attrMods[4] = new BasicAttribute("telephoneNumber", newInfo.get("PHONENUMBER"));
    }
    
    for (int i=0; i < newInfo.size()-1; i++) {
        mods[i] = new ModificationItem (DirContext.REPLACE_ATTRIBUTE, attrMods[i]);
    }
    
    this.contexte.modifyAttributes(entryDN, mods);
    System.out.println("Modification de l'utilisateur : SUCCES");
    }
    catch(Exception e)
    {
    System.out.println("Modification de l'utilisateur : ECHEC");
    e.printStackTrace();
    }
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

