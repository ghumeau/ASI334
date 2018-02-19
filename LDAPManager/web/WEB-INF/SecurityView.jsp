<%-- 
    Document   : SecurityView
    Created on : 13 févr. 2018, 09:19:30
    Author     : arnaudlegrignou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <c:import url="layout/head.jsp">
        <c:param name="page" value="Données de sécurité"/>
    </c:import>
    <body>
        <c:import url="layout/header.jsp">
            <c:param name="title" value="Sécurité"/>
        </c:import>
        <p>${urlQRcode}</p>
        <form method="post" action="security" ID="securityForm">
            <script>
                securityLine("password", "Ancien mot de passe (obligatoire)", "pwd", "${erreurs.pwd}");
                securityLine("password", "Nouveau mot de passe", "newpwd", "${erreurs.newpwd}");
                securityLine("password", "Confirmation du mot de passe", "confirm", "${erreurs.confirm}");
                securityLine("text", "Question de sécurité", "question", "${erreurs.question}");
                securityLine("text", "réponse de sécurité", "answer", "${erreurs.answer}");
            </script>


            <div class='securityLine'>
                <span class='securityLabel' for='proposition'>Gérer la double authentification</span>
                <span class="dataColon" id="QRColon">:</span>
                <c:choose>
                    <c:when test="${user.totpFlag == 'FALSE'}">
                        <input type="submit" class="QRcode" name="Qcode" value="Generer un QR code" />
                    </c:when>
                    <c:when test="${user.totpFlag == 'TRUE'}">  
                        <input type= "submit" class="QRcode" name="Qcode2" value="Désactiver la double authentification" />
                    </c:when>
                </c:choose>
            </div> 
            <div class='securityLine'>
                <span class='securityLabel' for='proposition'>Etat de la double Authentification</span>
                <span class="dataColon" id="QRColon">:</span>
                <c:choose>
                    <c:when test="${user.totpFlag == 'FALSE'}">
                        <span class="data">Désactivée</span>
                    </c:when>
                    <c:when test="${user.totpFlag == 'TRUE'}">  
                        <span class="data">Activée</span>
                    </c:when>
                </c:choose>
            </div>
            <input type="submit" class="log" value=""  />
            <a href="private" ><input type="button" value="" class="back" id="backData"/></a>
            <span class="erreur">${resultat}</span><br> 
        </form>
        
        
        <c:if test="${not empty urlQRcode}">
            <div class="QRcontainer">
                <img src="${urlQRcode}" class="QRframe"/>
            </div>
        </c:if>
    </body>
</html>
