package help;

import exceptions.DBMapperException;
import models.User;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

public class UserHelp {
    private static User user;

    public static User getUser(HttpSession session) {
        if (session != null && user == null && session.getAttribute("user_id") != null) {
            BigInteger id = (BigInteger) session.getAttribute("user_id");
            try {
                user = (User) MappersHelper.userMapper.getById(id.longValue());
            }
            catch (DBMapperException e) {
                //todo
                user = null;
            }
        }

        return user;
    }

    public static void refreshUser(HttpSession session) {
        if (session != null && session.getAttribute("user_id") != null) {
            BigInteger id = (BigInteger) session.getAttribute("user_id");
            try {
                user = (User) MappersHelper.userMapper.getById(id.longValue());
            }
            catch (DBMapperException e) {
                //todo
                user = null;
            }
        }
        else {
            user = null;
        }
    }
}
