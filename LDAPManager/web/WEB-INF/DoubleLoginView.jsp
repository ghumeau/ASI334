<%-- 
    Document   : DoubleLoginView
    Created on : 12 févr. 2018, 19:42:54
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    
    <c:import url="layout/head.jsp">
        <c:param name="page" value="Double Authentification"/>
    </c:import>
    <body>
        <c:import url="layout/header.jsp">
            <c:param name="title" value="Code Authenticator"/>
        </c:import>
        <form method="post" action="question">
            <script>
                securityLine("text","entrez le code Authenticator","Authenticator","${erreurs.code}");
            </script>
            <input type="submit" class="bouton" value="valider">
            <a class="bouton" href="uid" >Mot de passe perdu</a><br>  
      </form>
    </body>
</html>
