package fr.ensta.ldapmanager.control;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static fr.ensta.ldapmanager.control.UIDServlet.ATT_ECHECSQ;
import static fr.ensta.ldapmanager.control.UIDServlet.ATT_ERREURS;
import static fr.ensta.ldapmanager.control.UIDServlet.ATT_QUEST;
import static fr.ensta.ldapmanager.control.UIDServlet.ATT_USER;
import static fr.ensta.ldapmanager.control.UIDServlet.maxEchecs;
import fr.ensta.ldapmanager.model.Services;
import fr.ensta.ldapmanager.model.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author arnaudlegrignou
 */
@WebServlet(urlPatterns = {"/QuestionView"})
public class QuestionServlet extends HttpServlet {
    
    public static final String CHAMP_ANSWER = "answer";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        // A l'appel de la servlet (GET), affichage de la page UID si l'utilisateur n'a pas de session active
        if (user==null){
            Integer echecs = (Integer) session.getAttribute(ATT_ECHECSQ);
            if (echecs==null){session.setAttribute(ATT_ECHECSQ,0);}
            else if (echecs>=maxEchecs){
                Map<String, String> errors = new HashMap<>();
                errors.put(CHAMP_ANSWER, "Trop d'échec, vous avez été bloqué !!!");
                request.setAttribute(ATT_ERREURS, errors);
            }
            this.getServletContext().getRequestDispatcher("/WEB-INF/QuestionView.jsp").forward(request, response);
        }
        else {this.getServletContext().getRequestDispatcher("/private").forward(request, response);} // si déjà authentifié, transfert sur la page data
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String answer = request.getParameter(CHAMP_ANSWER);
        Map<String, String> errors = new HashMap<>();
        HashMap<String, String> securityInfo = new HashMap<>();
        Integer echecs = (Integer) session.getAttribute(ATT_ECHECSQ);
        
        if (echecs==null){echecs=0;}
        if (echecs>=maxEchecs){
            errors.put(CHAMP_ANSWER, "Trop d'échec, vous avez été bloqué !!!");
            request.setAttribute(ATT_ERREURS, errors);
            this.getServletContext().getRequestDispatcher("/WEB-INF/QuestionView.jsp").forward(request, response);
        }
        
        // Verification de la syntaxe de la réponse.
        if (Checks.isEmpty(answer)){errors.put(CHAMP_ANSWER,"Veuillez saisir une réponse.");}
        else if (!Checks.syntaxe(answer,Checks.Argument.ANSWER)){errors.put(CHAMP_ANSWER,"Veuillez saisir une réponse valide.");}
        
        // Récupération de la question / réponse
        if (errors.isEmpty()) {
            securityInfo = (HashMap) session.getAttribute(ATT_QUEST);
            if (!securityInfo.get("SECURITYQUESTION").equals(answer)){errors.put(CHAMP_ANSWER,"Réponse erronée, tentatives restantes : " + (maxEchecs-echecs));}
            else { // réponse correcte
                session.removeAttribute(ATT_QUEST);
                this.getServletContext().getRequestDispatcher("/WEB-INF/NewPassView.jsp").forward(request, response);
            }
        }
        
        echecs++;
        session.setAttribute(ATT_ECHECSQ,echecs);
        // Stockage des messages d'erreur dans l'objet request
        request.setAttribute(ATT_ERREURS, errors);
        this.getServletContext().getRequestDispatcher("/WEB-INF/QuestionView.jsp").forward(request, response);
    }

}
