package com.example.eklerov2.testprojectfoursquare;

import android.app.Application;
import android.content.Intent;

import com.example.eklerov2.testprojectfoursquare.activitys.AutorisationActivity;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;

/**
 * Created by Evgen on 16.03.16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .build();

    }
}
