package fr.ensta.ldapmanager.control;

import fr.ensta.ldapmanager.model.*;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginView extends HttpServlet {

    public static final String CHAMP_LOGIN = "login";
    public static final String CHAMP_PASS = "password";
    public static final String ATT_ERREURS = "erreurs";
    public static final String ATT_RESULTAT = "resultat";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // A l'appel de la servlet (GET), affichage de la page d'authentification
        this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result = null;
        Map<String, String> errors = new HashMap<>();
        Services svc = new Services();
        User usr = null;

        //Récupération des champs du formulaire.
        String login = request.getParameter(CHAMP_LOGIN);
        String pwd = request.getParameter(CHAMP_PASS);

        // Verification de la syntaxe de l'UID.
        if (Checks.isEmpty(login)){errors.put(CHAMP_LOGIN,"Veuillez saisir votre UID.");}
        else if (!Checks.syntaxe(login,Checks.Argument.UID)){errors.put(CHAMP_LOGIN,"Veuillez saisir un UID valide.");}

        // Verification de la syntaxe du mot de passe.
        if (Checks.isEmpty(pwd)){errors.put(CHAMP_PASS,"Veuillez saisir votre mot de passe.");}
        else if (!Checks.syntaxe(login,Checks.Argument.PWD)){errors.put(CHAMP_PASS,"Veuillez saisir un mot de passe valide.");}

        // Authentification si le couple UID/password est syntaxiquement valable
        if (errors.isEmpty()) {
            usr = svc.Authentify(login,pwd);
            if (usr==null){                                  // échec de l'authentification
                result = "Echec de l'authentification.";
            }
            else {                                           // authentification réussie
                request.setAttribute("usermap", svc.RetrieveInfo());
                // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
                this.getServletContext().getRequestDispatcher("/WEB-INF/LDAPView.jsp").forward(request, response);
            }
        }
     
        // Stockage du résultat et des messages d'erreur dans l'objet request
        request.setAttribute(ATT_ERREURS, errors);
        request.setAttribute(ATT_RESULTAT, result);
        // Transmission de la paire d'objets request/response à notre JSP
        this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
    }

}
