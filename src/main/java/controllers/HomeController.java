package controllers;

import help.UserHelp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class HomeController extends Controller {
    public HomeController() throws ClassNotFoundException, SQLException {
        super();
        tpl = "auth/login.jsp";
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ALLE;
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (UserHelp.getUser(session) != null) {
            tpl = "welcome.jsp";
        }
        else {
            tpl = "auth/login.jsp";
        }
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }
}
