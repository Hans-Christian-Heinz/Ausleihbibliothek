<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 19.11.20
  Time: 13:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="alert alert-danger mb-0">
    <p>
        <b>In der Persistierung zwischen Modellen und Datenbank ist ein Fehler aufgetreten.</b>
        <br/>
        <%= Encode.forHtml((String) request.getAttribute("message")) %>
    </p>
</div>