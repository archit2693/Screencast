package com.example.gemswin.screancasttest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Ravi on 01/06/15.
 */
@SuppressLint("CommitPrefEdits")
public class PrefManager {
     // Shared Preferences
    SharedPreferences pref;
    SharedPreferences pref1;

    // Editor for Shared preferences
    Editor editor;
    Editor editor1;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

	

    // Shared pref file name
    private static final String PREF_NAME = "AndroidHive";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    
    public static final String Event = "event";

    public static final String LOGIN = "login";


    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref1 = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        
        editor = pref.edit();
        editor1 = pref1.edit();
   
    }

    /**
     * Create login session
     */

    //Setters

    public void setColor(int product) {

        editor1.putInt("color", product);

        editor1.commit();
    }

     public void setClass(String product) {

         editor1.putString("class", product);

         editor1.commit();
     }
     public void setImage(int product) {

         editor1.putInt("image", product);

         editor1.commit();
     }
     public void setName(String product) {


         editor1.putString("name", product);

         editor1.commit();
     }
     public void setFlag(int product) {


         editor1.putInt("flag", product);

         editor1.commit();
     }

    public void setNotice(int product) {


        editor1.putInt("notice", product);

        editor1.commit();
    }

     public void setExtension(String product) {

         editor1.putString("extension", product);

         editor1.commit();
     }


    public void setPort(String port) {


        editor1.putString("port", port);

        editor1.commit();
    }

    public void setHosts(String hosts) {


        editor1.putString("hosts", hosts);

        editor1.commit();
    }

    public void setTunnel(int hosts) {


        editor1.putInt("tunnel", hosts);

        editor1.commit();
    }

    public void setPortClient(String port)
    {
        editor1.putString("port", port);
        editor1.commit();
    }

    public void setIPClient(String ipClient)
    {
        editor1.putString("ipAddress", ipClient);
        editor1.commit();
    }


    public String getPortClient() {
        return pref.getString("port", null);
    }


    public String getIPClient() {
        return pref.getString("ipAddress", null);
    }



    //Getters

    public int getNotice() {
        return pref.getInt("notice", 0);
    }

    public String getName() {
        return pref.getString("name", null);
    }

    public String getExtension() {
        return pref.getString("extension", null);
    }

    public int getImage() {
        return pref.getInt("image", 0);
    }

    public int getFlag() {return pref.getInt("flag", 0);}

    public int getTunnel() {return pref.getInt("tunnel", 0);}

    public String getPort() {
        return pref.getString("port", null);
    }

    public String getCenter() {
        return pref.getString("center", null);
    }

    public int getColor() {
        return pref.getInt("color", 0);
    }

    public void logout() {
        editor1.clear();
        editor1.commit();
    }

}