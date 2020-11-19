package controllers.admin.books;

import controllers.Controller;
import exceptions.DBMapperException;
import help.MappersHelper;
import mappers.BookMapper;
import mappers.DBMapper;
import models.Book;
import models.DBModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class AdminBookController extends Controller {
    public AdminBookController() {
        super();
        tpl = "admin/books/index.jsp";
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ADMIN;
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) throws DBMapperException {
        DBMapper mapper = MappersHelper.bookMapper;

        paginationHelp(req, mapper.count());
        int perPage = (int) req.getAttribute("perPage");
        int currentPage = (int) req.getAttribute("currentPage");

        //List<DBModel> books = mapper.getAll();
        List<DBModel> books = mapper.getPagination(perPage, currentPage);
        req.setAttribute("books", books);
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException, DBMapperException {
        DBMapper mapper = MappersHelper.bookMapper;
        Book book = new Book();
        book.setAuthor(req.getParameter("author"));
        book.setName(req.getParameter("name"));

        mapper.insert(book);

        //redirect back
        resp.sendRedirect((String) req.getAttribute("redirect"));
    }
}
