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
    private static Services svc = new Services();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Affichage de la page d'inscription */
        this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resultat = null;
        Map<String, String> erreurs = new HashMap<>();
        User usr = null;

        //Récupération des champs du formulaire.
        String login = request.getParameter(CHAMP_LOGIN);
        String pwd = request.getParameter(CHAMP_PASS);

        // Verification de la syntaxe de l'UID.
        try {
            testFormatUID(login);
        }
        catch (Exception e) {
            erreurs.put(CHAMP_LOGIN, e.getMessage());
        }

        // Verification de la syntaxe du mot de passe.
        try {
            testFormatPWD(pwd);
        }
        catch (Exception e) {
            erreurs.put(CHAMP_PASS, e.getMessage());
        }

        // Authentification si le couple UID/password est syntaxiquement valable
        if (erreurs.isEmpty()) {
            usr = svc.Authentify(login,pwd);
            if (usr==null){                                  // échec de l'authentification
                resultat = "Echec de l'authentification.";
            }
            else {                                           // authentification réussie
                request.setAttribute("usermap", svc.RetrieveInfo());
                // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
                this.getServletContext().getRequestDispatcher("/WEB-INF/LDAPView.jsp").forward(request, response);
            }
        }
     
        // Stockage du résultat et des messages d'erreur dans l'objet request
        request.setAttribute(ATT_ERREURS, erreurs);
        request.setAttribute(ATT_RESULTAT, resultat);
        // Transmission de la paire d'objets request/response à notre JSP
        this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
    }


    //Validation de la syntaxe des mots de passe saisis.
    private void testFormatPWD(String pwd) throws Exception {
        if (pwd.length()==0) {
            throw new Exception("Veuillez saisir un mot de passe");
        } 
        
        // Création de la liste des caractères valides (chiffres et lettres min/maj)
        List<String> validChar = new ArrayList<String>();
        int i;
        for (i = 48; i<=57; i++){                          // chiffres
            validChar.add(Character.toString((char) i));
        }
        for (i = 65; i<=90; i++){                          // majuscules
            validChar.add(Character.toString((char) i));
        }
        for (i = 97; i<=122; i++){                         // minuscules
            validChar.add(Character.toString((char) i));
        }
        for (i = 0; i<pwd.length(); i++){                  // Verification de la validité des caractères saisis
            if(!validChar.contains(pwd.substring(i,i+1))){
                throw new Exception("Veuillez saisir un mot de passe valide.");
            }
        }
        

    }

    //Validation de la syntaxe des UIDs saisis.
    private void testFormatUID(String uid) throws Exception {
        if (uid.length()==0) {
            throw new Exception("Veuillez saisir un UID.");
         }
        // Création de la liste des caractères valides (chiffres et lettres min/maj)
        List<String> validChar = new ArrayList<String>();
        int i;
        for (i = 48; i<=57; i++){                          // chiffres
            validChar.add(Character.toString((char) i));
        }
        for (i = 65; i<=90; i++){                          // majuscules
            validChar.add(Character.toString((char) i));
        }
        for (i = 97; i<=122; i++){                         // minuscules
            validChar.add(Character.toString((char) i));
        }
        for (i = 0; i<uid.length(); i++){                  // Verification de la validité des caractères saisis
                if(!validChar.contains(uid.substring(i,i+1))){
                throw new Exception("Veuillez saisir un UID valide.");
            }
        }
        
        
    }
}
