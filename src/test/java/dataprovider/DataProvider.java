package dataprovider;

import datamodel.UserRegisterDataModel;
import lib.DataReaders;

import java.util.List;
import java.util.stream.Stream;

public class DataProvider {
    /*it works also
    DataReaders.readUserRegisterDataFromJson(userRegisterDataFile);*/
    public static Stream<UserRegisterDataModel> provideUserRegisterTestData() {
        return DataReaders
                .readDataFromJsonAlt("UserRegisterNegativeData.json", UserRegisterDataModel[].class)
                .stream();
    }
}
