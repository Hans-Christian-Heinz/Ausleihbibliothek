package help;

import mappers.BookMapper;
import mappers.UserMapper;

public final class MappersHelper {
    public static final UserMapper userMapper = new UserMapper();
    public static final BookMapper bookMapper = new BookMapper();
}
