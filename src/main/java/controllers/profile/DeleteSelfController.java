package controllers.profile;

import controllers.Controller;
import exceptions.DBMapperException;
import help.MappersHelper;
import help.UserHelp;
import mappers.UserMapper;
import models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;

public class DeleteSelfController extends Controller {
    @Override
    protected void initBerechtigung() {
        berechtigung = Zugang.ANGEMELDET;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //get not implemented
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    @Override
    protected void handleGet(HttpServletRequest req, HttpServletResponse resp) {
        //do nothing
    }

    @Override
    protected void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException, DBMapperException {
        HttpSession session = req.getSession();
        User user = UserHelp.getUser(session);

        UserMapper mapper = MappersHelper.userMapper;
        mapper.delete(BigInteger.valueOf(user.getId().longValue()));
        session.invalidate();

        //redirect back
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
