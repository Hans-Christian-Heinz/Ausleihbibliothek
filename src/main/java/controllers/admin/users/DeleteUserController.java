package controllers.admin.users;

import controllers.Controller;
import mappers.UserMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

public class DeleteUserController extends Controller {
    public DeleteUserController() {
        super();
        berechtigung = Zugang.ADMIN;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        UserMapper mapper = new UserMapper();
        mapper.delete(db, BigInteger.valueOf(id));

        //redirect back
        resp.sendRedirect((String) req.getAttribute("redirect"));
    }
}
