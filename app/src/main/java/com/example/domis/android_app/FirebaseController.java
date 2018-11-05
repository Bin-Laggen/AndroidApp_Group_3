package com.example.domis.android_app;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FirebaseController {

    private static final FirebaseController ourInstance = new FirebaseController();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private HashMap<String, String> userList;
    private User tmpUser;
    private FutureTask<Void> ft;
    private ExecutorService es;

    public static FirebaseController getInstance() {
        return ourInstance;
    }

    private FirebaseController() {
        database = FirebaseDatabase.getInstance();
        ft = new FutureTask<Void>(new Runnable() {
            @Override
            public void run() {

            }
        }, null);
        es = Executors.newFixedThreadPool(1);
        getUserList();
    }

    public boolean checkUserLogin(User user) {

        if(getUser(user) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
        /**
        System.out.println("USERNAME: " + user.getUsername());
        System.out.println("PASSWORD: " + user.getPassword());

        if(userList.containsKey(user.getUsername()))
        {
            System.out.println("Key found");
            System.out.println("Here is its value: " + userList.get(user.getUsername()));
        }

        if (userList.containsKey(user.getUsername()) && user.getPassword().equals(userList.get(user.getUsername())))
        {
            System.out.println("Success");
            return true;
        }
        else
        {
            System.out.println("Kind of Success");
            return false;
        }
         */
    }

    public boolean registerUser(User user)
    {
        getUserList();
        System.out.println("Number of users: " + userList.size());
        int indexOfAt = user.getUsername().indexOf("@");
        String userID = user.getUsername().substring(0, indexOfAt);
        System.out.println("UserID: " + userID);
        userID = userID.replace(".", "");
        System.out.println("UserID: " + userID);
        myRef = database.getReference().child("USERS");
        if(!userList.containsKey(user.getUsername()))
        {
            myRef.child(userID).setValue(user);
            return true;
        }
        else
        {
            return false;
        }
    }

    private User getUser(User user)
    {
        tmpUser = null;
        myRef = database.getReference().child("USERS").child(user.getUsername());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tmpUser = dataSnapshot.getValue(User.class);
                es.execute(ft);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try
        {
            ft.get();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        return tmpUser;
    }

    private void getUserList() {
        userList = new HashMap<>();

        myRef = database.getReference().child("USERS");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user1 = postSnapshot.getValue(User.class);
                    System.out.println("FireUser: " + user1.getUsername());
                    System.out.println("FirePass: " + user1.getPassword());
                    userList.put(user1.getUsername(), user1.getPassword());
                    es.execute(ft);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try
        {
            ft.get();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}

