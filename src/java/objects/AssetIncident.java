/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.Date;

/**
 *
 * @author RubySenpaii
 */
public class AssetIncident {
    public static final String COLUMN_ASSET_TAG = "AssetTag";
    public static final String COLUMN_TIMESTAMP = "Timestamp";
    public static final String COLUMN_SEVERITY = "Severity";
    public static final String COLUMN_REMARKS = "Remarks";
    public static final String COLUMN_REPORTED_BY = "ReportedBy";
    
    public String AssetTag;
    public Date Timestamp;
    public int Severity;
    public String Remarks;
    public int ReportedBy;
    
    public Equipment Equipment;
    public Employee Reporter;
    
    public String getSeverity() {
        switch(this.Severity) {
            case 1: return "Low";
            case 2: return "Medium";
            case 3: return "High";
            default: return "?";
        }
    }
}
