<%-- 
    Document   : SecurityView
    Created on : 13 févr. 2018, 09:19:30
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <c:import url="layout/head.jsp"/>
    <body>
        <c:import url="layout/header.jsp"/>   
        <form method="post" action="private" ID="securityForm">
            <h3>Données de sécurité</h3>
            <script>
                securityLine("pwd","Saisir l'ancien mot de passe");
                securityLine("newpwd","Saisir le nouveau mot de passe");
                securityLine("confirm","confirmation du nouveau mot de passe");
                securityLine("question","question de sécurité");
                securityLine("answer", "réponse de sécurité");
            </script>
            <input type="submit" class="bouton" value="Valider"  />
        </form>
    </body>
</html>
