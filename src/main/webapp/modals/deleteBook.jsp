<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 16.11.20
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="help.CSRFHelper" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
%>

<div class="modal fade" id="deleteBookModal<%= request.getParameter("bid") %>" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Buch löschen</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p class="text-justify">
                    Sind Sie sicher, dass sie das Buch <%= Encode.forHtml(request.getParameter("name")) %> löschen möchten?
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-link text-secondary" data-dismiss="modal">Abbrechen</button>

                <!-- Im Skript delete.js wird eine delete-request gesendet -->
                <a type="button" href="<%= prefix %>/admin/books?bid=<%= request.getParameter("bid") %>"
                   class="btn btn-danger delete-link">
                    Buch löschen
                </a>
            </div>
        </div>
    </div>
</div>