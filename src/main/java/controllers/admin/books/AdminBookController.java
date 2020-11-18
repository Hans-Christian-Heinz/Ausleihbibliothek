package controllers.admin.books;

import controllers.Controller;
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
        berechtigung = Zugang.ADMIN;
        tpl = "admin/books/index.jsp";
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        DBMapper mapper = MappersHelper.bookMapper;

        paginationHelp(req, mapper.count());
        int perPage = (int) req.getAttribute("perPage");
        int currentPage = (int) req.getAttribute("currentPage");

        //List<DBModel> books = mapper.getAll();
        List<DBModel> books = mapper.getPagination(perPage, currentPage);
        req.setAttribute("books", books);
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DBMapper mapper = MappersHelper.bookMapper;
        Book book = new Book();
        book.setAuthor(req.getParameter("author"));
        book.setName(req.getParameter("name"));

        try {
            mapper.insert(book);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        //redirect back
        resp.sendRedirect((String) req.getAttribute("redirect"));
    }
}
