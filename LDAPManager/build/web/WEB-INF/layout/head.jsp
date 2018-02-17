<%-- 
    Document   : head
    Created on : 13 févr. 2018, 19:36:44
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <title><c:out value="${param.page}"/></title>
        <link type="text/css" media="screen" rel="stylesheet" href="css/layout.css" />
        <link type="text/css" media="screen" rel="stylesheet" href="css/style.css" />
        <script type="text/javascript" src="scripts/affichage.js"></script>
    </head>