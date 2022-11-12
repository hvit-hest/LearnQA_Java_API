import io.restassured.RestAssured;
import io.restassured.internal.path.xml.NodeBase;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FindPasswordTest {

    private String loginName = "super_admin";
    private String wikiUrl = "https://en.wikipedia.org/wiki/List_of_the_most_common_passwords";
    private String loginUrl = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
    private String checkCookieUrl = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";
    private String authCookieName = "auth_cookie";
    private String notAuthorizedMessage = "You are NOT authorized";

    @Test
    public void findPasswordTest() {

        /*It can be done in future:
        try to login without login field with password field only. 500?
        try to login with wrong login field without password field at all. 500?
        try to login without login and without password field. 500? */

        //get list of popular passwords from wiki
        Response response = RestAssured.get(wikiUrl).andReturn();
        /*Of course in such way we will get table's column with row numbers also. But who will care?
        We will check the cells as passwords too in order to save the time and money spent for the test development )))*/
        List<Object> tableContent = new XmlPath(XmlPath.CompatibilityMode.HTML, response.asString())
                .get("**.findAll{it.@class == 'wikitable'}[1].**.findAll{it.name()=='td'}");
        //Or alternatively. In this simple way we need a less study of "a GPath dialect for restAssure"
        /*List<Object> tableContent = new XmlPath(XmlPath.CompatibilityMode.HTML,response.asString())
                .getString("html.body.div.div.div.div.table[2].**.findAll{it.name()=='td'}");*/

        //Objects in List<Object> above can be String or NodeImpl with link [a]. We need Strings (items' values, i.e. text) only.
        List<String> tableContentList = tableContent.stream().map(i -> {
            if (!i.getClass().getTypeName().equals("io.restassured.internal.path.xml.NodeImpl"))
                return i.toString().trim();
            else return ((NodeBase) i).getProperty("value").toString().trim();
        }).collect(Collectors.toList());


        //What is inside? it has to be 250 the List's elements since the passwords' table has 250 cells
        //including one column/25 cells with rows numbers
        /*System.out.println(tableContentList.size() + "======List================");
        tableContentList.forEach(System.out::println);*/

        //Removing duplicates
        Set<String> targetSet = new HashSet<String>(tableContentList);

        //What is inside?
        /*System.out.println(targetSet.size() + "======Set================");
        targetSet.forEach(System.out::println);*/


        for (String psswd : targetSet) {
            String bodyParams = String.format("login=%s&password=%s", loginName, psswd);

            Response loginResponse = RestAssured
                    .given()
                    .body(bodyParams)
                    .post(loginUrl)
                    .andReturn();

            String authCookieValue = loginResponse.getCookie(authCookieName);

            XmlPath checkCookieResponse = RestAssured
                    .given()
                    .cookie(authCookieName, authCookieValue)
                    .get(checkCookieUrl)
                    .andReturn().htmlPath();

            String actualMessage = checkCookieResponse.getString("html.body");

            if (!actualMessage.equals(notAuthorizedMessage)) {
                System.out.println("===============Answer is found=================");
                System.out.println(String.format("Authorization message is '%s'", actualMessage));
                System.out.println(String.format("Password is '%s'", psswd));
                break;
            }
        }
    }
}