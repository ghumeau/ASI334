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
        <form>
        <h3>Vous avez été deconnecté</h3>
        <a href="../login/">Se connecter</a>
        </form>
    </body>
</html>
