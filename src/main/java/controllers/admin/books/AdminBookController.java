package controllers.admin.books;

import controllers.Controller;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBMapper mapper = new BookMapper();
        List<DBModel> books = mapper.getAll(db);
        req.setAttribute("books", books);

        super.doGet(req, resp);
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DBMapper mapper = new BookMapper();
        Book book = new Book();
        book.setAuthor(req.getParameter("author"));
        book.setName(req.getParameter("name"));

        try {
            mapper.insert(db, book);
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
