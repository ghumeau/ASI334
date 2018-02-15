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
        <form method="post" action="question">
            <h3>Question / réponse de sécurité</h3>
            <script>
                standardine("Question de sécurité","Question","${securityInfo}");
                securityLine("text","entrez la réponse de sécurité","Answer","${erreurs.answer}");
            </script>
            <input type="submit" class="bouton" value="valider">
      </form>
    </body>
</html>
