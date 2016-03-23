package com.example.eklerov2.testprojectfoursquare.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Evgen on 23.03.16.
 */
public class Utils {


    public static String urlBilder(String prefix,String sufix,String size){
        String currentPrefix="";
        String currentSufix="";
        String currentUrl = "";
        currentPrefix = prefix.replace("\\", "");
        currentSufix = sufix.replace("\\", "");

        currentUrl = currentPrefix + size +currentSufix;
        return currentUrl;
    }

    public static String getDateToString(int numberOfDate) {
        String date;
        if (numberOfDate < 10) {
            date = "0" + numberOfDate;
        } else {
            date = "" + numberOfDate;
        }
        return date;
    }

    public static String getCurrentDate(Calendar calendar) {
       String year = calendar.get(GregorianCalendar.YEAR) + "";
        String month = Utils.getDateToString(calendar.get(GregorianCalendar.MONTH));
        String day = Utils.getDateToString(GregorianCalendar.DAY_OF_MONTH);
        String currentDate = year + month + day;
        return currentDate;
    }

    public static double roundLocation(double latlon){

        double roundNumber = new BigDecimal(latlon).setScale(4, RoundingMode.HALF_UP).doubleValue();
        return roundNumber;
    }
}
