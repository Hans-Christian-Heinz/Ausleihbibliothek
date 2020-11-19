package controllers.admin.users;

import controllers.Controller;
import exceptions.DBMapperException;
import help.MappersHelper;
import mappers.UserMapper;
import models.DBModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
}
