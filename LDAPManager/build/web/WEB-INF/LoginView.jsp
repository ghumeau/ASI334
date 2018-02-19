<%-- 
    Document   : index
    Created on : 6 févr. 2018, 10:00:41
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <c:import url="layout/head.jsp">
        <c:param name="page" value="Authentification"/>
    </c:import>
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
                <input type="submit" value="" class="log" id="validerLogin"/>
                <a href="uid" ><input type="button" value="" class="forget" id="forgetPassword"/></a><br>
                <span class="erreur">${resultat}</span><br> 
            </div>
                 
                <a class="link" href="../cheat" >accès direct</a>   
        </form>
        
    </body>
</html>
