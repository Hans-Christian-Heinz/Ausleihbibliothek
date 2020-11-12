package controllers.auth;

import controllers.Controller;
import help.PasswordNew;
import models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class LoginController extends Controller {
    public LoginController() throws ClassNotFoundException, SQLException {
        super();
        tpl = "auth/login.jsp";
        berechtigung = Zugang.GAST;
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User();
        user.getByKey("username", req.getParameter("username"), db);
        try {
            if (PasswordNew.validatePassword(req.getParameter("password"), user.getPassword())) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);

                resp.sendRedirect("home");
            }
            //Wenn das Passwort nicht stimmt, wird hier die Funktionalität von Controller doGet() nachgeahmt.
            else {
                ServletContext context = this.getServletContext();
                RequestDispatcher dispatcher = context.getRequestDispatcher("/layout.jsp");

                req.setAttribute("tpl", this.tpl);
                ((Map<String, String>)req.getAttribute("errors")).put("password", "Das Passwort stimmt nicht.");
                ((Map<String, String>)req.getAttribute("old")).put("username", req.getParameter("username"));
                dispatcher.forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
