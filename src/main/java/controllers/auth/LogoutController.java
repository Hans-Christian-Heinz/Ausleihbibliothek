package controllers.auth;

import controllers.Controller;
import help.UserHelp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutController extends Controller {
    public LogoutController() {
        super();
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ANGEMELDET;
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
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
