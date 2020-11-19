package controllers.profile;

import controllers.Controller;
import exceptions.DBMapperException;
import help.MappersHelper;
import help.UserHelp;
import mappers.UserMapper;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class EditProfileController extends Controller {
    public EditProfileController() {
        super();
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ANGEMELDET;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get not implemented
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException, DBMapperException {
        UserMapper mapper = MappersHelper.userMapper;
        User user = UserHelp.getUser(req.getSession());
        user.setName(req.getParameter("name"));
        user.setUsername(req.getParameter("username"));
        user.setVorname(req.getParameter("vorname"));

        mapper.update(user);

        //redirect back
        resp.sendRedirect((String) req.getAttribute("redirect"));
    }
}
