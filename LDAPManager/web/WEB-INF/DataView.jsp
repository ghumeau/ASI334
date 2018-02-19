<%-- 
    Document   : Visu
    Created on : 6 févr. 2018, 20:29:33
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

    <c:import url="layout/head.jsp">
        <c:param name="page" value="Données personnelles"/>
    </c:import>
    <body>
        <c:import url="layout/header.jsp">
            <c:param name="title" value="Données personnelles"/>
        </c:import>

        <form method="post" action="private" class="dataForm">
            <div class="IDdata">
                <script>
                    standardLine("UID", "uid", "${user.uid}");
                    standardLine("Nom ", "nom", "${user.lastName}");
                    standardLine("Prénom", "prenom", "${user.firstName}");
                </script></div>

            <div class="Alterabledata">
                <script>
                    dataLine("Email", "mail", "${user.eMail}", "${erreurs.mail}");
                    dataLine("Téléphone", "tel", "${user.phoneNumber}", "${erreurs.tel}");
                </script>
            </div><br>
            <div class="buttonLine">
                <input type="button" class="allModify" id="modifier"value ="Tout modifier"  onclick="allModify()" /> 
                <input type="submit" class="validate" id="valider" value="" hidden="true" onclick="allvalidate()" />
                <input type="button" class="cancel" id="annuler" value="" hidden="true" onclick="allCancel()" />
            </div>
            <div class="buttonLine">
                <a href="security" ><input type="button" value="Informations de sécurité" class="security" id="securiteButton"/></a>
            </div>
            <div class="buttonLine">
                <a href="public" ><input type="button" value="Se deconnecter" class="logout" id="logoutButton"/></a>
            </div>
        </form>

    </body>
</html>
