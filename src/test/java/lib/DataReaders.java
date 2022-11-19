package lib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import datamodel.UserAgentDataModel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class DataReaders {

    public static  List<UserAgentDataModel> readDataFromJson(String jsonDataFile) {
        Type dataType = new TypeToken<List<UserAgentDataModel>>() {
        }.getType();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream resourceAsStream = cl.getResourceAsStream(jsonDataFile);
            //InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(jsonArrayFile);

            reader = new JsonReader(new InputStreamReader(resourceAsStream));
        } catch (NullPointerException ioe) {
            System.err.println("Unable to read json file:" + jsonDataFile);
        }
        return gson.fromJson(reader, dataType);
    }
}
