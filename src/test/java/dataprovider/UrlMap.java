package dataprovider;

import java.util.HashMap;
import java.util.Map;

public class UrlMap {
    public static Map<String, String[]> URLS = new HashMap() {
        {
            put("create", new  String[] {"https://playground.learnqa.ru/api/user/", "post"} );
            put("login", new  String[] {"https://playground.learnqa.ru/api/user/login", "post"});
            put("delete", new  String[] {"https://playground.learnqa.ru/api/user/%s", "delete"});
            put("edit", new  String[] {"https://playground.learnqa.ru/api/user/%s", "put"});
            put("getData", new String[] {"https://playground.learnqa.ru/api/user/%s", "get"});
            put("getAuth", new String[] {"https://playground.learnqa.ru/api/user/auth", "get"});
        }
    };
}
