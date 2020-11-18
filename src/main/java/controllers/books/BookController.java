package controllers.books;

import controllers.Controller;
import help.MappersHelper;
import mappers.BookMapper;
import mappers.DBMapper;
import models.DBModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BookController extends Controller {
    public BookController() {
        berechtigung = Zugang.ANGEMELDET;
        tpl = "books/index.jsp";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBMapper mapper = MappersHelper.bookMapper;

        paginationHelp(req, mapper.count());
        int perPage = (int) req.getAttribute("perPage");
        int currentPage = (int) req.getAttribute("currentPage");

        //List<DBModel> books = mapper.getAll();
        List<DBModel> books = mapper.getPagination(perPage, currentPage);
        req.setAttribute("books", books);

        super.doGet(req, resp);
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }
}
