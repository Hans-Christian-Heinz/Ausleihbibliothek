<%@ page import="help.CSRFHelper" %>
<%@ page import="org.owasp.encoder.Encode" %><%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 16.11.20
  Time: 12:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
%>

<div class="modal fade" id="deleteUserModal<%= request.getParameter("uid") %>" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Benutzerprofil löschen</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p class="text-justify">
                    Sind Sie sicher, dass Sie das Benutzerprofil von <%= Encode.forHtml(request.getParameter("fullName")) %> löschen möchten?
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-link text-secondary" data-dismiss="modal">Abbrechen</button>

                <!-- Im Skript delete.js wird eine delete-request gesendet -->
                <a type="button" href="<%= prefix %>/admin/users?uid=<%= request.getParameter("uid") %>"
                   class="btn btn-danger delete-link">
                    Benutzerprofil löschen
                </a>
            </div>
        </div>
    </div>
</div>