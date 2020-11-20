package controllers.admin.users;

import controllers.Controller;
import exceptions.DBMapperException;
import exceptions.HttpMethodNotAllowedException;
import help.MappersHelper;
import mappers.UserMapper;
import models.DBModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class UserListController extends Controller {
    public UserListController() {
        super();
        tpl = "admin/users/list.jsp";
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ADMIN;
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) throws DBMapperException {
        UserMapper mapper = MappersHelper.userMapper;

        paginationHelp(req, mapper.count());
        int perPage = (int) req.getAttribute("perPage");
        int currentPage = (int) req.getAttribute("currentPage");

        //List<DBModel> books = mapper.getAll();
        List<DBModel> users = mapper.getPagination(perPage, currentPage);
        req.setAttribute("users", users);
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws DBMapperException, IOException {
        long uid = Long.parseLong(req.getParameter("uid"));

        UserMapper mapper = MappersHelper.userMapper;
        mapper.delete(BigInteger.valueOf(uid));

        resp.addHeader("redirectTo", req.getContextPath() + "/admin/users");
        resp.getWriter().print(true);
    }
}
