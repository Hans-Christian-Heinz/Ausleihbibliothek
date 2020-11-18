<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 17.11.20
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="models.User" %>
<%@ page import="models.DBModel" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Book" %>
<%@ page import="help.UserHelp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    User user = UserHelp.getUser(session);
    List<DBModel> books = (List<DBModel>) request.getAttribute("books");
%>

<div class="card">
    <div class="card-header">
        <h3>Von Ihnen ausgeliehene Bücher</h3>
    </div>
    <div class="card-body">
        <table class="table table-bordered table-hover table-striped text-center">
            <tr>
                <th style="width: 45%">Name</th>
                <th style="width: 45%">Autor</th>
                <th style="width: 10%"></th>
            </tr>
            <% for (DBModel model : books) { %>
            <% Book book = (Book) model; %>
            <tr>
                <td><%= book.getName() %></td>
                <td><%= book.getAuthor() %></td>
                <td>
                    <form method="post" action="<%= prefix %>/books/borrow">
                        <input type="hidden" name="id" value="<%= book.getId() %>"/>
                        <input type="hidden" name="user_id" value="<%= book.getAusgeliehenVon() == null ? "0" : book.getAusgeliehenVon() %>"/>
                        <% if (book.getAusgeliehenVon() == null) { %>
                        <input type="submit" class="btn btn-sm btn-outline-primary" value="Ausleihen"/>
                        <% } else if (book.getAusgeliehenVon().equals(user.getId())) { %>
                        <input type="submit" class="btn btn-sm btn-outline-primary" value="Zurückgeben"/>
                        <% } %>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
</div>

<jsp:include page="/pagination.jsp">
    <jsp:param name="href" value="/books/borrowed"/>
</jsp:include>