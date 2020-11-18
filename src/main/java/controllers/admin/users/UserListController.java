package controllers.admin.users;

import controllers.Controller;
import help.MappersHelper;
import mappers.UserMapper;
import models.DBModel;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserListController extends Controller {
    public UserListController() {
        super();
        tpl = "admin/users/list.jsp";
        berechtigung = Zugang.ADMIN;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserMapper mapper = MappersHelper.userMapper;

        paginationHelp(req, mapper.count());
        int perPage = (int) req.getAttribute("perPage");
        int currentPage = (int) req.getAttribute("currentPage");

        //List<DBModel> books = mapper.getAll();
        List<DBModel> users = mapper.getPagination(perPage, currentPage);
        req.setAttribute("users", users);

        super.doGet(req, resp);
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }
}
