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
        <form method="post" action="security" ID="securityForm">
            <h3>Données de sécurité</h3>
            <script>
                securityLine("password","Saisir l'ancien mot de passe","pwd","${errors.pwd}");
                securityLine("password","Saisir le nouveau mot de passe","newpwd","${errors.newpwd}");
                securityLine("password","Confirmation du nouveau mot de passe","confirm","${errors.confirm}");
                securityLine("text","Question de sécurité","question","${errors.question}");
                securityLine("text","réponse de sécurité","answer","${errors.answer}");
                securityLine("checkbox","Double Authentification","double","");
            </script>
            <input type="submit" class="bouton" value="Valider"  />
            <a class="retour" id="returnButton"   href="private">retour aux données</a>
        </form>
    </body>
</html>
