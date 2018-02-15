<%-- 
    Document   : Visu
    Created on : 6 févr. 2018, 20:29:33
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

    <c:import url="layout/head.jsp"/>
    <body>
        <c:import url="layout/header.jsp"/>
        <form method="post" action="private" class="dataForm">
            <h3>Données personnelles</h3>
            <h3>UID = ${user.uid}</h3>
            
            <script>
                dataLine("Nom ","nom","${user.lastName}");
                dataLine("Prénom","prenom","${user.firstName}");
                dataLine("Email","mail","${user.email}");
                dataLine("Téléphone","tel","${user.phoneNumber}");
            </script>
            
            <input type="button" class="bouton" id="modifier"value ="tout modifier"  onclick="allModify()" /> 
            <input type="submit" class="bouton" id="valider" value="Valider" hidden="true" onclick="allvalidate()" />
            <input type="button" class="bouton" id="annuler" value="Annuler" hidden="true" onclick="allCancel()" />
            
            <br><a class="bouton" id='securityButton' href="security">Modifier les éléments de sécurité</a>
            <br><a class="logout" id="logoutButton"   href="public">Se deconnecter</a>
            
        </form>
        
    </body>
</html>
