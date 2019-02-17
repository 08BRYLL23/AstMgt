/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import objects.Asset;
import objects.Supplier;
import services.AssetService;
import services.SupplierService;

/**
 *
 * @author rubysenpaii
 */
public class AjaxServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getRequestURI();
        System.out.println("ajax servlet" + action);
        String json = "";
        switch (action.split("/")[action.split("/").length - 1]) {
            case "AssetListWithType":
                json = AssetListWithType(request);
                break;
            case "SupplierListWithType":
                json = SupplierListWithType(request);
                break;
            case "RetrieveContactPerson":
                json = RetrieveContactPerson(request);
                break;
            default:
                break;
        }
        System.out.println("returns " + json);
        response.getWriter().write(json);
    }
    
    private String AssetListWithType(HttpServletRequest request) {
        String type = request.getParameter("type");
        ArrayList<Asset> assets = new AssetService().GetAssetsWithType(type);
        Gson gson = new Gson();
        return "{\"Assets\":" + gson.toJson(assets) + "}";
    } 
    
    private String SupplierListWithType(HttpServletRequest request) {
        String type = request.getParameter("type");
        ArrayList<Supplier> assets = new SupplierService().FindSupplierByType(type);
        Gson gson = new Gson();
        return "{\"Assets\":" + gson.toJson(assets) + "}";
    } 
    
    private String RetrieveContactPerson(HttpServletRequest request) {
        String supplierName = request.getParameter("supplier");
        String contactPerson = new SupplierService().GetSupplierContactPerson(supplierName);
        Gson gson = new Gson();
        return "{\"ContactPerson\":" + gson.toJson(contactPerson) + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
