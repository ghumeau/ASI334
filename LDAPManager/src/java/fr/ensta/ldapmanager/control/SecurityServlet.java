/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.control;

import fr.ensta.ldapmanager.model.Services;
import fr.ensta.ldapmanager.model.User;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 *
 * @author arnaudlegrignou
 */
public class SecurityServlet extends HttpServlet {

    public static final String CHAMP_PWD = "pwd";
    public static final String CHAMP_NEWPWD1 = "newpwd";
    public static final String CHAMP_NEWPWD2 = "confirm";
    public static final String CHAMP_QUEST = "question";
    public static final String CHAMP_ANS = "answer";
    public static final String CHAMP_AUTH = "auth";
    public static final String ATT_USER = "user";
    public static final String ATT_AUTH = "authentified";
    public static final String ATT_ERREURS = "erreurs";
    public static final String ATT_RESULTAT = "resultat";
    public static final String ATT_URLQRC = "urlQRcode";
    
    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response )   throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        Boolean auth = (Boolean) session.getAttribute(ATT_AUTH);
        // A l'appel de la servlet (GET), affichage de la page d'authentification si l'utilisateur n'a pas de session active
        if (auth==null){
            this.getServletContext().getRequestDispatcher("/login").forward(request, response);
        }
        else {
            request.setAttribute(ATT_USER, user.GetInfo());
            // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
            this.getServletContext().getRequestDispatcher("/WEB-INF/SecurityView.jsp").forward(request, response);
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        Services svc = new Services();
        Map<String, String> errors = new HashMap<>();
        String result = null;
        String pwd = request.getParameter(CHAMP_PWD);
        String newpwd1 = request.getParameter(CHAMP_NEWPWD1);
        String newpwd2 = request.getParameter(CHAMP_NEWPWD2);
        String question = request.getParameter(CHAMP_QUEST);
        String answer = request.getParameter(CHAMP_ANS);
        boolean auth2 = "oui".equals(request.getParameter(CHAMP_AUTH));
        
        if (!user.getPassword().equals(pwd)){ // vérification du mot de passe
            errors.put(CHAMP_PWD,"Veuillez saisir votre mot de passe");
        }
        else{
            // changement de mot de passe
            if (!Checks.isEmpty(newpwd1)){
                if (!Checks.syntaxe(newpwd1,Checks.Argument.PWD)) {errors.put(CHAMP_NEWPWD1,"Erreur de syntaxe!");}
                else if (!Checks.syntaxe(newpwd2,Checks.Argument.PWD)) {errors.put(CHAMP_NEWPWD2,"Erreur de syntaxe!");}
                else if (!newpwd1.equals(newpwd2)) {errors.put(CHAMP_NEWPWD2,"Mots de passe différents!");}
                else {
                    user.setPassword(newpwd1);
                    result = "Modification prise en compte";
                }
            }
            
            // changement de question/réponse
            if (!Checks.isEmpty(question)){
                if (!Checks.syntaxe(question,Checks.Argument.QUESTION)) {errors.put(CHAMP_QUEST,"Erreur de syntaxe!");}
                else if (Checks.isEmpty(answer)) {errors.put(CHAMP_ANS,"Veuillez saisir une réponse");}
                else if (!Checks.syntaxe(answer,Checks.Argument.PWD)) {errors.put(CHAMP_ANS,"Erreur de syntaxe!");}
                else {
                    user.setSecureQuestion(question);
                    user.setSecureAnswer(answer);
                    result = "Modification prise en compte";
                }
            }
            
            // changement du mode d'authentification
            if (user.isTotpFlag()!=auth2) {
                if (!auth2) {
                    user.setTotpFlag(auth2);
                    user.setTotpSecret("");
                }
                else {
                    user.setTotpFlag(auth2);
                    String url = svc.GenerateTotpKey(user);
                    request.setAttribute(ATT_URLQRC, url);
                }
                result = "Modification prise en compte";
            }
            
            // enregistrement des modifications
            svc.ModifyInfo(user);
            
            // retour à SecurityView avec les éventuelles erreurs
            session.setAttribute(ATT_USER, user);
            request.setAttribute(ATT_USER, user.GetInfo());
            request.setAttribute(ATT_ERREURS, errors);
            request.setAttribute(ATT_RESULTAT, result);
            this.getServletContext().getRequestDispatcher("/WEB-INF/SecurityView.jsp").forward(request, response);
        }
    }

}
