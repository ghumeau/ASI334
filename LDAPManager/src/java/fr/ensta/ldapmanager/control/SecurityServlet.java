/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.control;

import fr.ensta.ldapmanager.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 *
 * @author arnaudlegrignou
 */
public class SecurityServlet extends HttpServlet {

    public static final String ATT_USER = "user";
    
    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response )   throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        // A l'appel de la servlet (GET), affichage de la page d'authentification si l'utilisateur n'a pas de session active
        if (user==null){
            this.getServletContext().getRequestDispatcher("/login").forward(request, response);
        }
        else {
            request.setAttribute(ATT_USER, user.GetInfo());
            // Transmission de la MAP contenant les infos utilisateur à la JSP d'affichage des données
            this.getServletContext().getRequestDispatcher("/WEB-INF/SecurityView.jsp").forward(request, response);
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

}
