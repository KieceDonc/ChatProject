package com.vvdev.wifichatproject.interfaces;

import com.stfalcon.chatkit.commons.models.IUser;

public class DefaultUser implements IUser {

    String ID, NAME, AVATAR;

    public DefaultUser(String Id, String Name, String Avatar){
        ID= Id;
        NAME=Name;
        AVATAR=Avatar;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getAvatar() {
        return AVATAR;
    }
}
