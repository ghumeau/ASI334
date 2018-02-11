<%-- 
    Document   : index
    Created on : 6 févr. 2018, 10:00:41
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Connexion</title>
        <link type="text/css" media="screen" rel="stylesheet" href="../css/style.css" />
    </head>


    <body>
        <header><h1>LDAP Manager</h1></header>
        
        
        
        <form method="post" class="loginForm" action="">
                <h2>Authentification</h2>
                <div>
                    <label for="login">Login (UID)</label><span>: </span><br>
                    <input type="text" id="login" name="login" class="loginForm"/>
                    <span class="erreur">${erreurs['login']}</span>
                </div>
                <div>
                    <label for="password">Mot de passe</label><span>: </span>
                    <input type="password" id="password" name="password" class="loginForm"  />
                    <span class="erreur">${erreurs['password']}</span>
                </div>
                <div>
                    <input type="submit" value="connection" class="valider">
                    
                    <p>${resultat}</p>
                </div>
            </form>
            <input type="button" value="Mot de passe perdu">
            <a href="../private/"> <input type="button" href="../private/" value="accès direct"></a>
        
    </body>
</html>
