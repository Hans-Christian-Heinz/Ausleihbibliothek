package controllers.admin;

import controllers.Controller;
import exceptions.HttpMethodNotAllowedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StdController extends Controller {
    public StdController() {
        super();
        tpl = "admin/std.jsp";
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ADMIN;
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws HttpMethodNotAllowedException {
        throw new HttpMethodNotAllowedException(HttpMethodNotAllowedException.Methods.DELETE);
    }
}
