package fr.ensta.ldapmanager.control;

import fr.ensta.ldapmanager.model.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        /* Affichage de la page d'inscription */
        this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resultat;
        Map<String, String> erreurs = new HashMap<>();

        /* Récupération des champs du formulaire. */
        String login = request.getParameter(CHAMP_LOGIN);
        String motDePasse = request.getParameter(CHAMP_PASS);


        /* Validation du champ email. */
        try {
            validationNom(login);
        } catch (Exception e) {
            erreurs.put(CHAMP_LOGIN, e.getMessage());
        }

        /* Validation des champs mot de passe et confirmation. */
        try {
            validationMotsDePasse(motDePasse);
        } catch (Exception e) {
            erreurs.put(CHAMP_PASS, e.getMessage());
        }


        //Initialisation du résultat global de la validation. 
        if (erreurs.isEmpty()) {
            resultat = "Succès de l'authentification.";
        } else {
            resultat = "Échec de l'authentification.";
        }
        
        
        //Authentification en tenant compte de la logique metier (draft)
        
        User utilisateur = new User("0001","password");
        
        String nom = "Le Grignou";       
        String prenom = "Arnaud";      
        String tel = "0768256292";       
        String mail = "arnaudegrignou@yahoo.fr";       
        String question = "ecole ?";
        String reponse = "ensta paristech";        
        
        utilisateur.setLastName(nom);
        utilisateur.setFirstName(prenom);
        utilisateur.setPhoneNumber(tel);
        utilisateur.setEmail(mail);
        utilisateur.setSecureAnswer(reponse);
        
        
        
        
        
        
        /* Stockage du résultat et des messages d'erreur dans l'objet request */
        request.setAttribute(ATT_ERREURS, erreurs);
        request.setAttribute(ATT_RESULTAT, resultat);
        request.setAttribute("nom", nom);

        /* Transmission de la paire d'objets request/response à notre JSP */
        if (erreurs.isEmpty()) {this.getServletContext().getRequestDispatcher("/WEB-INF/LDAPView.jsp").forward(request, response);}
        else{this.getServletContext().getRequestDispatcher("/WEB-INF/LoginView.jsp").forward(request, response);}
    }


     //Validation de la sémantique des mots de passe saisis.
    private void validationMotsDePasse(String motDePasse) throws Exception {
        if (motDePasse == "") {
            throw new Exception("Veuillez saisir un mot de passe");
        } 
        if (motDePasse != "" && motDePasse.trim().length() < 3) {
            throw new Exception("Les mots de passe doivent contenir au moins 3 caractères.");
        }

    }

    //Validation de la semantique des UIDs saisis.
    private void validationNom(String nom) throws Exception {
        if (nom != null && nom.trim().length() < 3) {
            throw new Exception("Le nom d'utilisateur doit contenir au moins 3 caractères.");
        }
    }
}
