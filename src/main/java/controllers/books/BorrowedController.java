package controllers.books;

import controllers.Controller;
import exceptions.DBMapperException;
import exceptions.HttpMethodNotAllowedException;
import help.UserHelp;
import models.DBModel;
import models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class BorrowedController extends Controller {
    public BorrowedController() {
        super();
        tpl = "books/borrowed.jsp";
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ANGEMELDET;
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) throws DBMapperException {
        HttpSession session = req.getSession();
        User user = UserHelp.getUser(session);
        paginationHelp(req, user.getRelCount("books"));
        int perPage = (int) req.getAttribute("perPage");
        int currentPage = (int) req.getAttribute("currentPage");

        List<DBModel> books = (List) user.getRelValue("books", perPage, currentPage);
        req.setAttribute("books", books);
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
