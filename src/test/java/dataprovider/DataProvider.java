package dataprovider;

import datamodel.UserRegisterDataModel;
import lib.DataReaders;

import java.util.List;
import java.util.stream.Stream;

public class DataProvider {
    /*it works also
    DataReaders.readUserRegisterDataFromJson(userRegisterDataFile);*/
     

    public static Stream<UserRegisterDataModel> provideUserRegisterPositiveData() {
        return DataReaders.readDataFromJsonAlt("UserRegisterPositiveData.json", UserRegisterDataModel[].class).stream();
    }

    public static Stream<UserRegisterDataModel> provideUserLoginPositiveData() {
        return DataReaders.readDataFromJsonAlt("UserLoginPositiveData.json", UserRegisterDataModel[].class).stream();
    }

    public static <T> Stream<T> provideUserDataList(String jsonFile, Class<T[]> dataClass) {
        return DataReaders.readDataFromJsonAlt(jsonFile, dataClass).stream();
    }

    public static Stream<UserRegisterDataModel> provideTakeUserInfoNegativeData() {
        return DataReaders
                .readDataFromJsonAlt("UserInfoRequestNegativeData.json", UserRegisterDataModel[].class)
                .stream();
    }

    public static Stream<UserRegisterDataModel> provideChangeUserInfoNegativeData() {
        return DataReaders
                .readDataFromJsonAlt("UserChangeRequestNegativeData.json", UserRegisterDataModel[].class)
                .stream();
    }

    public static Stream<UserRegisterDataModel> provideDeleteUserData() {
        return DataReaders
                .readDataFromJsonAlt("UserDeleteRequestData.json", UserRegisterDataModel[].class)
                .stream();
    }
}