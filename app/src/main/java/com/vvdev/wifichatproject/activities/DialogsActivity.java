package com.vvdev.wifichatproject.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.vvdev.wifichatproject.R;
import com.vvdev.wifichatproject.interfaces.DefaultDialog;
import com.vvdev.wifichatproject.interfaces.DefaultMessage;
import com.vvdev.wifichatproject.interfaces.DefaultUser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DialogsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogslistmain);

        DialogsList dialogsList = findViewById(R.id.dialogsList);

        Log.e("test",String.valueOf(dialogsList));

        @SuppressLint("ResourceType") DialogsListAdapter dialogsListAdapter = new DialogsListAdapter<>( new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.get().load(Integer.parseInt(url)).into(imageView);
            }
        });
        dialogsList.setAdapter(dialogsListAdapter);
        List<IUser> TestListUser = new ArrayList<>();
        IUser UserTest = new DefaultUser("0","test",String.valueOf(R.drawable.wifi_icon_good));
        IMessage MessageTest = new DefaultMessage("0","ceci est un test",UserTest,new Date());
        TestListUser.add(UserTest);
        IDialog Test = new DefaultDialog("0",String.valueOf(R.drawable.wifi_icon_good),"tst",TestListUser,MessageTest,1);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("/some/file/path/filename.ser", true);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(MessageTest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialogsListAdapter.addItem(Test);
    }
}
