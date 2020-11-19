package controllers.admin.books;

import controllers.Controller;
import exceptions.DBMapperException;
import help.MappersHelper;
import mappers.BookMapper;
import mappers.DBMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

public class DeleteBookController extends Controller {
    public DeleteBookController() {
        super();
    }

    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ADMIN;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get not implemented
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException, DBMapperException {
        DBMapper mapper = MappersHelper.bookMapper;
        long id = Long.parseLong(req.getParameter("id"));
        mapper.delete(BigInteger.valueOf(id));

        //redirect back
        resp.sendRedirect((String) req.getAttribute("redirect"));
    }
}
