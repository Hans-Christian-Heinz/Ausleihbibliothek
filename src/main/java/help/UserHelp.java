package help;

import models.User;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

public final class UserHelp {
    private static User user;

    public static User getUser(HttpSession session) {
        if (user == null && session.getAttribute("user_id") != null) {
            BigInteger id = (BigInteger) session.getAttribute("user_id");
            try {
                user = (User) MappersHelper.userMapper.getById(id.longValue());
            }
            catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                //todo
            }
        }

        return user;
    }

    public static void refreshUser(HttpSession session) {
        if (session.getAttribute("user_id") != null) {
            BigInteger id = (BigInteger) session.getAttribute("user_id");
            try {
                user = (User) MappersHelper.userMapper.getById(id.longValue());
            }
            catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                //todo
            }
        }
        else {
            user = null;
        }
    }
}
