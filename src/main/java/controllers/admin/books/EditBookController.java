package controllers.admin.books;

import controllers.Controller;
import exceptions.DBMapperException;
import help.MappersHelper;
import mappers.DBMapper;
import models.Book;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EditBookController extends Controller {
    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ADMIN;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //get not implemented
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp){
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException, DBMapperException {
        DBMapper mapper = MappersHelper.bookMapper;
        String id = req.getParameter("id");
        Book book = (Book) mapper.getById(Long.parseLong(id));
        book.setAuthor(req.getParameter("author"));
        book.setName(req.getParameter("name"));

        mapper.update(book);

        //redirect back
        resp.sendRedirect((String) req.getAttribute("redirect"));
    }
}
