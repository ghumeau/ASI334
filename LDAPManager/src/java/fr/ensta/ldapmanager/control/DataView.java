/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.control;

import java.io.IOException;
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
    public void doGet( HttpServletRequest request, HttpServletResponse response )   throws ServletException, IOException {
        String message = "Transmission de variables : OK !";
        request.setAttribute( "test", message );
        this.getServletContext().getRequestDispatcher( "/WEB-INF/DataView.jsp" ).forward( request, response );
        //processRequest(request, response);

    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

}
