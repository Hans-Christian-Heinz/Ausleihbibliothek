package controllers.books;

import controllers.Controller;
import help.UserHelp;
import models.DBModel;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class BorrowedController extends Controller {
    public BorrowedController() {
        super();
        berechtigung = Zugang.ANGEMELDET;
        tpl = "books/borrowed.jsp";
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = UserHelp.getUser(session);
        paginationHelp(req, user.getRelCount("books"));
        int perPage = (int) req.getAttribute("perPage");
        int currentPage = (int) req.getAttribute("currentPage");

        List<DBModel> books = (List) user.getRelValue("books", perPage, currentPage);
        req.setAttribute("books", books);
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //do nothing
    }
}
