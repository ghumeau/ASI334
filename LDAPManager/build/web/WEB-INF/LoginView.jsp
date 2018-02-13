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
            <div class="LoginLine">
                <input type="text" id="login" name="login" class="loginField" placeholder="login (uid)"/>
                <span class="erreur">${erreurs['login']}</span>
            </div>
            <div class="loginLine">
                <input type="password" id="password" name="password" class="loginField" placeholder="password" />
                <span class="erreur">${erreurs['password']}</span>
            </div>
            <div class="LoginLine">
                <input type="submit" value="connection" class="bouton" id="valider"><br>
                <span class="erreur">${resultat}</span>
                <a class="bouton" href="../uid/" >Mot de passe perdu</a>   
                <a class="bouton" href="../cheat/" >accès direct</a>   
            </div>
        </form>
    </body>
</html>
