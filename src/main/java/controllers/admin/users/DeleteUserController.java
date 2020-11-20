package controllers.admin.users;

import controllers.Controller;
import exceptions.DBMapperException;
import exceptions.HttpMethodNotAllowedException;
import help.MappersHelper;
import mappers.UserMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

public class DeleteUserController extends Controller {
    public DeleteUserController() {
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
        long id = Long.parseLong(req.getParameter("id"));
        UserMapper mapper = MappersHelper.userMapper;
        mapper.delete(BigInteger.valueOf(id));

        //redirect back
        resp.sendRedirect((String) req.getAttribute("redirect"));
    }

    @Override
    protected void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws HttpMethodNotAllowedException {
        throw new HttpMethodNotAllowedException(HttpMethodNotAllowedException.Methods.DELETE);
    }
}
