/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import extra.FileModification;
import extra.SharedFormat;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import objects.Asset;
import objects.Employee;
import objects.ExpenditureItem;
import objects.ExpenditureLimit;
import services.AssetService;
import services.ExpenditureItemService;
import services.ExpenditureLimitService;

/**
 *
 * @author RubySenpaii
 */
@MultipartConfig
public class ExpenditureServlet extends BaseServlet {

    @Override
    public void servletAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getRequestURI();
        System.out.println("action used " + action);
        try {
            String url;
            switch (action.split("/")[action.split("/").length - 1]) {
                case "SubmitWFP":
                    url = WFP(request);
                    break;
                case "SubmitAPP":
                    url = APP(request);
                    break;
                case "Submit":
                    url = SubmitExpenditureLimit(request);
                    break;
                case "Submitv2":
                    url = SubmitExpenditureLimitv2(request);
                    break;
                default:
                    url = ExpenditureLimit(request);
                    break;
            }
            ServletContext context = getServletContext();
            RequestDispatcher rd = context.getRequestDispatcher(url);
            rd.forward(request, response);
        } catch (Exception x) {
            throw new ServletException(x);
        }
    }

    private String ExpenditureLimit(HttpServletRequest request) {
        File file = new File(getServletContext().getRealPath(SharedFormat.APP_FILE_PATH));
        String fileNames[] = file.list();
        ArrayList<String> filtered = new ArrayList<>();
        for (int i = 0; i < fileNames.length; i++) {
            if (!fileNames[i].equals("placeholder.md")) {
                filtered.add(fileNames[i]);
            }
        }
        Collections.sort(filtered);
        HttpSession session = request.getSession();
        session.setAttribute("fileList", filtered);
        return "/management/expenditure_limit.jsp";
    }

    private String WFP(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            Employee user = (Employee) session.getAttribute("user");
            FileModification file = new FileModification();
            Part wfpFile = request.getPart("wfp");
            file.SaveFile(getServletContext().getRealPath("/uploaded-files/wfp"), wfpFile, "WFP" + user.Division + SharedFormat.TIME_STAMP.format(Calendar.getInstance().getTime()));

            //Part appFile = request.getPart("annual-plan");
            //file.SaveFile(getServletContext().getRealPath("/uploaded-files/app"), appFile, "AnnualProcurementPlan" + SharedFormat.TIME_STAMP.format(Calendar.getInstance().getTime()));
        } catch (IOException x) {
            System.err.println("IO Exception in uploading wfp");
            System.err.println(x);
        } catch (ServletException x) {
            System.err.println("Servlet Exception in uploading wfp");
            System.err.println(x);
        }
        return "/HomeServlet";
    }

    private String APP(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            Employee user = (Employee) session.getAttribute("user");
            FileModification file = new FileModification();

            Part appFile = request.getPart("app");
            file.SaveFile(getServletContext().getRealPath("/uploaded-files/app"), appFile, "AnnualProcurementPlan" + SharedFormat.TIME_STAMP.format(Calendar.getInstance().getTime()));
        } catch (IOException x) {
            System.err.println("IO Exception in uploading app");
            System.err.println(x);
        } catch (ServletException x) {
            System.err.println("Servlet Exception in uploading app");
            System.err.println(x);
        }
        return "/HomeServlet";
    }

    private String SubmitExpenditureLimitv2(HttpServletRequest request) {
        String[] names = request.getParameterValues("name");
        String[] types = request.getParameterValues("type");
        String[] price = request.getParameterValues("price");
        String[] admin = request.getParameterValues("admin");
        String[] general = request.getParameterValues("general");
        String[] procurement = request.getParameterValues("procurement");
        String[] personnel = request.getParameterValues("personnel");
        String[] records = request.getParameterValues("records");
        int year = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < names.length; i++) {
            AssetService assetService = new AssetService();
            int assetId;
            try {
                assetId = assetService.GetAssetByName(names[i]).AssetId;
            } catch (IndexOutOfBoundsException x) {
                assetId = assetService.GetAssets().size() + 1;
                Asset asset = new Asset();
                asset.AssetId = assetId;
                asset.AssetName = names[i];
                asset.AssetType = types[i];
                asset.Description = price[i];
                asset.FundCluster = "N/A";
                asset.StockNo = assetId + "";
                asset.Unit = "pc";
                asset.EstimatedUsefulLife = 3;
                assetService.AddAsset(asset);
            }
            ExpenditureItem procurementExpItem = new ExpenditureItem();
            procurementExpItem.AssetId = assetId;
            procurementExpItem.QuantityOrdered = 0;
            procurementExpItem.Year = year;
            procurementExpItem.Quarter = SharedFormat.getQuarter();
            procurementExpItem.Division = "Procurement";
            procurementExpItem.QuantityLimit = Integer.parseInt(procurement[i]);
            
            ExpenditureItem adminExpItem = new ExpenditureItem();
            adminExpItem.AssetId = assetId;
            adminExpItem.QuantityOrdered = 0;
            adminExpItem.Year = year;
            adminExpItem.Quarter = SharedFormat.getQuarter();
            adminExpItem.Division = "Admin";
            adminExpItem.QuantityLimit = Integer.parseInt(admin[i]);
            
            ExpenditureItem generalExpItem = new ExpenditureItem();
            generalExpItem.AssetId = assetId;
            generalExpItem.QuantityOrdered = 0;
            generalExpItem.Year = year;
            generalExpItem.Quarter = SharedFormat.getQuarter();
            generalExpItem.Division = "General";
            generalExpItem.QuantityLimit = Integer.parseInt(general[i]);
            
            ExpenditureItem personnelExpItem = new ExpenditureItem();
            personnelExpItem.AssetId = assetId;
            personnelExpItem.QuantityOrdered = 0;
            personnelExpItem.Year = year;
            personnelExpItem.Quarter = SharedFormat.getQuarter();
            personnelExpItem.Division = "Personnel";
            personnelExpItem.QuantityLimit = Integer.parseInt(personnel[i]);
            
            ExpenditureItem recordsExpItem = new ExpenditureItem();
            recordsExpItem.AssetId = assetId;
            recordsExpItem.QuantityOrdered = 0;
            recordsExpItem.Year = year;
            recordsExpItem.Quarter = SharedFormat.getQuarter();
            recordsExpItem.Division = "Records";
            recordsExpItem.QuantityLimit = Integer.parseInt(records[i]);
            
            ExpenditureItemService eis = new ExpenditureItemService();
            System.out.println("admin result: " + eis.AddExpenditureItem(adminExpItem));
            System.out.println("general result: " + eis.AddExpenditureItem(generalExpItem));
            System.out.println("personnel result: " + eis.AddExpenditureItem(personnelExpItem));
            System.out.println("records result: " + eis.AddExpenditureItem(recordsExpItem));
            System.out.println("procurement result: " + eis.AddExpenditureItem(procurementExpItem));
        }
        return "/HomeServlet";
    }

    private String SubmitExpenditureLimit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Employee employee = (Employee) session.getAttribute("user");

        int year = Calendar.getInstance().get(Calendar.YEAR);
        String quarter = SharedFormat.getQuarter();
//        double adminSupplies = Double.parseDouble(request.getParameter("admin-supplies"));
        double adminSupplies = 0.00;
        double adminEquipment = Double.parseDouble(request.getParameter("admin-equipment"));
//        double procurementSupplies = Double.parseDouble(request.getParameter("procurement-supplies"));
        double procurementSupplies = 0.00;
        double procurementEquipment = Double.parseDouble(request.getParameter("procurement-equipment"));
//        double managementSupplies = Double.parseDouble(request.getParameter("management-supplies"));
        double managementSupplies = 0.00;
        double managementEquipment = Double.parseDouble(request.getParameter("management-equipment"));
//        double generalSupplies = Double.parseDouble(request.getParameter("general-supplies"));
        double generalSupplies = 0.00;
        double generalEquipment = Double.parseDouble(request.getParameter("general-equipment"));
//        double financeSupplies = Double.parseDouble(request.getParameter("finance-supplies"));
        double financeSupplies = 0.00;
        double financeEquipment = Double.parseDouble(request.getParameter("finance-equipment"));

        ExpenditureLimit adminLimit = new ExpenditureLimit();
        adminLimit.Division = "Admin";
        adminLimit.Equipment = adminEquipment;
        adminLimit.Quarter = quarter;
        adminLimit.Supplies = adminSupplies;
        adminLimit.Year = year;

        ExpenditureLimit procurementLimit = new ExpenditureLimit();
        procurementLimit.Division = "Procurement";
        procurementLimit.Equipment = procurementEquipment;
        procurementLimit.Quarter = quarter;
        procurementLimit.Supplies = procurementSupplies;
        procurementLimit.Year = year;

        ExpenditureLimit managementLimit = new ExpenditureLimit();
        managementLimit.Division = "Personnel";
        managementLimit.Equipment = managementEquipment;
        managementLimit.Quarter = quarter;
        managementLimit.Supplies = managementSupplies;
        managementLimit.Year = year;

        ExpenditureLimit generalLimit = new ExpenditureLimit();
        generalLimit.Division = "General";
        generalLimit.Equipment = generalEquipment;
        generalLimit.Quarter = quarter;
        generalLimit.Supplies = generalSupplies;
        generalLimit.Year = year;

        ExpenditureLimit financeLimit = new ExpenditureLimit();
        financeLimit.Division = "Records";
        financeLimit.Equipment = financeEquipment;
        financeLimit.Quarter = quarter;
        financeLimit.Supplies = financeSupplies;
        financeLimit.Year = year;

        /*try {
            FileModification file = new FileModification();
            Part wfpFile = request.getPart("financial-plan");
            file.SaveFile(getServletContext().getRealPath("/uploaded-files/wfp"), wfpFile, "WorkFinancialPlan" + SharedFormat.TIME_STAMP.format(Calendar.getInstance().getTime()));
            
            //Part appFile = request.getPart("annual-plan");
            //file.SaveFile(getServletContext().getRealPath("/uploaded-files/app"), appFile, "AnnualProcurementPlan" + SharedFormat.TIME_STAMP.format(Calendar.getInstance().getTime()));
        } catch (IOException x) {
            System.err.println("IO Exception in uploading wfp or app");
            System.err.println(x);
        } catch (ServletException x) {
            System.err.println("Servlet Exception in uploading wfp or app");
            System.err.println(x);
        }*/
        ExpenditureLimitService expenditureService = new ExpenditureLimitService();
        int result = expenditureService.AddExpenditureLimit(adminLimit);
        result = expenditureService.AddExpenditureLimit(financeLimit);
        result = expenditureService.AddExpenditureLimit(generalLimit);
        result = expenditureService.AddExpenditureLimit(managementLimit);
        result = expenditureService.AddExpenditureLimit(procurementLimit);
        if (result == 1) {
            return "/template.jsp";
        } else {
            return "/AMS/ExpenditureServlet";
        }
    }
}
