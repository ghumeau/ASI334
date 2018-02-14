<%-- 
    Document   : LogoutView
    Created on : 7 févr. 2018, 22:59:36
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link type="text/css" media="screen" rel="stylesheet" href="../css/style.css" />
    </head>
    <script type="text/javascript" src="../scripts/affichage.js"></script>
    <header>LDAP Manager</header>
    <body>
        <form>
            <h2>Question / réponse de sécurité</h2>
            <p>Question de sécurité</p>
            <script>
                securityLine("Answer","entrez la réponse de sécurité");
            </script>
            <input type="submit" class="bouton" value="valider">
        
      </form>
    </body>
</html>
