<%-- 
    Document   : Visu
    Created on : 6 f�vr. 2018, 20:29:33
    Author     : arnaudlegrignou
--%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Affichage des donn�es</title>
        <link type="text/css" media="screen" rel="stylesheet" href="../css/style.css" />
    </head>
    <script type="text/javascript" src="../scripts/affichage.js"></script>
    <body>

        <header><h1>LDAP Manager</h1></header>
        <form method="post" action="private" class="dataForm">
            <h2>Donn�es personnelles</h2>
           
            <script>
                
                for (var iter = 0; iter < ${user}.length; iter++) {
                dataLine("uid","${nom}");
                }
                
                dataLine("uid","${nom}");
                dataLine("nom","${nom}");
                dataLine("prenom","");
                dataLine("email","");
                dataLine("t�l�phone","");                        
            </script>

            <input type="button" class="bouton" id="modifier" class="modify" value ="modifier" onclick="disableTxt()" /> 
            <input type="button" class="bouton" id="annuler" value="Annuler" hidden="true" onclick="undisableTxt()" />
            <input type="button" class="bouton" value="Valider"  />
            <a class="bouton" href="../security/">Modifier les �l�ments de s�curit�</a>
            
        </form>
            
        
        
        </br><a href="../public/"> <input type="button" href="../private/" value="Se deconnecter"></a><br /> 
        
        <footer><h1>LDAP Manager</h1></footer>

    </body>
</html>
