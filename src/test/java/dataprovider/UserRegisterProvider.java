package dataprovider;

import datamodel.UserRegisterDataModel;
import lib.DataReaders;

import java.util.List;
import java.util.stream.Stream;

public class UserRegisterProvider {
    public static String userRegisterDataFile = "UserRegisterNegativeData.json";
    /*it works also
    public static List<UserRegisterDataModel> UserRegisterDataList = DataReaders.readUserRegisterDataFromJson(userRegisterDataFile);*/
    public static List<UserRegisterDataModel> UserRegisterDataList = DataReaders
            .readDataFromJsonAlt(userRegisterDataFile, UserRegisterDataModel[].class);
    public static Stream<UserRegisterDataModel> provideTestData() {
        return UserRegisterDataList.stream();
    }
}
