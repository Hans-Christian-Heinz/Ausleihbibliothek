package controllers.profile;

import controllers.Controller;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class EditProfileController extends Controller {
    public EditProfileController() {
        super();
        berechtigung = Zugang.ANGEMELDET;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User user = (User) req.getSession().getAttribute("user");
            user.setName(req.getParameter("name"));
            user.setUsername(req.getParameter("username"));
            user.setVorname(req.getParameter("vorname"));

            user.update(db);

            //redirect back
            resp.sendRedirect((String) req.getAttribute("redirect"));
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
