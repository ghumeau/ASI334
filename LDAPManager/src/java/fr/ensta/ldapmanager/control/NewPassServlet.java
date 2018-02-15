/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.control;

import fr.ensta.ldapmanager.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author arnaudlegrignou
 */
@WebServlet(name = "NewPassServlet", urlPatterns = {"/NewPassServlet"})
public class NewPassServlet extends HttpServlet {
    
    public static final String ATT_USER = "user";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(ATT_USER);
        // A l'appel de la servlet (GET), affichage de la page UID si l'utilisateur n'a pas de session active
        if (user==null){
            this.getServletContext().getRequestDispatcher("/WEB-INF/NewPassView.jsp").forward(request, response);
        }
        else {this.getServletContext().getRequestDispatcher("/private").forward(request, response);} // si déjà authentifié, transfert sur la page data
    }

}
