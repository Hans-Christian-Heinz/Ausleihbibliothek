package controllers.books;

import controllers.Controller;
import help.MappersHelper;
import help.UserHelp;
import mappers.BookMapper;
import mappers.DBMapper;
import mappers.UserMapper;
import models.Book;
import models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AusleihController extends Controller {
    public AusleihController() {
        berechtigung = Zugang.ANGEMELDET;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //do nothing
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DBMapper mapper = MappersHelper.bookMapper;
        try {
            User user = UserHelp.getUser(req.getSession());
            Book book = (Book) mapper.getById(Long.parseLong(req.getParameter("id")));

            if (book.getAusgeliehenVon() == null) {
                book.setAusgeliehenVon(user.getId());
            }
            else {
                book.setAusgeliehenVon((BigInteger) null);
            }

            mapper.update(book);

            //redirect back
            resp.sendRedirect((String) req.getAttribute("redirect"));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
