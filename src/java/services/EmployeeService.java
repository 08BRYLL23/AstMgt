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
import objects.Employee;

/**
 *
 * @author Bryll Joey Delfin
 */
public class EmployeeService {

    private String AddQuery = "INSERT INTO Employee(" + Employee.COLUMN_EMPLOYEE_ID + "," + Employee.COLUMN_LAST_NAME + ","
            + Employee.COLUMN_FIRST_NAME + "," + Employee.COLUMN_EMAIL + "," + Employee.COLUMN_CONTACT_NUMBER + "," + Employee.COLUMN_DIVISION + ","
            + Employee.COLUMN_CIVIL_STATUS + "," + Employee.COLUMN_BIRTHDATE + "," + Employee.COLUMN_GENDER + "," + Employee.COLUMN_EMPLOYEE_STATUS + ","
            + Employee.COLUMN_USERNAME + "," + Employee.COLUMN_PASSWORD + "," + Employee.COLUMN_START_DATE + "," + Employee.COLUMN_END_DATE + "," + Employee.COLUMN_FLAG
            + ")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    private String SelectOneQuery = "Select * FROM Employee WHERE " + Employee.COLUMN_USERNAME + " = ? AND " + Employee.COLUMN_PASSWORD + " = ? ;";
    private String SelectOneByEmployeeId = "Select * FROM Employee WHERE " + Employee.COLUMN_EMPLOYEE_ID + " = ?";
    private String SelectAllEmployees = "Select * FROM Employee";

    public ArrayList<Employee> Authenticate(String username, String password) {
        DBConnectionFactory db = DBConnectionFactory.getInstance();
        Connection conn = db.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(SelectOneQuery);
            ps.setString(1, username);
            ps.setString(2, password);
            ArrayList<Employee> elist = getResult(ps.executeQuery());
            ps.close();
            return elist;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }

    public ArrayList<Employee> FindEmployeeById(int employeeId) {
        DBConnectionFactory db = DBConnectionFactory.getInstance();
        Connection conn = db.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(SelectOneByEmployeeId);
            ps.setInt(1, employeeId);
            ArrayList<Employee> elist = getResult(ps.executeQuery());
            ps.close();
            return elist;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }

    public ArrayList<Employee> FindAllEmployee(int employeeId) {
        DBConnectionFactory db = DBConnectionFactory.getInstance();
        Connection conn = db.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(SelectAllEmployees);
            ArrayList<Employee> elist = getResult(ps.executeQuery());
            ps.close();
            return elist;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }

    public int AddEmployee(Employee employee) {
        try {
            DBConnectionFactory db = DBConnectionFactory.getInstance();
            Connection conn = db.getConnection();

            PreparedStatement ps = conn.prepareStatement(AddQuery);
            ps.setInt(1, employee.EmployeeId);
            ps.setString(2, employee.LastName);
            ps.setString(3, employee.FirstName);
            ps.setString(4, employee.Email);
            ps.setString(5, employee.ContactNumber);
            ps.setString(6, employee.Division);
            ps.setString(7, employee.CivilStatus);
            ps.setObject(8, employee.BirthDate);
            ps.setString(9, employee.Gender);
            ps.setString(10, employee.EmployeeStatus);
            ps.setString(11, employee.Username);
            ps.setString(12, employee.Password);
            ps.setObject(13, employee.StartDate);
            ps.setObject(14, employee.EndDate);
            ps.setInt(15, employee.Flag);

            int result = ps.executeUpdate();
            conn.close();
            ps.close();
            return result;

        } catch (SQLException e) {
            System.err.println(e);
        }
        return 0;
    }

    private ArrayList<Employee> getResult(ResultSet rs) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        while (rs.next()) {
            Employee e = new Employee();
            e.BirthDate = rs.getDate(Employee.COLUMN_BIRTHDATE);
            e.CivilStatus = rs.getString(Employee.COLUMN_CIVIL_STATUS);
            e.ContactNumber = rs.getString(Employee.COLUMN_CONTACT_NUMBER);
            e.Division = rs.getString(Employee.COLUMN_DIVISION);
            e.Email = rs.getString(Employee.COLUMN_EMAIL);
            e.EmployeeId = rs.getInt(Employee.COLUMN_EMPLOYEE_ID);
            e.EmployeeStatus = rs.getString(Employee.COLUMN_EMPLOYEE_STATUS);
            e.EndDate = rs.getDate(Employee.COLUMN_END_DATE);
            e.FirstName = rs.getString(Employee.COLUMN_FIRST_NAME);
            e.Flag = rs.getInt(Employee.COLUMN_FLAG);
            e.Gender = rs.getString(Employee.COLUMN_GENDER);
            e.LastName = rs.getString(Employee.COLUMN_LAST_NAME);
            e.Password = rs.getString(Employee.COLUMN_PASSWORD);
            e.StartDate = rs.getDate(Employee.COLUMN_START_DATE);
            e.Username = rs.getString(Employee.COLUMN_USERNAME);
        }
        rs.close();
        return employees;
    }
}