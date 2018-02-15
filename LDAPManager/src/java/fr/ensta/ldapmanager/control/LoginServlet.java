package fr.ensta.ldapmanager.control;

import fr.ensta.ldapmanager.model.*;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

    public static final String CHAMP_LOGIN = "login";
    public static final String CHAMP_PASS = "password";
    public static final String ATT_ERREURS = "erreurs";
    public static final String ATT_RESULTAT = "resultat";
    public static final String ATT_ECHECS = "echecs";
    public static final String ATT_USER = "user";
    public static final int maxEchecs = 5;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        // A l'appel de la servlet (GET), affichage de la page d'authentification si l'utilisateur n'a pas de session active
        if (user==null){
            Integer echecs = (Integer) session.getAttribute(ATT_ECHECS);
            if (echecs==null){session.setAttribute(ATT_ECHECS,0);}
            else if (echecs>=maxEchecs){session.setAttribute(ATT_RESULTAT, "Trop d'échec, vous avez été bloqué !!!");}
            this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
        }
        else {
            this.getServletContext().getRequestDispatcher("/private").forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Services svc = new Services();
        User usr = null;
        HttpSession session = request.getSession();
        Integer echecs = (Integer) session.getAttribute(ATT_ECHECS);
        String result = null;
        Map<String, String> errors = new HashMap<>();
        //Récupération des champs du formulaire.
        String login = request.getParameter(CHAMP_LOGIN);
        String pwd = request.getParameter(CHAMP_PASS);
        
        // Vérification du nombre d'échecs d'authentification
        if (echecs==null){echecs=0;}
        if (echecs>=maxEchecs){
            request.setAttribute(ATT_RESULTAT, "Trop d'échec, vous avez été bloqué !!!");
            this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
        }        

        // Verification de la syntaxe de l'UID.
        if (Checks.isEmpty(login)){errors.put(CHAMP_LOGIN,"Veuillez saisir votre UID.");}
        else if (!Checks.syntaxe(login,Checks.Argument.UID)){errors.put(CHAMP_LOGIN,"Veuillez saisir un UID valide.");}

        // Verification de la syntaxe du mot de passe.
        if (Checks.isEmpty(pwd)){errors.put(CHAMP_PASS,"Veuillez saisir votre mot de passe.");}
        else if (!Checks.syntaxe(login,Checks.Argument.PWD)){errors.put(CHAMP_PASS,"Veuillez saisir un mot de passe valide.");}

        // Tentative d'authentification si le couple UID/password est syntaxiquement valable
        if (errors.isEmpty()) {usr = svc.AuthenticationSequence(login,pwd);}
        
        if (usr!=null){                                  // authentification réussie
            session.setAttribute(ATT_USER, usr);
            request.setAttribute(ATT_USER, usr.GetInfo());
            // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
            this.getServletContext().getRequestDispatcher("/WEB-INF/DataView.jsp").forward(request, response);
        }
        else{                                            // échec
            echecs++;
            session.setAttribute(ATT_ECHECS,echecs);
            result = "Echec de l'authentification, tentatives restantes : " + (maxEchecs-echecs);
            // Stockage du résultat et des messages d'erreur dans la requête
            request.setAttribute(ATT_ERREURS, errors);
            request.setAttribute(ATT_RESULTAT, result);
            this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
        }
    }
}
