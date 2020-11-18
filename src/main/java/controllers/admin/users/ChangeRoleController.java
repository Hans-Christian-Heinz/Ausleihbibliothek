package controllers.admin.users;

import controllers.Controller;
import help.MappersHelper;
import mappers.UserMapper;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ChangeRoleController extends Controller {
    public ChangeRoleController() {
        super();
        berechtigung = Zugang.ADMIN;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //do nothing
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserMapper mapper = MappersHelper.userMapper;
        try {
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
