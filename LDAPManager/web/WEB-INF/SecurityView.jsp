<%-- 
    Document   : SecurityView
    Created on : 13 févr. 2018, 09:19:30
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
    <body>
        <header><h1>Données de sécurité</h1></header>
        
        </form>
        
        <form method="post" action="private" ID="securityForm">
            <h2>Données de sécurité</h2>
           
            <script>
                securityLine("pwd","Saisir l'ancien mot de passe");
                securityLine("newpwd","Saisir le nouveau mot de passe");
                securityLine("confirm","confirmation du nouveaumot de passe");
                securityLine("question","question de sécurité");
                securityLine("answer", "réponse de sécurité");
            </script>

            <input type="button" class="bouton" value="Valider"  />

            
            
        </form>
    </body>
</html>
