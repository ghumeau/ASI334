package fr.ensta.ldapmanager.control;

import fr.ensta.ldapmanager.model.*;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class LoginView extends HttpServlet {

    public static final String CHAMP_LOGIN = "login";
    public static final String CHAMP_PASS = "password";
    public static final String ATT_ERREURS = "erreurs";
    public static final String ATT_RESULTAT = "resultat";
    public static final String ATT_ECHECS = "echecs";
    public static final int maxEchecs = 5;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Services svc = (Services) session.getAttribute("svc");
        // A l'appel de la servlet (GET), affichage de la page d'authentification si l'utilisateur n'a pas de session active
        if (svc==null){
            Integer echecs = (Integer) session.getAttribute(ATT_ECHECS);
            if (echecs==null){session.setAttribute(ATT_ECHECS,0);}
            else if (echecs>=maxEchecs){session.setAttribute(ATT_RESULTAT, "Trop d'échec, vous avez été bloqué !!!");}
            this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
        }
        else {
            request.setAttribute("usermap", svc.RetrieveInfo());
            // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
            this.getServletContext().getRequestDispatcher("/WEB-INF/LDAPView.jsp").forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result = null;
        Map<String, String> errors = new HashMap<>();
        Services svc = new Services();
        User usr = null;
        HttpSession session = request.getSession();
        Integer echecs = (Integer) session.getAttribute(ATT_ECHECS);
        
        // Vérification du nombre d'échecs d'authentification
        if (echecs==null){echecs=0;}
        if (echecs>=maxEchecs){
            request.setAttribute(ATT_RESULTAT, "Trop d'échec, vous avez été bloqué !!!");
            this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
        }
        
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
            if (usr!=null){                                  // authentification réussie
                request.setAttribute("svc", svc);
                // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
                this.getServletContext().getRequestDispatcher("/WEB-INF/DataView.jsp").forward(request, response);
            }
        }
     
        echecs++;
        session.setAttribute(ATT_ECHECS,echecs);
        result = "Echec de l'authentification, tentatives restantes : " + (maxEchecs-echecs);
        // Stockage du résultat et des messages d'erreur dans l'objet request
        request.setAttribute(ATT_ERREURS, errors);
        request.setAttribute(ATT_RESULTAT, result);
        // Transmission de la paire d'objets request/response à notre JSP
        this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
    }


    //Validation de la syntaxe des mots de passe saisis.
    private void testFormatPWD(String pwd) throws Exception {
        if (pwd.length()==0) {
            throw new Exception("Veuillez saisir un mot de passe");
        } 
        
        // Création de la liste des caractères valides (chiffres et lettres min/maj)
        List<String> validChar = new ArrayList<>();
        int i;
        for (i = 48; i<=57; i++){validChar.add(Character.toString((char) i));} // chiffres
        for (i = 65; i<=90; i++){validChar.add(Character.toString((char) i));} // majuscules
        for (i = 97; i<=122; i++){validChar.add(Character.toString((char) i));} // minuscules
        for (i = 0; i<pwd.length(); i++){                  // Verification de la validité des caractères saisis
            if(!validChar.contains(pwd.substring(i,i+1))){throw new Exception("Veuillez saisir un mot de passe valide.");}
        }
        

    }

    //Validation de la syntaxe des UIDs saisis.
    private void testFormatUID(String uid) throws Exception {
        if (uid.length()==0) {
            throw new Exception("Veuillez saisir un UID.");
         }
        // Création de la liste des caractères valides (chiffres et lettres min/maj)
        List<String> validChar = new ArrayList<>();
        int i;
        for (i = 48; i<=57; i++){validChar.add(Character.toString((char) i));} // chiffres
        for (i = 65; i<=90; i++){validChar.add(Character.toString((char) i));} // majuscules
        for (i = 97; i<=122; i++){validChar.add(Character.toString((char) i));} // minuscules
        for (i = 0; i<uid.length(); i++){                  // Verification de la validité des caractères saisis
                if(!validChar.contains(uid.substring(i,i+1))){throw new Exception("Veuillez saisir un UID valide.");}
        }
        
        
    }
}
