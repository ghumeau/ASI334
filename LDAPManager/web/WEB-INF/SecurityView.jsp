<%-- 
    Document   : SecurityView
    Created on : 13 févr. 2018, 09:19:30
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <c:import url="layout/head.jsp">
        <c:param name="page" value="Données de sécurité"/>
    </c:import>
    <body>
        <c:import url="layout/header.jsp">
            <c:param name="title" value="Sécurité"/>
        </c:import>
        <form method="post" action="security" ID="securityForm">
            <script>
                securityLine("password","Saisir l'ancien mot de passe","pwd","${erreurs.pwd}");
                securityLine("password","Saisir le nouveau mot de passe","newpwd","${erreurs.newpwd}");
                securityLine("password","Confirmation du nouveau mot de passe","confirm","${erreurs.confirm}");
                securityLine("text","Question de sécurité","question","${erreurs.question}");
                securityLine("text","réponse de sécurité","answer","${erreurs.answer}");
                securityLine("checkbox","Double Authentification","double","");
            </script>
            <input type="submit" class="bouton" value="Valider"  /><br><br>
            <a class="link" id="returnButton"   href="private">retour aux données</a>
            
            <div id="QRCode">
                    ${urlQRcode}
            </div>
        </form>
    </body>
</html>
