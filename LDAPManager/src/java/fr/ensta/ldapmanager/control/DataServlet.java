/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.control;

import fr.ensta.ldapmanager.model.*;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author arnaudlegrignou
 */
@WebServlet(name = "DataServlet", urlPatterns = {"/DataServlet"})
public class DataServlet extends HttpServlet {
    
    public static final String CHAMP_MAIL = "mail";
    public static final String CHAMP_PHONE = "tel";
    public static final String ATT_USER = "user";
    public static final String ATT_AUTH = "authentified";
    public static final String ATT_ERREURS = "erreurs";

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
            this.getServletContext().getRequestDispatcher("/WEB-INF/DataView.jsp").forward(request, response);
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        Services svc = new Services(user);
        boolean modified = false;
        Map<String, String> errors = new HashMap<>();
        String mail = request.getParameter(CHAMP_MAIL);
        String phone = request.getParameter(CHAMP_PHONE);
        
        Boolean auth = (Boolean) session.getAttribute(ATT_AUTH);
        // A l'appel de la servlet, affichage de la page d'authentification si l'utilisateur n'a pas de session active
        if (auth==null){
            this.getServletContext().getRequestDispatcher("/login").forward(request, response);
        }
        
        // tests de la validité des champs et modification en l'absence d'erreurs
        if (!Checks.syntaxe(mail,Checks.Argument.MAIL) && !Checks.isEmpty(mail)) {errors.put(CHAMP_MAIL,"Erreur de syntaxe!");}
        else if (!mail.equals(user.getEmail())) {
            user.setEmail(mail);
            errors.put(CHAMP_MAIL,"Adresse mail modifiée");
            modified = true;
        }
        if (!Checks.syntaxe(phone,Checks.Argument.PHONE)  && !Checks.isEmpty(phone)) {errors.put(CHAMP_PHONE,"Erreur de syntaxe!");}
        else if (!phone.equals(user.getPhoneNumber())) {
            user.setPhoneNumber(phone);
            errors.put(CHAMP_PHONE,"Numéro de téléphone modifié");
            modified = true;
        }
        
        // enregistrement des modifications le cas échéant
        if (modified) {svc.ModifyInfo(user);}
        
        // retour à DataView avec les éventuelles erreurs
        //session.setAttribute(ATT_USER, user);
        request.setAttribute(ATT_USER, user.GetInfo());
        request.setAttribute(ATT_ERREURS, errors);
        this.getServletContext().getRequestDispatcher("/WEB-INF/DataView.jsp").forward(request, response);
    }

}
