<%-- 
    Document   : LogoutView
    Created on : 7 févr. 2018, 22:59:36
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    
    <c:import url="layout/head.jsp"/>
    <header>LDAP Manager</header>
    <body>
        <c:import url="layout/header.jsp"/>
        <form method="post" action="new">
            <script>
                securityLine("pass","entrez votre nouveau mot de passe");
                securityLine("pass","confirmez votre mot de passe");    
            </script>
            <a class="bouton" href="login" >Valider</a>  
      </form>
    </body>
</html>