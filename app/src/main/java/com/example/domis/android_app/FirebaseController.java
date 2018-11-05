package com.example.domis.android_app;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FirebaseController {

    private static final FirebaseController ourInstance = new FirebaseController();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private HashMap<String, String> userList;
    private User tmpUser;
    private FutureTask<Void> ft;
    private ExecutorService es;
    private boolean success;

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
        //getUserList();
    }

    public boolean checkUserLogin(User user) {
        //getUserList();
        if(user != null)
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
        //getUserList();
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

    public boolean getUser(final User user)
    {
        success = false;
        //int indexOfAt = user.getUsername().indexOf("@");
        //String userID = user.getUsername().substring(0, indexOfAt);
        //userID = userID.replace(".", "");
        String userID = user.getUsername();
        Log.d("Search for: ", userID);
        myRef = database.getReference().child("USERS").child(user.getUsername());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PersonalData.USER = dataSnapshot.getValue(User.class);
                Log.e("USER.toString(): ", "===============================================\n" + tmpUser.toString());

                PersonalData.validate(user.getPassword());
                //es.execute(ft);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", "Failed To Get the user");
            }
        });

        /**
        try
        {
            ft.get(10000, TimeUnit.MILLISECONDS);
        }
        catch (Throwable e)
        {
            Log.e("=================", "=======================");
            Log.e("123", user.getUsername());
            e.printStackTrace();
        }
         */

        return success;
    }

    /**
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
            ft.get(1000, TimeUnit.MILLISECONDS);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

    }
     */
}

