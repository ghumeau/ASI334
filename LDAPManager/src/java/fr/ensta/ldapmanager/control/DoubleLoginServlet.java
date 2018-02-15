package fr.ensta.ldapmanager.control;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
@WebServlet(name = "DoubleLoginServlet", urlPatterns = {"/DoubleLoginServlet"})
public class DoubleLoginServlet extends HttpServlet {

    public static final String CHAMP_CODE = "code";
    public static final String ATT_ERREURS = "erreurs";
    public static final String ATT_ECHECS = "echecs";
    public static final String ATT_USER = "user";
    public static final String ATT_AUTH = "authentified";
    public static final int maxEchecs = 5;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        Boolean auth = (Boolean) session.getAttribute(ATT_AUTH);
        // A l'appel de la servlet (GET), affichage de la page UID si l'utilisateur n'a pas de session active
        if (auth==null){
            Integer echecs = (Integer) session.getAttribute(ATT_ECHECS);
            if (echecs==null){session.setAttribute(ATT_ECHECS,0);}
            else if (echecs>=maxEchecs){
                Map<String, String> errors = new HashMap<>();
                errors.put(CHAMP_CODE, "Trop d'échec, vous avez été bloqué !!!");
                request.setAttribute(ATT_ERREURS, errors);
            }
            this.getServletContext().getRequestDispatcher("/WEB-INF/DoubleLoginView.jsp").forward(request, response);
        }
        else {this.getServletContext().getRequestDispatcher("/private").forward(request, response);} // si déjà authentifié, transfert sur la page data
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Services svc = new Services();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        Integer echecs = (Integer) session.getAttribute(ATT_ECHECS);
        String result = null;
        Map<String, String> errors = new HashMap<>();
        //Récupération des champs du formulaire.
        String code = request.getParameter(CHAMP_CODE);
        
        if (Checks.isEmpty(code)) {errors.put(CHAMP_CODE,"Veuillez saisir un code.");}
        else if (!Checks.syntaxe(code, Checks.Argument.CODE)) {errors.put(CHAMP_CODE,"Syntaxe invalide");}
        else if (!svc.CheckTotpCode(user, code)) {errors.put(CHAMP_CODE,"Code incorrect.");}
        
        if (errors.isEmpty()) {
            session.setAttribute(ATT_AUTH, true);
            request.setAttribute(ATT_USER, user.GetInfo());
            // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
            this.getServletContext().getRequestDispatcher("/WEB-INF/DataView.jsp").forward(request, response);
        }
        else {
            echecs++;
            session.setAttribute(ATT_ECHECS,echecs);
            // Stockage du résultat et des messages d'erreur dans la requête
            request.setAttribute(ATT_ERREURS, errors);
            this.getServletContext().getRequestDispatcher("/WEB-INF/DoubleLoginView.jsp").forward(request, response);
        }
    }
    
}
