package controllers.admin;

import controllers.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StdController extends Controller {
    public StdController() {
        super();
        tpl = "admin/std.jsp";
        berechtigung = Zugang.ADMIN;
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //do nothing
    }
}
