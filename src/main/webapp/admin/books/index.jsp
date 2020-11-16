<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 16.11.20
  Time: 13:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="models.User" %>
<%@ page import="java.util.List" %>
<%@ page import="models.DBModel" %>
<%@ page import="models.Book" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    User user = (User) session.getAttribute("user");
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
    List<DBModel> books = (List<DBModel>) request.getAttribute("books");
%>

<div class="card">
    <div class="card-header">
        <h3>Alle Bücher</h3>
    </div>
    <div class="card-body">
        <table class="table table-bordered table-hover text-center">
            <tr>
                <th><label for="name">Name</label></th>
                <th><label for="author">Autor</label></th>
                <th>Ausgeliehen von</th>
                <th>Löschen</th>
            </tr>
            <% for (DBModel model : books) { %>
                <% Book book = (Book) model; %>
                <tr>
                    <td><%= book.getName() %></td>
                    <td><%= book.getAuthor() %></td>
                    <td>TODO rel</td>
                    <td>
                        <a href="#deleteBookModal<%= model.getId() %>" data-toggle="modal" class="btn btn-sm btn-outline-danger">Löschen</a>
                    </td>
                </tr>
            <% } %>

            <tr>
                <td>
                    <input type="text" class="form-control <%= errors.containsKey("name") ? "is-invalid" : "" %>" name="name"
                           form="formAddBook" id="name" required placeholder="Name"/>
                    <%
                        if(errors.containsKey("name")) {
                    %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= errors.get("name") %></strong>
                        </span>
                    <%
                        }
                    %>
                </td>
                <td>
                    <input type="text" class="form-control <%= errors.containsKey("author") ? "is-invalid" : "" %>"
                           name="author" form="formAddBook" id="author" required placeholder="Autor"/>
                    <%
                        if(errors.containsKey("author")) {
                    %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= errors.get("author") %></strong>
                        </span>
                    <%
                        }
                    %>
                </td>
                <td></td>
                <td>
                    <form method="post" action="<%= prefix %>/admin/books" id="formAddBook">
                        <input type="submit" class="btn btn-primary" value="Hinzufügen"/>
                    </form>
                </td>
            </tr>
        </table>
    </div>
</div>

<% for (DBModel model : books) { %>
<jsp:include page="/modals/deleteBook.jsp">
    <jsp:param name="bid" value="<%= model.getId() %>"/>
    <jsp:param name="name" value="<%= ((Book)model).getName() %>"/>
</jsp:include>
<% } %>