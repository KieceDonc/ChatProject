package com.vvdev.wifichatproject.interfaces;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.List;

public class DefaultDialog implements IDialog {

    String ID, PHOTO,NAME;
    List<IUser> USERS;
    IMessage LASTMESSAGE;
    int UNREADCOUNT;

    public DefaultDialog(String id, String Photo, String Name,List<IUser> users, IMessage LastMessage,int unreadCount ){
        ID = id;
        PHOTO = Photo;
        NAME=Name;
        USERS=users;
        LASTMESSAGE=LastMessage;
        UNREADCOUNT=unreadCount;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDialogPhoto() {
        return PHOTO;
    }

    @Override
    public String getDialogName() {
        return NAME;
    }

    @Override
    public List<IUser> getUsers() {
        return USERS;
    }

    @Override
    public IMessage getLastMessage() {
        return LASTMESSAGE;
    }

    @Override
    public void setLastMessage(IMessage message) {
        LASTMESSAGE=message;
    }

    @Override
    public int getUnreadCount() {
        return UNREADCOUNT;
    }
}
