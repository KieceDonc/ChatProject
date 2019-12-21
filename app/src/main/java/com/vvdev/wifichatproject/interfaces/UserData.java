package com.vvdev.wifichatproject.interfaces;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class UserData {

    private String Path="";
    private FileOutputStream fos;
    private ObjectOutputStream db;
    {
        try {
            fos = new FileOutputStream(Path, true);
            db = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void storeIDialog(IDialog){

    }

    public void createConversation(IUser User, IUser OtherUser){
        String MessageConversation ="Conversation_"+User+OtherUser;
        List<IMessage>
        db.write();
        db.
    }

    public void getConversation(IUser User, IUser OtherUser){
        String MessageConversation ="Conversation_"+User+OtherUser;

    }

}
