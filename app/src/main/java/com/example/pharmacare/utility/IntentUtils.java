package com.example.pharmacare.utility;

import android.content.Context;
import android.content.Intent;

import com.example.pharmacare.MainActivity;
import com.example.pharmacare.NotificationActivity;
import com.example.pharmacare.SearchItemActivity;

public class IntentUtils {

    public static void goToSearchActivity(Context context){
        Intent intent=new Intent(context, SearchItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }
    public static void goToHome(Context context){
        Intent intent=new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }
    public static void goToNotification(Context context){
        Intent intent=new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }
}