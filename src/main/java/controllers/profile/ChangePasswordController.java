package controllers.profile;

import controllers.Controller;
import help.MappersHelper;
import help.PasswordNew;
import mappers.UserMapper;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class ChangePasswordController extends Controller {
    public ChangePasswordController() {
        super();
        berechtigung = Zugang.ANGEMELDET;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserMapper mapper = MappersHelper.userMapper;
        //first: validate the password_old (Rest der Validierung passiert in der Superklasse Controller
        HttpSession session = req.getSession();;
        User user = (User) session.getAttribute("user");
        try {
            if (PasswordNew.validatePassword(req.getParameter("password_old"), user.getPassword())) {
                user.setPassword(PasswordNew.generateStorngPasswordHash(req.getParameter("password")));
                mapper.update(user);
            }
            //Wenn das Passwort nicht stimmt: redirect back.
            else {
                ((Map<String, String>)session.getAttribute("errors")).put("password_old", "Das Passwort stimmt nicht.");
                ((Map<String, String>)session.getAttribute("errors")).put("modal", "changePwdModal");
                session.setAttribute("keepErrors", true);
            }
            //redirect back
            resp.sendRedirect((String) req.getAttribute("redirect"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
