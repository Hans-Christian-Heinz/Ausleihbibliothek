package controllers.auth;

import controllers.Controller;
import exceptions.HttpMethodNotAllowedException;
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
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.GAST;
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
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
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws HttpMethodNotAllowedException {
        throw new HttpMethodNotAllowedException(HttpMethodNotAllowedException.Methods.DELETE);
    }
}
