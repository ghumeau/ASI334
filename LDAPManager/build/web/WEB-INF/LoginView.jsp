<%-- 
    Document   : index
    Created on : 6 févr. 2018, 10:00:41
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <c:import url="layout/head.jsp"/>
    <body>
        <c:import url="layout/header.jsp">
            <c:param name="title" value="Authentification"/>
        </c:import>
        <form method="post" class="loginForm" action="login">
            <div class="loginLine">
                <input type="text" id="login" name="login" class="loginField" placeholder="login (uid)"/>
                <span class="erreur">${erreurs.login}</span>
            </div>
            <div class="loginLine">
                <input type="password" id="password" name="password" class="loginField" placeholder="password" />
                <span class="erreur">${erreurs.password}</span>
            </div>
            <div class="LoginLine">
                <input type="submit" value="connection" class="bouton" id="valider">
                <span class="erreur">${resultat}</span><br>
                <a class="link" href="uid" >Mot de passe perdu</a><br>   
                <a class="link" href="../cheat" >accès direct</a>   
            </div>
        </form>
    </body>
</html>
