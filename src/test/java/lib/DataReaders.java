package lib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import datamodel.UserAgentDataModel;
import datamodel.UserRegisterDataModel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class DataReaders {

    public static List<UserAgentDataModel> readAgentDataFromJson(String jsonDataFile) {
        Type dataType = new TypeToken<List<UserAgentDataModel>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {

            /*InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(jsonArrayFile);
            getClass() - We can not use it here since readDataFromJson - static */

            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream resourceAsStream = cl.getResourceAsStream(jsonDataFile);

            reader = new JsonReader(new InputStreamReader(resourceAsStream));
        } catch (NullPointerException ioe) {
            System.err.println("Unable to read json file:" + jsonDataFile);
        }
        return gson.fromJson(reader, dataType);
    }

    public static List<UserRegisterDataModel> readUserRegisterDataFromJson(String jsonDataFile) {
        Type dataType = new TypeToken<List<UserRegisterDataModel>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream resourceAsStream = cl.getResourceAsStream(jsonDataFile);

            reader = new JsonReader(new InputStreamReader(resourceAsStream));
        } catch (NullPointerException ioe) {
            System.err.println("Unable to read json file:" + jsonDataFile);
        }
        return gson.fromJson(reader, dataType);
    }

    public static <T> List<T> readDataFromJsonAlt(String jsonDataFile, Class<T[]> clazz) {
        Type dataType = new TypeToken<List<T>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream resourceAsStream = cl.getResourceAsStream(jsonDataFile);

            reader = new JsonReader(new InputStreamReader(resourceAsStream));
        } catch (NullPointerException ioe) {
            System.err.println("Unable to read json file:" + jsonDataFile);
        }
        return Arrays.asList(gson.fromJson(reader, clazz));
    }
}
