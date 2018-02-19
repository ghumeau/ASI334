<%-- 
    Document   : LogoutView
    Created on : 7 févr. 2018, 22:59:36
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    
    <c:import url="layout/head.jsp">
        <c:param name="page" value="Nouveau mot de passe"/>
    </c:import>
    <body>
        <c:import url="layout/header.jsp">
            <c:param name="title" value="Nouveau mot de passe"/>
        </c:import>
        <form method="post" action="new">
            <script>
                securityLine("password","entrez votre nouveau mot de passe","pass","${erreurs.pass}");
                securityLine("password","confirmez votre mot de passe","confirm","${erreurs.confirm}");    
            </script>
            <input type="submit" class="log" id="Valider"  value=""</a>  
      </form>
    </body>
</html>