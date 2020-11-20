package controllers.admin.users;

import controllers.Controller;
import exceptions.DBMapperException;
import exceptions.HttpMethodNotAllowedException;
import help.MappersHelper;
import mappers.UserMapper;
import models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ChangeRoleController extends Controller {
    public ChangeRoleController() {
        super();
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ADMIN;
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
        User user = (User) mapper.getById(Long.parseLong(req.getParameter("id")));
        if (user.getRole() == null) {
            user.setRole("admin");
        }
        else {
            user.setRole(null);
        }

        mapper.update(user);

        //redirect back
        resp.sendRedirect((String) req.getAttribute("redirect"));
    }

    @Override
    protected void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws HttpMethodNotAllowedException {
        throw new HttpMethodNotAllowedException(HttpMethodNotAllowedException.Methods.DELETE);
    }
}
