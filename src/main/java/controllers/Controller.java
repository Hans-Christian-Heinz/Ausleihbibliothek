package controllers;

import java.lang.reflect.*;
import db.DatabaseHelper;
import help.CSRFHelper;
import help.UserHelp;
import models.User;
import validators.FalseValidator;
import validators.Validator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller extends HttpServlet {
    protected String tpl;
    protected final Connection db;
    protected Zugang berechtigung;

    protected enum Zugang {
        ALLE,
        GAST,
        ANGEMELDET,
        ADMIN
    };

    public Controller() {
        db = DatabaseHelper.getConnection();
         if (db == null) {
            this.tpl = "errors/dbAccess.jsp";
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserHelp.refreshUser(session);

        //get the contextPath of the servlet
        req.setAttribute("contextPath", req.getContextPath());
        session.setAttribute("uri", req.getRequestURI());

        if (istBerechtigt(session)) {
            ServletContext context = this.getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher("/layout.jsp");

            req.setAttribute("tpl", this.tpl);
            if (session.getAttribute("keepErrors") != null && (boolean) session.getAttribute("keepErrors")) {
                session.setAttribute("keepErrors", false);
            }
            else {
                session.setAttribute("errors", new HashMap<>());
                session.setAttribute("old", new HashMap<>());
            }

            handleGet(req, resp);

            dispatcher.forward(req, resp);
        }
        else {
            resp.sendRedirect("home");
        }
    }

    protected abstract void handleGet(HttpServletRequest req, HttpServletResponse resp);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        HttpSession session = req.getSession();
        UserHelp.refreshUser(session);

        String redirect = (String) session.getAttribute("uri");
        if (redirect == null) {
            redirect = req.getRequestURI();
        }
        session.setAttribute("uri", req.getRequestURI());
        req.setAttribute("redirect", redirect);

        //get the contextPath  of the servlet
        req.setAttribute("contextPath", req.getContextPath());

        if (istBerechtigt(req.getSession())) {
            String classname = this.getClass().getCanonicalName();
            String validatorname = classname.replaceFirst("controllers", "validators");
            validatorname = validatorname.replaceAll("Controller", "Validator");
            Validator validator;
            try {
                Constructor<Validator> constr = (Constructor<Validator>) Class.forName(validatorname).getDeclaredConstructors()[0];
                validator = constr.newInstance(req.getParameterMap(), UserHelp.getUser(session));
            }
            catch (Exception e) {
                validator = new FalseValidator(req.getParameterMap(), UserHelp.getUser(session));
            }

            req.setAttribute("tpl", this.tpl);

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
        }
        else {
            resp.sendRedirect("home");
        }
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

    protected abstract void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException;

    private boolean istBerechtigt(HttpSession session) {
        User user = UserHelp.getUser(session);
        switch (berechtigung) {
            case GAST:
                return user == null;
            case ANGEMELDET:
                return user != null;
            case ADMIN:
                return user != null && "admin".equals(user.getRole());
            default:
                return true;
        }
    }
}
