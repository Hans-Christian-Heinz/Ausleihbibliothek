package controllers.profile;

import controllers.Controller;
import exceptions.HttpMethodNotAllowedException;
import help.MappersHelper;
import help.PasswordNew;
import help.UserHelp;
import mappers.UserMapper;
import models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class ChangePasswordController extends Controller {
    public ChangePasswordController() {
        super();
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ANGEMELDET;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //get not implemented
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) {
        UserMapper mapper = MappersHelper.userMapper;
        //first: validate the password_old (Rest der Validierung passiert in der Superklasse Controller
        HttpSession session = req.getSession();
        User user = UserHelp.getUser(session);
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
            //todo
            e.printStackTrace();
        }
    }

    @Override
    protected void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws HttpMethodNotAllowedException {
        throw new HttpMethodNotAllowedException(HttpMethodNotAllowedException.Methods.DELETE);
    }
}
