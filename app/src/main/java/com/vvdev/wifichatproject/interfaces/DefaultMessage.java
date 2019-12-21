package com.vvdev.wifichatproject.interfaces;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class DefaultMessage implements IMessage {

    String ID, TEXT;
    IUser USER;
    Date CREATEAT;

    public DefaultMessage(String Id, String Text, IUser User, Date CreateAt){
        ID = Id;
        TEXT = Text;
        USER=User;
        CREATEAT=CreateAt;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getText() {
        return TEXT;
    }

    @Override
    public IUser getUser() {
        return USER;
    }

    @Override
    public Date getCreatedAt() {
        return CREATEAT;
    }
}
