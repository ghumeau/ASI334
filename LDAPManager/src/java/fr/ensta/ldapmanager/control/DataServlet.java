/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.control;

import fr.ensta.ldapmanager.model.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author arnaudlegrignou
 */
@WebServlet(name = "DataServlet", urlPatterns = {"/DataServlet"})
public class DataServlet extends HttpServlet {
    
    public static final String ATT_RESULTAT = "resultat";
    public static final String ATT_ECHECS = "echecs";
    public static final String ATT_USER = "user";
    public static final int maxEchecs = 5;

    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response )   throws ServletException, IOException {
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
            request.setAttribute(ATT_USER, user);
            // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
            this.getServletContext().getRequestDispatcher("/WEB-INF/LDAPView.jsp").forward(request, response);
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

}
