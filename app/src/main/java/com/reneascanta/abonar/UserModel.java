package com.reneascanta.abonar;

/**
 * Created by Rene on 27/10/2017.
 */


public class UserModel {
    boolean isSelected;
    String userName;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserModel(boolean isSelected, String userName) {

        this.isSelected = isSelected;
        this.userName = userName;
    }
}
