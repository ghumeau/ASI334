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
        <c:param name="page" value="Question de sécurite"/>
    </c:import>
    <body>
        <c:import url="layout/header.jsp">
            <c:param name="title" value="Question de sécurité"/>
        </c:import>
        <form method="post" action="question">
            <script>
                standardLine("Question de sécurité","Question","${securityInfo}");
                securityLine("text","entrez la réponse de sécurité","Answer","${erreurs.answer}");
            </script>
            <input type="submit" class="bouton" value="valider">
      </form>
    </body>
</html>
