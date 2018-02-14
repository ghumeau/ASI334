<%-- 
    Document   : Visu
    Created on : 6 févr. 2018, 20:29:33
    Author     : arnaudlegrignou
--%>


<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>



<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Affichage des données</title>
        <link type="text/css" media="screen" rel="stylesheet" href="../css/style.css" />
         <script type="text/javascript" src="../scripts/affichage.js"></script>
    </head>
    <body>

        <header><h1>LDAP Manager</h1></header>
        <form method="post" action="private" class="dataForm">
            <h2>Données personnelles</h2>
            
            <div>
                <span class="dataLabel">UID : </span><span class="data">${user.uid}</span>
                <input type="text" id="uid" name="uid" class="dataField" placeholder="${user.uid}"/> 
            </div>
            <div>
                <span class="dataLabel">Nom : </span><span class="data">${user.lastName}</span>
                <input type="text" id="uid" name="uid" class="dataField" placeholder="${user.lastName}"/> 
            </div>
            <div>
                <span class="dataLabel">prénom : </span><span class="data">${user.lastName}</span>
                <input type="text" id="uid" name="uid" class="dataField" placeholder="${user.lastName}"/> 
            </div>
            <div>
                <span class="dataLabel">Email : </span><span class="data">${user.lastName}</span>
                <input type="text" id="uid" name="uid" class="dataField" placeholder="${user.lastName}"/> 
            </div>
            <div>
                <span class="dataLabel">Email : </span><span class="data">${user.lastName}</span>
                <input type="text" id="uid" name="uid" class="dataField" placeholder="${user.lastName}"/> 
            </div>
          
         
            
            <script>
                
            
                
                dataLine("UID","${nom}");
                dataLine("nom","${nom}");
                dataLine("prenom","");
                dataLine("email","");
                dataLine("téléphone","");                        
            </script>

            <input type="button" class="bouton" id="modifier" class="modify" value ="modifier" onclick="disableTxt()" /> 
            <input type="button" class="bouton" id="annuler" value="Annuler" hidden="true" onclick="undisableTxt()" />
            <input type="button" class="bouton" value="Valider"  />
            <a class="bouton" href="../security/">Modifier les éléments de sécurité</a>
            
        </form>
            
        
        
        </br><a href="../public/"> <input type="button" href="../private/" value="Se deconnecter"></a><br /> 
        
        <footer><h1>LDAP Manager</h1></footer>

    </body>
</html>
