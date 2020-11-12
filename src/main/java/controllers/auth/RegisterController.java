package controllers.auth;

import controllers.Controller;
import help.PasswordNew;
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
        //Validierung geschieht schon in der Elternklasse
        try {
            User user = new User();
            user.setVorname(req.getParameter("vorname"));
            user.setUsername(req.getParameter("username"));
            user.setName(req.getParameter("name"));
            //user.setPassword(Password.getSaltedHash(req.getParameter("password")));
            user.setPassword(PasswordNew.generateStorngPasswordHash(req.getParameter("password")));

            user.insert(db);

            req.getSession().setAttribute("user", user);
            resp.sendRedirect("home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
