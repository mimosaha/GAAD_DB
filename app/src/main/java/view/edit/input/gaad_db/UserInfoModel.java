package view.edit.input.gaad_db;

/**
 * Created by hp on 5/29/2018.
 */

public class UserInfoModel {

    public String getUserName() {
        return userName;
    }

    public UserInfoModel setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public UserInfoModel setUserAddress(String userAddress) {
        this.userAddress = userAddress;
        return this;
    }

    public String getUserDesignation() {
        return userDesignation;
    }

    public UserInfoModel setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
        return this;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public UserInfoModel setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
        return this;
    }

    private String userName, userAddress, userDesignation, userContactNumber;

}
