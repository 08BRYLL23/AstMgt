/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import extra.SharedFormat;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import objects.Asset;
import objects.AssetRequested;
import objects.Employee;
import objects.PurchaseRequest;
import services.AssetRequestedService;
import services.AssetService;
import services.PurchaseRequestService;

/**
 *
 * @author RubySenpaii
 */
public class PurchaseRequestServlet extends BaseServlet {

    @Override
    public void servletAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getRequestURI();
        try {
            String url;
            switch (action.split("/")[action.split("/").length - 1]) {
                case "Add":
                    url = CreatePurchaseRequest(request);
                    break;
                case "Submit":
                    url = AddPurchaseRequest(request);
                    break;
                case "Edit":
                    url = EditPurchaseRequest();
                    break;
                case "View":
                    url = ViewPurchaseRequestById(request);
                    break;
                case "Reject":
                    url = RejectPurchaseRequest(request);
                    break;
                case "Flag":
                case "List":
                    url = ListPurchaseRequest(request);
                    break;
                default:
                    url = ListPurchaseRequest(request);
                    break;
            }
            ServletContext context = getServletContext();
            RequestDispatcher rd = context.getRequestDispatcher(url);
            rd.forward(request, response);
        } catch (Exception x) {
            throw new ServletException(x);
        }
    }

    private String CreatePurchaseRequest(HttpServletRequest request) {
        AssetService assetDB = new AssetService();
        ArrayList<Asset> assetList = new ArrayList<Asset>();
        assetList = assetDB.GetAssets();
        HttpSession session = request.getSession();
        session.setAttribute("assets", assetList);
        
        File file = new File(getServletContext().getRealPath(SharedFormat.WFP_FILE_PATH));
        String fileNames[] = file.list();
        ArrayList<String> filtered = new ArrayList<>();
        for (int i = 0; i < fileNames.length; i++) {
            if (!fileNames[i].equals("placeholder.md")) {
                filtered.add(fileNames[i]);
            }
        }
        Collections.sort(filtered);
        session.setAttribute("fileList", filtered);

        try {
            Asset asset = assetDB.GetAsset(Integer.parseInt(request.getParameter("asset-id")));
            ArrayList<Asset> choices = assetDB.GetAssetsWithType(asset.AssetType);
            session.setAttribute("choices", choices);
            session.setAttribute("asset", asset);
        } catch (NumberFormatException x) {
            session.removeAttribute("choices");
            session.removeAttribute("asset");
            System.out.println("not re-acquiring");
        }
        return "/forms/purchase-request/add.jsp";
    }

    private String RejectPurchaseRequest(HttpServletRequest request) {
        PurchaseRequestService prservice = new PurchaseRequestService();
        int prid = Integer.parseInt(request.getParameter("prid"));
        HttpSession session = request.getSession();
        Employee employee = (Employee) session.getAttribute("user");
        String remarks = request.getParameter("remarks");
        try {
            prservice.RejectPurchaseRequest(employee.EmployeeId, prid, remarks);
            return "/PurchaseRequest/List";
        } catch (Exception e) {
            System.out.println("Error in PR servlet" + e);
            return "/forms/purchase-request/list.jsp";
        }
    }

    private String ViewPurchaseRequestById(HttpServletRequest request) {
        PurchaseRequestService prservice = new PurchaseRequestService();
        AssetRequestedService assetReqDB = new AssetRequestedService();
        AssetService assetDB = new AssetService();
        HttpSession session = request.getSession();
        int id = Integer.parseInt(request.getParameter("prid"));
        System.out.println("Getting the number : " + id);
        PurchaseRequest purchaseRequest = prservice.FindPurhcaseRequesById(id);
        ArrayList<AssetRequested> assetReqList = assetReqDB.GetAssetsRequestedWithPurchaseRequest(purchaseRequest.PurchaseRequestId);
        ArrayList<String> assetNameList = new ArrayList<>();
        for (int i = 0; i < assetReqList.size(); i++) {
            String name = assetDB.GetAsset(assetReqList.get(i).AssetId).AssetName;
            System.out.println("NAMES are : " + name);
            assetNameList.add(name);
        }

        session.setAttribute("assetRequested", assetReqList);
        session.setAttribute("assetNames", assetNameList);
        session.setAttribute("purchaseRequest", purchaseRequest);
        return "/forms/purchase-request/view.jsp";
    }

    private String ListPurchaseRequest(HttpServletRequest request) {
        PurchaseRequestService prservice = new PurchaseRequestService();
        ArrayList<PurchaseRequest> PRList = new ArrayList<>();
        PRList = prservice.FindAllPR();
        HttpSession session = request.getSession();
        session.setAttribute("PR", PRList);
        return "/forms/purchase-request/list.jsp";
    }

    private String AddPurchaseRequest(HttpServletRequest request) throws ParseException {

        PurchaseRequest pr = new PurchaseRequest();
        PurchaseRequestService purchaseRequestService = new PurchaseRequestService();
        String[] assets = request.getParameterValues("assets");
        String[] quantity = request.getParameterValues("quantity");
        String[] price = request.getParameterValues("price");
        Date test = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(test);
        int year = cal.get(Calendar.YEAR);
        pr.PurchaseRequestId = purchaseRequestService.FindAllPR().size() + 1;
        String prNoTemplate = "PR" + year + "-" + pr.PurchaseRequestId;
        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("user");

        pr.RequestedBy = user.EmployeeId;
        pr.PurchaseRequestNo = prNoTemplate;
        pr.Purpose = request.getParameter("purpose");
        System.out.println("Retrieiving at : " + test);
        pr.Date = test;
        pr.RequestedDate = test;
        ArrayList<AssetRequested> ALIST = new ArrayList<>();
        AssetService assetService = new AssetService();
        for (int i = 0; i < assets.length; i++) {
            AssetRequested ar = new AssetRequested();
            Asset as = new Asset();
            as = assetService.GetAssetByName(assets[i]);
            ar.AssetId = as.AssetId;
            ar.PurchaseRequestId = pr.PurchaseRequestId;
            ar.Quantity = Integer.parseInt(quantity[i]);
            ar.UnitCost = Double.parseDouble(price[i]);
            ALIST.add(ar);
        }
        AssetRequestedService ars = new AssetRequestedService();
        int result = purchaseRequestService.AddPurchaseRequest(pr);
        switch (result) {
            case 1:
                int checker = ars.AddAssetRequest(ALIST);
                if (checker != 0) {
                    return "/forms/purchase-request/add.jsp";
                }
                return "/forms/purchase-request/balh.jsp";
            default:
                return "/forms/login.jsp";
        }
    }

    private String EditPurchaseRequest() {
        PurchaseRequestService purchaseRequestService = new PurchaseRequestService();
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        int result = purchaseRequestService.UpdatePurchaseRequest(purchaseRequest);
        switch (result) {
            default:
                return "";
        }
    }
}
