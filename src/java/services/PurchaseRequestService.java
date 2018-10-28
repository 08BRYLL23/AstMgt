/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import db.DBConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import objects.PurchaseRequest;

/**
 *
 * @author Bryll Joey Delfin
 */
public class PurchaseRequestService {

    private final String AddQuery = "INSERT INTO PurchaseRequest(" + PurchaseRequest.COLUMN_PURCHASE_REQUEST_ID + "," + PurchaseRequest.COLUMN_PURCHASE_REQUEST_NO + "," 
            + PurchaseRequest.COLUMN_RESPONSIBILITY_CENTER_CODE + "," + PurchaseRequest.COLUMN_DATE + "," + PurchaseRequest.COLUMN_PURPOSE + ","
            + PurchaseRequest.COLUMN_REQUESTED_BY + "," + PurchaseRequest.COLUMN_REQUESTED_DATE 
            + ")VALUES(?,?,?,?,?,?,?);";
    private final String FindPurchaseRequestbyId = "SELECT * FROM PurchaseRequest WHERE " + PurchaseRequest.COLUMN_PURCHASE_REQUEST_ID + " = ? ;";
    private final String FindPurchaseRequestbyNo = "SELECT * FROM PurchaseRequest WHERE " + PurchaseRequest.COLUMN_PURCHASE_REQUEST_NO + " = ? ;";
    private final String FindAllPurchaseRequest = "SELECT * FROM PurchaseRequest ; ";

    public int AddPurchaseRequest(PurchaseRequest pr) {
        try {
            DBConnectionFactory db = DBConnectionFactory.getInstance();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(AddQuery);
            ps.setInt(1, pr.PurchaseRequestId);
            ps.setString(2, pr.PurchaseRequestNo);
            ps.setString(3, pr.ResponsibilityCenterCode);
            ps.setObject(4, pr.Date);
            ps.setString(5, pr.Purpose);
            ps.setInt(6, pr.RequestedBy);
            ps.setObject(7, pr.RequestedDate);

            int res = ps.executeUpdate();
            ps.close();
            conn.close();
            return res;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return 0;
    }
    
    public int UpdatePurchaseRequest(PurchaseRequest purchaseRequest) {
        try {
            DBConnectionFactory db = DBConnectionFactory.getInstance();
            Connection con = db.getConnection();
            
            String query = "UPDATE PurchaseRequest SET " + PurchaseRequest.COLUMN_APPROVED_BY + " = ?, " + PurchaseRequest.COLUMN_APPROVED_DATE + " = ?, "
                    + PurchaseRequest.COLUMN_DATE + " = ?, " + PurchaseRequest.COLUMN_PURCHASE_REQUEST_NO + " = ?, " + PurchaseRequest.COLUMN_PURPOSE + " = ?, "
                    + PurchaseRequest.COLUMN_REQUESTED_BY + " = ?, " + PurchaseRequest.COLUMN_REQUESTED_DATE + " = ?, "
                    + PurchaseRequest.COLUMN_RESPONSIBILITY_CENTER_CODE + " = ? WHERE " + PurchaseRequest.COLUMN_PURCHASE_REQUEST_ID + " = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, purchaseRequest.ApprovedBy);
            ps.setObject(2, purchaseRequest.ApprovedDate);
            ps.setObject(3, purchaseRequest.Date);
            ps.setString(4, purchaseRequest.PurchaseRequestNo);
            ps.setString(5, purchaseRequest.Purpose);
            ps.setInt(6, purchaseRequest.RequestedBy);
            ps.setObject(7, purchaseRequest.RequestedDate);
            ps.setString(8, purchaseRequest.ResponsibilityCenterCode);
            ps.setInt(9, purchaseRequest.PurchaseRequestId);
            
            int result = ps.executeUpdate();
            ps.close();
            con.close();
            return result;
        } catch (SQLException x) {
            System.err.println(x);
            return -1;
        }
    }

    public ArrayList<PurchaseRequest> FindPurhcaseRequesById(int prid) {
        DBConnectionFactory db = DBConnectionFactory.getInstance();
        Connection conn = db.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(FindPurchaseRequestbyId);
            ps.setInt(1, prid);
            ArrayList<PurchaseRequest> elist = getResult(ps.executeQuery());
            ps.close();
            return elist;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }
    
    public ArrayList<PurchaseRequest> FindPurchaseRequestByNo(String prno) {
        DBConnectionFactory db = DBConnectionFactory.getInstance();
        Connection conn = db.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(FindPurchaseRequestbyNo);
            ps.setString(1, prno);
            ArrayList<PurchaseRequest> elist = getResult(ps.executeQuery());
            ps.close();
            return elist;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }
    
    public ArrayList<PurchaseRequest> FindAllPR() {
        DBConnectionFactory db = DBConnectionFactory.getInstance();
        Connection conn = db.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(FindAllPurchaseRequest);
            ArrayList<PurchaseRequest> elist = getResult(ps.executeQuery());
            ps.close();
            return elist;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }

    private ArrayList<PurchaseRequest> getResult(ResultSet rs) throws SQLException {
        ArrayList<PurchaseRequest> purchaserequestList = new ArrayList<PurchaseRequest>();
        while (rs.next()) {
            PurchaseRequest e = new PurchaseRequest();
            e.ApprovedBy = rs.getInt(PurchaseRequest.COLUMN_APPROVED_BY);
            e.ApprovedDate = rs.getDate(PurchaseRequest.COLUMN_APPROVED_DATE);
            e.Date = rs.getDate(PurchaseRequest.COLUMN_DATE);
            e.PurchaseRequestId = rs.getInt(PurchaseRequest.COLUMN_PURCHASE_REQUEST_ID);
            e.PurchaseRequestNo = rs.getString(PurchaseRequest.COLUMN_PURCHASE_REQUEST_NO);
            e.Purpose = rs.getString(PurchaseRequest.COLUMN_PURPOSE);
            e.RequestedBy = rs.getInt(PurchaseRequest.COLUMN_REQUESTED_BY);
            e.RequestedDate = rs.getDate(PurchaseRequest.COLUMN_REQUESTED_DATE);
            e.ResponsibilityCenterCode = rs.getString(PurchaseRequest.COLUMN_RESPONSIBILITY_CENTER_CODE);
            purchaserequestList.add(e);
        }
        rs.close();
        return purchaserequestList;
    }
}
