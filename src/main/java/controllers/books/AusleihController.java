package controllers.books;

import controllers.Controller;
import exceptions.DBMapperException;
import help.MappersHelper;
import help.UserHelp;
import mappers.BookMapper;
import mappers.DBMapper;
import mappers.UserMapper;
import models.Book;
import models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
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
        super();
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ANGEMELDET;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException, DBMapperException {
        DBMapper mapper = MappersHelper.bookMapper;
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
    }
}
