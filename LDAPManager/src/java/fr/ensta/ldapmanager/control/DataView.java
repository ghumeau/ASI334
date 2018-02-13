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

/**
 *
 * @author arnaudlegrignou
 */
@WebServlet(name = "LDAPViewer", urlPatterns = {"/LDAPViewer"})
public class DataView extends HttpServlet {

    @Override
<<<<<<< HEAD:LDAPManager/src/java/fr/ensta/ldapmanager/control/DataView.java
    public void doGet( HttpServletRequest request, HttpServletResponse response )   throws ServletException, IOException {
        String message = "Transmission de variables : OK !";
        request.setAttribute( "test", message );
        this.getServletContext().getRequestDispatcher( "/WEB-INF/DataView.jsp" ).forward( request, response );
        //processRequest(request, response);
=======
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Services svc = new Services();
        request.setAttribute("usermap", svc.RetrieveInfo());
        // A l'appel de la servlet (GET), affichage de la page 
        this.getServletContext().getRequestDispatcher("/WEB-INF/LDAPView.jsp").forward(request, response);
>>>>>>> 7ae895a760cf7c9a2fedcf0a9fcdde1fdb949060:LDAPManager/src/java/fr/ensta/ldapmanager/control/LDAPView.java
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

}
