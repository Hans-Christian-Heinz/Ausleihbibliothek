package controllers.auth;

import controllers.Controller;
import help.MappersHelper;
import help.PasswordNew;
import mappers.UserMapper;
import models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterController extends Controller {
    public RegisterController() {
        super();
        tpl = "auth/register.jsp";
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
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserMapper mapper = MappersHelper.userMapper;
        //Validierung geschieht schon in der Elternklasse
        try {
            User user = new User();
            user.setVorname(req.getParameter("vorname"));
            user.setUsername(req.getParameter("username"));
            user.setName(req.getParameter("name"));
            //user.setPassword(Password.getSaltedHash(req.getParameter("password")));
            user.setPassword(PasswordNew.generateStorngPasswordHash(req.getParameter("password")));

            mapper.insert(user);

            req.getSession().setAttribute("user_id", user.getId());
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
