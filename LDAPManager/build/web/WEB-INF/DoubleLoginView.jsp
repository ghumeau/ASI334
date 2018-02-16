<%-- 
    Document   : DoubleLoginView
    Created on : 12 févr. 2018, 19:42:54
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    
    <c:import url="layout/head.jsp"/>
    <body>
        <c:import url="layout/header.jsp"/>
        <form method="post" action="question">
            <h3>Doube authentification - code Authenticator</h3>
            <script>
                securityLine("text","entrez le code Authenticator","Authenticator","${erreurs.code}");
            </script>
            <input type="submit" class="bouton" value="valider">
            <a class="bouton" href="uid" >Mot de passe perdu</a><br>  
      </form>
    </body>
</html>
