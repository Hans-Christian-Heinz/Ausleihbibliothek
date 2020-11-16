package mappers;

import models.DBModel;
import models.User;

import java.util.HashMap;
import java.util.Map;

public class UserMapper extends DBMapper {
    @Override
    protected Class<? extends DBModel> stdClass() {
        return User.class;
    }

    @Override
    protected String stdTable() {
        return "users";
    }

    @Override
    protected Map<String, String> stdPropertyMap() {
        Map<String, String> res = new HashMap<>();
        res.put("id", "id");
        res.put("username", "username");
        res.put("vorname", "vorname");
        res.put("name", "name");
        res.put("role", "role");
        res.put("password", "password");
        return res;
    }
}
