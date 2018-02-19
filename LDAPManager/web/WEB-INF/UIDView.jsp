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
        <c:param name="page" value="uid"/>
    </c:import>
    <body>
        <c:import url="layout/header.jsp">
            <c:param name="title" value="Saisie de l'identifiant"/>
        </c:import>
        <form method="post" action="uid">
            <script>
                securityLine("text","entrez votre identifiant","uid","${erreurs.uid}");               
            </script>
            <input type="submit" class="log" value=""> 
      </form>
    </body>
</html>
