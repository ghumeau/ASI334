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
            <script>
                securityLine("pass","entrez votre nouveau mot de passe");
                securityLine("pass","confirmez votre mot de passe");    
            </script>
            <a class="bouton" href="../login/" >Valider</a>  
      </form>
    </body>
</html>