<%-- 
    Document   : list
    Created on : Oct 21, 2018, 6:31:23 PM
    Author     : RubySenpaii
--%>

<%@page import="objects.PurchaseOrder"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Asset Management - List Purchase Order</title>
        <jsp:include page="../../shared/css.jsp"/>
    </head>

    <body>
        <section id="container">
            <jsp:include page="../../shared/header.jsp"/>
            <jsp:include page="../../shared/sidebar.jsp"/>
            <!--main content start-->
            <section id="main-content">
                <section class="wrapper">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="content-panel">
                                <h4>List of Purchase Order</h4>
                                <br/>
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Purchase Order No</th>
                                            <th>Supplier Name</th>
                                            <th>Order Date</th>
                                            <th>Delivery Address</th>
                                            <th>Delivery Date</th>
                                            <th>Delivery Terms</th>
                                            <th>Payment Terms</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            ArrayList<PurchaseOrder> PO = (ArrayList<PurchaseOrder>) session.getAttribute("PO");
                                            for (PurchaseOrder po : PO) {
                                        %>
                                        <tr>
                                            <td><c:out value="<%= po.PurchaseOrderNumber%>"></c:out></td>
                                            <td><c:out value="<%= po.Supplier.SupplierName%>"></c:out></td>
                                            <td><c:out value="<%= po.OrderDate%>"></c:out></td>
                                            <td><c:out value="<%= po.DeliveryAddress%>"></c:out></td>
                                            <td><c:out value="<%= po.DeliveryDate%>"></c:out></td>
                                            <td><c:out value="<%= po.DeliveryTerms%>"></c:out></td>
                                            <td><c:out value="<%= po.PaymentTerm%>"></c:out></td>
                                            <td></td>
                                        </tr>
                                        <%
                                            }
                                        %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <!-- /row -->
                </section>
            </section>
            <!--main content end-->
            <jsp:include page="../../shared/footer.jsp"/>
        </section>
    </body>
    <jsp:include page="../../shared/js.jsp"/>
</html>