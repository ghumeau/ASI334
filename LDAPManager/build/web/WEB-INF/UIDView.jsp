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
    <body>
        <c:import url="layout/header.jsp"/>
        <form method="post" action="uid">
            <h3>Saisie de l'identifiant (UID)</h3>
            <script>
                securityLine("text","entrez votre identifiant","uid","${errors.uid}");               
            </script>
            <input type="submit" class="bouton" value="valider"> 
      </form>
    </body>
</html>
