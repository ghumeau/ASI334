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
    public static final int maxEchecs = 5;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        // A l'appel de la servlet (GET), affichage de la page UID si l'utilisateur n'a pas de session active
        if (user==null){
            Integer echecs = (Integer) session.getAttribute(ATT_ECHECSQ);
            if (echecs==null){session.setAttribute(ATT_ECHECSQ,0);}
            else if (echecs>=maxEchecs){
                Map<String, String> errors = new HashMap<>();
                errors.put(CHAMP_UID, "Trop d'échec, vous avez été bloqué !!!");
                request.setAttribute(ATT_ERREURS, errors);
            }
            this.getServletContext().getRequestDispatcher("/WEB-INF/UIDView.jsp").forward(request, response);
        }
        else {
            request.setAttribute(ATT_USER, user.GetInfo());
            // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
            this.getServletContext().getRequestDispatcher("/WEB-INF/DATAView.jsp").forward(request, response);
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String uid = request.getParameter(CHAMP_UID);
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
        
        // Récupération de la question / réponse
        if (errors.isEmpty()) {
            Services svc = new Services();
            HashMap securityInfo = svc.RetrieveSecurityInfo(uid);
            if (securityInfo.isEmpty()){errors.put(CHAMP_UID,"UID inconnu, tentatives restantes : " + (maxEchecs-echecs));}
            else {
                session.setAttribute(ATT_QUEST, securityInfo); // stockage de la question/réponse en session
                session.setAttribute(ATT_UID, uid);            // stockage de l'uid
                session.setAttribute(ATT_ECHECSQ,0);
                request.setAttribute(ATT_QUEST, securityInfo.get("SECURITYQUESTION"));
                this.getServletContext().getRequestDispatcher("/WEB-INF/QuestionView.jsp").forward(request, response);
            }
        }
        
        echecs++;
        session.setAttribute(ATT_ECHECSQ,echecs);
        // Stockage des messages d'erreur dans l'objet request
        request.setAttribute(ATT_ERREURS, errors);
        // Transmission de la paire d'objets request/response à notre JSP
        this.getServletContext().getRequestDispatcher("/WEB-INF/UIDView.jsp").forward(request, response);
    }

}
