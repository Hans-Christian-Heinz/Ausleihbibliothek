package controllers;

import java.lang.reflect.*;

import db.DatabaseHelper;
import exceptions.DBMapperException;
import exceptions.HttpMethodNotAllowedException;
import help.CSRFHelper;
import help.UserHelp;
import models.User;
import validators.Validator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller extends HttpServlet {
    protected String tpl;
    protected final Connection db;
    protected Zugang berechtigung;
    protected String accessMsg;

    protected enum Zugang {
        ALLE,
        GAST,
        ANGEMELDET,
        ADMIN
    }

    public Controller() {
        db = DatabaseHelper.getConnection();
        if (db == null) {
            this.tpl = "errors/dbAccess.jsp";
        }
         initBerechtigung();
    }

    protected abstract void initBerechtigung();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //this does make sense but I don't want to bother conforming to the rules for an exercise-project
        /*resp.setHeader("Content-Security-Policy",
                "default-src 'self'; " +
                        "img-src 'none'; " +
                        "object-src 'none'; " +
                        "script-src 'self' code.jquery.com cdn.jsdelivr.net; " +
                        "style-src 'self' cdn.jsdelivr.net unsafe-inline"
        );*/

        HttpSession session = req.getSession();
        UserHelp.refreshUser(session);
        if (! istBerechtigt(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, accessMsg);
            return;
        }

        //get the contextPath of the servlet
        req.setAttribute("contextPath", req.getContextPath());
        session.setAttribute("uri", req.getRequestURI());

        //if (istBerechtigt(session)) {
            ServletContext context = this.getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher("/layout.jsp");

            if (session.getAttribute("keepErrors") != null && (boolean) session.getAttribute("keepErrors")) {
                session.setAttribute("keepErrors", false);
            }
            else {
                session.setAttribute("errors", new HashMap<>());
                session.setAttribute("old", new HashMap<>());
            }

        try {
            handleGet(req, resp);
            req.setAttribute("tpl", this.tpl);
            dispatcher.forward(req, resp);
        } catch (DBMapperException e) {
            req.setAttribute("tpl", "errors/mapperError.jsp");
            req.setAttribute("message", e.getMessage());

            dispatcher.forward(req, resp);
        }
        /*}
        else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }*/
    }

    protected abstract void handleGet(HttpServletRequest req, HttpServletResponse resp) throws DBMapperException;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*resp.setHeader("Content-Security-Policy",
                "default-src 'self'; " +
                        "img-src 'none'; " +
                        "object-src 'none'; " +
                        "script-src 'self' code.jquery.com cdn.jsdelivr.net; " +
                        "style-src * unsafe-inline"
        );*/

        HttpSession session = req.getSession();
        UserHelp.refreshUser(session);
        if (! istBerechtigt(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, accessMsg);
            return;
        }

        try {
            if (!CSRFHelper.isValid(req)) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Es ist kein gültiges CSRF-Token vorhanden.");
                return;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Es ist kein gültiges CSRF-Token vorhanden.");
            return;
        }

        String redirect = (String) session.getAttribute("uri");
        if (redirect == null) {
            redirect = req.getRequestURI();
        }
        session.setAttribute("uri", req.getRequestURI());
        req.setAttribute("redirect", redirect);

        //get the contextPath  of the servlet
        req.setAttribute("contextPath", req.getContextPath());

        //if (istBerechtigt(req.getSession())) {
        String classname = this.getClass().getCanonicalName();
        String validatorname = classname.replaceFirst("controllers", "validators");
        validatorname = validatorname.replaceAll("Controller", "Validator");
        Validator validator;
        try {
            Constructor<Validator> constr = (Constructor<Validator>) Class.forName(validatorname).getDeclaredConstructors()[0];
            validator = constr.newInstance(req.getParameterMap(), UserHelp.getUser(session));
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Kein Validator gefunden: POST-Request nicht erlaubt.");
            return;
        }

        req.setAttribute("tpl", this.tpl);

        try {
            if (validator.validate()) {
                req.setAttribute("errors", new HashMap<>());
                req.setAttribute("old", new HashMap<>());

                this.handlePost(req, resp);
            }
            else {
                session.setAttribute("errors", validator.getErrors());

                Map<String, String> old = new HashMap<>();
                Enumeration<String> names = req.getParameterNames();
                while (names.hasMoreElements()) {
                    String key = names.nextElement();
                    old.put(key, req.getParameter(key));
                }
                session.setAttribute("old", old);

                //redirect back
                //Stelle sicher, dass errors und old in der Session nicht verworfen werden
                session.setAttribute("keepErrors", true);
                resp.sendRedirect(redirect);
            }
        } catch (DBMapperException e) {
            req.setAttribute("tpl", "errors/mapperError.jsp");
            req.setAttribute("message", e.getMessage());

            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/layout.jsp");
            dispatcher.forward(req, resp);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            //todo
            //exception while hashing pwd
            e.printStackTrace();
        }
        /*}
        else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }*/
    }

    /**
     * Hilfsmethode für Pagination
     *
     * @param req
     * @param count Die Gesamtanzahl der anzuzeigenden Elemente
     */
    protected void paginationHelp(HttpServletRequest req, int count) {
        int perPage;
        int currentPage;
        if (req.getParameter("perPage") == null) {
            perPage = 10;
        }
        else {
            perPage = Integer.parseInt(req.getParameter("perPage"));
        }
        if (req.getParameter("currentPage") == null) {
            currentPage = 0;
        }
        else {
            currentPage = Integer.parseInt(req.getParameter("currentPage")) - 1;
        }
        int totalPages = count/perPage;
        if (count%perPage > 0) {
            totalPages++;
        }

        req.setAttribute("perPage", perPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPage", currentPage);
    }

    protected abstract void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, DBMapperException, InvalidKeySpecException, NoSuchAlgorithmException;

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //this does make sense but I don't want to bother conforming to the rules for an exercise-project
        /*resp.setHeader("Content-Security-Policy",
                "default-src 'self'; " +
                        "img-src 'none'; " +
                        "object-src 'none'; " +
                        "script-src 'self' code.jquery.com cdn.jsdelivr.net; " +
                        "style-src 'self' cdn.jsdelivr.net unsafe-inline"
        );*/

        HttpSession session = req.getSession();
        UserHelp.refreshUser(session);
        if (! istBerechtigt(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, accessMsg);
            return;
        }

        try {
            handleDelete(req, resp);
        } catch (HttpMethodNotAllowedException e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (DBMapperException e) {
            req.setAttribute("tpl", "errors/mapperError.jsp");
            req.setAttribute("message", e.getMessage());

            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/layout.jsp");
            dispatcher.forward(req, resp);
        }
    }

    protected abstract void handleDelete(HttpServletRequest req, HttpServletResponse resp)
            throws DBMapperException, HttpMethodNotAllowedException, IOException;

    private boolean istBerechtigt(HttpSession session) {
        User user = UserHelp.getUser(session);
        switch (berechtigung) {
            case GAST:
                accessMsg = "Angemeldete Benutzer dürfen nicht auf diese Seite zugreifen.";
                return user == null;
            case ANGEMELDET:
                accessMsg = "Nur angemeldete Benutzer dürfen auf diese Seite zugreifen.";
                return user != null;
            case ADMIN:
                accessMsg = "Nur Administratoren dürfen auf diese Seite zugreifen.";
                return user != null && "admin".equals(user.getRole());
            default:
                return true;
        }
    }
}
