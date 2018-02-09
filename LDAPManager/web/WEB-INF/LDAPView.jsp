<%-- 
    Document   : Visu
    Created on : 6 févr. 2018, 20:29:33
    Author     : arnaudlegrignou
--%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Affichage des données</title>
        <link type="text/css" media="screen" rel="stylesheet" href="../css/style.css" />
    </head>
    <script type="text/javascript" src="../scripts/affichage.js"></script>
    <body>

        <header><h1>LDAP Manager</h1></header>
        <form method="get" action="private">
            <h2>Gestion des données</h2>
           
            <script>
                ligne("uid","${nom}");
                ligne("nom","${nom}");
                ligne("prenom","");
                ligne("email","");
                ligne("téléphone","");
                
                
            </script>

            <input type="button" id="modifier" class="modify" value ="modifier" onclick="disableTxt()" /> 
            <input type="button" id="annuler" value="Annuler" hidden="true" onclick="undisableTxt()" />
            <input type="button" value="Valider"  />
            <input type="button" value="Modifier les éléments de sécurité"  />
            
            
        </form>
        
        </br><a href="../public/"> <input type="button" href="../private/" value="Se deconnecter"></a><br /> 
        
        

    </body>
</html>
