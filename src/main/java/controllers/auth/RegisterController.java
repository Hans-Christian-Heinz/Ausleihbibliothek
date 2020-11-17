package controllers.auth;

import controllers.Controller;
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
        berechtigung = Zugang.GAST;
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserMapper mapper = new UserMapper();
        //Validierung geschieht schon in der Elternklasse
        try {
            User user = new User();
            user.setVorname(req.getParameter("vorname"));
            user.setUsername(req.getParameter("username"));
            user.setName(req.getParameter("name"));
            //user.setPassword(Password.getSaltedHash(req.getParameter("password")));
            user.setPassword(PasswordNew.generateStorngPasswordHash(req.getParameter("password")));

            mapper.insert(user);

            req.getSession().setAttribute("user", user);
            resp.sendRedirect("home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
