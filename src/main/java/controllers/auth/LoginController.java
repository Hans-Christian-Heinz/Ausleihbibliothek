package controllers.auth;

import controllers.Controller;
import help.MappersHelper;
import help.PasswordNew;
import mappers.UserMapper;
import models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        UserMapper mapper = MappersHelper.userMapper;
        try {
            User user = (User) mapper.getByKey("username", req.getParameter("username"));
            if (PasswordNew.validatePassword(req.getParameter("password"), user.getPassword())) {
                HttpSession session = req.getSession();
                session.setAttribute("user_id", user.getId());

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
