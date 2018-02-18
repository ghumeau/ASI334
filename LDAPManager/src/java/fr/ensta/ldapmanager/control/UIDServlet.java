package fr.ensta.ldapmanager.control;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import fr.ensta.ldapmanager.model.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author arnaudlegrignou
 */
@WebServlet(urlPatterns = {"/UIDView"})
public class UIDServlet extends HttpServlet {

    public static final String CHAMP_UID = "uid";
    public static final String ATT_ERREURS = "erreurs";
    public static final String ATT_QUEST = "securityInfo";
    public static final String ATT_ECHECSQ = "echecsQuestion";
    public static final String ATT_USER = "user";
    public static final String ATT_UID = "uid";
    public static final String ATT_AUTH = "authentified";
    public static final int maxEchecs = 5;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        Boolean auth = (Boolean) session.getAttribute(ATT_AUTH);
        // A l'appel de la servlet (GET), affichage de la page UID si l'utilisateur n'a pas de session active
        if (auth==null){
            Integer echecs = (Integer) session.getAttribute(ATT_ECHECSQ);
            if (echecs==null){session.setAttribute(ATT_ECHECSQ,0);}
            else if (echecs>=maxEchecs){
                Map<String, String> errors = new HashMap<>();
                errors.put(CHAMP_UID, "Trop d'échec, vous avez été bloqué !!!");
                request.setAttribute(ATT_ERREURS, errors);
            }
            this.getServletContext().getRequestDispatcher("/WEB-INF/UIDView.jsp").forward(request, response);
        }
        else {this.getServletContext().getRequestDispatcher("/private").forward(request, response);} // si déjà authentifié, transfert sur la page data
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String uid = request.getParameter(CHAMP_UID);
        HashMap<String,String> securityInfo = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        Integer echecs = (Integer) session.getAttribute(ATT_ECHECSQ);
        
        if (echecs==null){echecs=0;}
        if (echecs>=maxEchecs){
            errors.put(CHAMP_UID, "Trop d'échec, vous avez été bloqué !!!");
            request.setAttribute(ATT_ERREURS, errors);
            this.getServletContext().getRequestDispatcher("/WEB-INF/UIDView.jsp").forward(request, response);
        }
        
        // Verification de la syntaxe de l'UID.
        if (Checks.isEmpty(uid)){errors.put(CHAMP_UID,"Veuillez saisir votre UID.");}
        else if (!Checks.syntaxe(uid,Checks.Argument.UID)){errors.put(CHAMP_UID,"Veuillez saisir un UID valide.");}
        
        // si UID valide, tentative de récupération de la question / réponse
        if (errors.isEmpty()) {
            Services svc = new Services();
            securityInfo = svc.RetrieveSecurityInfo(uid);
            if (securityInfo.isEmpty()) {
                echecs++;
                session.setAttribute(ATT_ECHECSQ,echecs);
                errors.put(CHAMP_UID,"Récupération impossible avec cet UID");
            }
        }
        
        if (!errors.isEmpty()) {
            // Stockage des messages d'erreur dans l'objet request
            request.setAttribute(ATT_ERREURS, errors);
            this.getServletContext().getRequestDispatcher("/WEB-INF/UIDView.jsp").forward(request, response);
        }
        else {
                session.setAttribute(ATT_QUEST, securityInfo); // stockage de la question/réponse en session
                session.setAttribute(ATT_UID, uid);            // stockage de l'uid
                session.setAttribute(ATT_ECHECSQ, 0);
                request.setAttribute(ATT_QUEST, securityInfo.get("securityQuestion"));
                this.getServletContext().getRequestDispatcher("/WEB-INF/QuestionView.jsp").forward(request, response);
            }
        
        
    }

}
