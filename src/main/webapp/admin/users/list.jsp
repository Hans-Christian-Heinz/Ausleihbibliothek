<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 13.11.20
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="models.User" %>
<%@ page import="models.DBModel" %>
<%@ page import="java.util.List" %>
<%@ page import="help.UserHelp" %>
<%@ page import="help.CSRFHelper" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    User user = UserHelp.getUser(session);
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
    List<DBModel> users = (List<DBModel>) request.getAttribute("users");
%>

<div class="card">
    <div class="card-header">
        <h3>Benutzerliste</h3>
    </div>
    <div class="card-body">
        <table class="table table-striped table-hover text-center">
            <tr>
                <th>Benutzername</th>
                <th>Name</th>
                <th>Rolle</th>
                <th>Löschen</th>
            </tr>
            <% for (DBModel model : users) { %>
                <% User u = (User) model; %>
                <tr id="tableRowUsers<%= u.getId() %>">
                    <td><%= Encode.forHtml(u.getUsername()) %></td>
                    <td><%= Encode.forHtml(u.getFullName()) %></td>
                    <td>
                        <%= "admin".equals(u.getRole()) ? "Admin" : "Benutzer" %>
                        <% if (! user.equals(u)) { %>
                            <form action="<%= prefix %>/admin/users/changeRole" method="post">
                                <input type="hidden" name="<%= CSRFHelper.CSRF_TOKEN %>" value="<%= CSRFHelper.getToken(session) %>"/>
                                <input type="hidden" name="id" value="<%= u.getId() %>"/>
                                <input type="submit" class="btn btn-small btn-outline-secondary"
                                       value="<%= "admin".equals(u.getRole()) ? "Profil abwerten" : "Profil aufwerten" %>"/>
                            </form>
                        <% } else if(errors.containsKey("id")) { %>
                            <p class="text-danger small">
                                <strong><%= errors.get("id") %></strong>
                            </p>
                        <% } %>
                    </td>
                    <td>
                        <% if (! user.equals(u)) { %>
                            <a href="#deleteUserModal<%= u.getId() %>" data-toggle="modal" class="btn btn-small btn-outline-danger">Löschen</a>
                        <% } %>
                    </td>
                </tr>
            <% } %>
        </table>
    </div>
</div>

<jsp:include page="/pagination.jsp">
    <jsp:param name="href" value="/admin/users"/>
</jsp:include>

<% for (DBModel model : users) { %>
<jsp:include page="/modals/deleteUser.jsp">
    <jsp:param name="fullName" value="<%= ((User)model).getFullName() %>"/>
    <jsp:param name="uid" value="<%= model.getId() %>"/>
</jsp:include>
<% } %>