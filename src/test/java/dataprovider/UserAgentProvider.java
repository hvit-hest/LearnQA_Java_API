package dataprovider;

import datamodel.UserAgentDataModel;
import lib.DataReaders;

import java.util.List;
import java.util.stream.Stream;

public class UserAgentProvider {

    public static List<UserAgentDataModel> userAgentDataList = DataReaders.<UserAgentDataModel>readAgentDataFromJson("UserAgentData.json");
    public static Stream<UserAgentDataModel> provideTestData() {
        return userAgentDataList.stream();
    }
}