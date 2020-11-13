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
            //Wenn das Passwort nicht stimmt: redirect back
            else {
                HttpSession session = req.getSession();

                ((Map<String, String>)session.getAttribute("errors")).put("password", "Das Passwort stimmt nicht.");
                ((Map<String, String>)session.getAttribute("old")).put("username", req.getParameter("username"));
                session.setAttribute("keepErrors", true);
                resp.sendRedirect((String) req.getAttribute("redirect"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
