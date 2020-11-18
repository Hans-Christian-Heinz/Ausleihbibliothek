package help;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

/**
 * Kopiert von https://m.heise.de/developer/artikel/Sichere-Java-Webanwendungen-Teil-2-Cross-Site-Request-Forgery-2303228.html?seite=all
 */
public final class CSRFHelper {
    public static final String CSRF_TOKEN = "_csrf";

    private static String getToken() throws Exception {
        SecureRandom sr = SecureRandom.
                getInstance("SHA1PRNG", "SUN");
        sr.nextBytes(new byte[20]);
        return String.valueOf(sr.nextLong());
    }

    public static String getToken(HttpSession session)
            throws Exception {
        if (session == null) {
            throw new ServletException("No session");
        }

        String token = (String) session.
                getAttribute(CSRF_TOKEN);

        if (token == null || token.isEmpty() || token.trim().isEmpty()) {
            token = getToken();
            session.setAttribute(CSRF_TOKEN, token);
        }

        return token;
    }

    public static boolean isValid(HttpServletRequest req)
            throws Exception {
        if (req.getSession(false) == null) {
            throw new ServletException("No session");
        }

        return getToken(req.getSession(false)).equals(req.getParameter(CSRF_TOKEN));
    }
}
