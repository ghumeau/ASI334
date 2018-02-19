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
        <c:param name="page" value="Deconnection"/>
    </c:import>
    <body> 
        <c:import url="layout/header.jsp">
            <c:param name="title" value="Deconnection"/>
        </c:import>
        <form>
        <p>Vous avez été deconnecté</p>
          <a href="login" ><input type="button" value="Se connecter" class="login" id="loginButton"/>
        </form>
    </body>
</html>
