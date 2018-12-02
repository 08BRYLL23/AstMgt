/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import objects.Equipment;
import objects.PurchaseRequest;
import objects.Supplies;
import services.EquipmentService;
import services.PurchaseOrderService;
import services.PurchaseRequestService;
import services.SuppliesService;

/**
 *
 * @author rubysenpaii
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/HomeServlet"})
public class HomeServlet extends BaseServlet {

    @Override
    public void servletAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletContext context = getServletContext();
        RequestDispatcher rd = context.getRequestDispatcher("/homepage.jsp");
        rd.forward(request, response);
    }
}