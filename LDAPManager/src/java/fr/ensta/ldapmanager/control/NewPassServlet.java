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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author arnaudlegrignou
 */
@WebServlet(name = "NewPassServlet", urlPatterns = {"/NewPassServlet"})
public class NewPassServlet extends HttpServlet {
    
    public static final String CHAMP_NEWPWD1 = "pass";
    public static final String CHAMP_NEWPWD2 = "confirm";
    public static final String ATT_USER = "user";
    public static final String ATT_AUTH = "authentified";
    public static final String ATT_ERREURS = "erreurs";
    public static final String ATT_UID = "uid";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        Boolean auth = (Boolean) session.getAttribute(ATT_AUTH);
        // A l'appel de la servlet (GET), affichage de la page UID si l'utilisateur n'a pas de session active
        if (auth==null){
            this.getServletContext().getRequestDispatcher("/WEB-INF/NewPassView.jsp").forward(request, response);
        }
        else {this.getServletContext().getRequestDispatcher("/private").forward(request, response);} // si déjà authentifié, transfert sur la page data
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Services svc = new Services();
        String uid = (String) session.getAttribute(ATT_UID);
        Map<String, String> errors = new HashMap<>();
        String newpwd1 = request.getParameter(CHAMP_NEWPWD1);
        String newpwd2 = request.getParameter(CHAMP_NEWPWD2);
        
        Boolean auth = (Boolean) session.getAttribute(ATT_AUTH);
        // A l'appel de la servlet, affichage de la page d'authentification si l'utilisateur n'a pas de session active
        if (uid==null){
            this.getServletContext().getRequestDispatcher("/login").forward(request, response);
        }
        
        // changement de mot de passe
        if (Checks.isEmpty(newpwd1)) {errors.put(CHAMP_NEWPWD1,"Veuillez saisir un nouveau mot de passe.");}
        else {
            if (!Checks.syntaxe(newpwd1,Checks.Argument.PWD)) {errors.put(CHAMP_NEWPWD1,"Erreur de syntaxe!");}
            else if (!Checks.syntaxe(newpwd2,Checks.Argument.PWD)) {errors.put(CHAMP_NEWPWD2,"Erreur de syntaxe!");}
            else if (!newpwd1.equals(newpwd2)) {errors.put(CHAMP_NEWPWD2,"Mots de passe différents!");}
        }
            
        if (errors.isEmpty()) {
            // enregistrement du nouveau mot de passe
            User user = new User(uid,newpwd1);
            svc.ModifyPassword(user);
            User authUser = svc.AuthenticationSequence(uid, newpwd1);
            session.setAttribute(ATT_USER, authUser);
            session.setAttribute(ATT_AUTH, true);
            request.setAttribute(ATT_USER, authUser.GetInfo());
            this.getServletContext().getRequestDispatcher("/WEB-INF/DataView.jsp").forward(request, response);
        }
        else {
            // retour avec les erreurs
            request.setAttribute(ATT_ERREURS, errors);
            this.getServletContext().getRequestDispatcher("/WEB-INF/NewPassView.jsp").forward(request, response);
        }
    }

}
