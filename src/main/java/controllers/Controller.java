package controllers;

import java.lang.reflect.*;
import db.DatabaseHelper;
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
    protected Connection db;
    protected Zugang berechtigung;

    protected enum Zugang {
        ALLE,
        GAST,
        ANGEMELDET,
        ADMIN
    };

    public Controller() {
        try {
            this.db = DatabaseHelper.getConnection();
        } catch(ClassNotFoundException | SQLException e) {
            this.tpl = "errors/dbAccess.jsp";
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (istBerechtigt(req.getSession())) {
            ServletContext context = this.getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher("/layout.jsp");

            req.setAttribute("tpl", this.tpl);
            req.setAttribute("errors", new HashMap<>());
            req.setAttribute("old", new HashMap<>());

            dispatcher.forward(req, resp);
        }
        else {
            resp.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (istBerechtigt(req.getSession())) {
            String classname = this.getClass().getCanonicalName();
            String validatorname = classname.replaceFirst("controllers", "validators");
            validatorname = validatorname.replaceAll("Controller", "Validator");
            Validator validator;
            try {
                Constructor<Validator> constr = (Constructor<Validator>) Class.forName(validatorname).getDeclaredConstructors()[0];
                validator = constr.newInstance(req.getParameterMap(), db);
            }
            catch (Exception e) {
                validator = new FalseValidator(req.getParameterMap(), db);
            }

            req.setAttribute("tpl", this.tpl);

            if (validator.validate()) {
                req.setAttribute("errors", new HashMap<>());
                req.setAttribute("old", new HashMap<>());

                this.handlePost(req, resp);
            }
            else {
                ServletContext context = this.getServletContext();
                RequestDispatcher dispatcher = context.getRequestDispatcher("/layout.jsp");

                req.setAttribute("tpl", this.tpl);
                req.setAttribute("errors", validator.getErrors());

                Map<String, String> old = new HashMap<>();
                Enumeration<String> names = req.getParameterNames();
                while (names.hasMoreElements()) {
                    String key = names.nextElement();
                    old.put(key, req.getParameter(key));
                }
                req.setAttribute("old", old);

                dispatcher.forward(req, resp);
            }
        }
        else {
            resp.sendRedirect("home");
        }
    }

    protected abstract void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException;

    private boolean istBerechtigt(HttpSession session) {
        User user = (User)session.getAttribute("user");
        switch (berechtigung) {
            case GAST:
                return user == null;
            case ANGEMELDET:
                return user != null;
            case ADMIN:
                return user != null && user.getRole().equals("Admin");
            default:
                return true;
        }
    }
}
