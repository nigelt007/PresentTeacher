package com.avatar.presentteacher;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by nigthoma on 9/27/2014.
 */
public class Utility {

    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static int[] stringToIntDateFormatter(String dateString){
        int[] date = new int[3];
        String[] inputDate = dateString.split("-");
        date[0] = Integer.parseInt(inputDate[0]);
        date[1]= monthStringToInt(inputDate[1]);
        date[2] =Integer.parseInt(inputDate[2]);
        return date;
    }

    private static int monthStringToInt (String month){

        if("JANUARY".contains(month)){
            return 1;
        }
        if("FEBRUARY".contains(month)){
            return 2;
        }
        if("MARCH".contains(month)){
            return 3;
        }
        if("APRIL".contains(month)){
            return 4;
        }
        if("MAY".contains(month)){
            return 5;
        }
        if("JUNE".contains(month)){
            return 6;
        }
        if("JULY".contains(month)){
            return 7;
        }
        if("AUGUST".contains(month)){
            return 8;
        }
        if("SEPTEMBER".contains(month)){
            return 9;
        }
        if("OCTOBER".contains(month)){
            return 10;
        }
        if("NOVEMBER".contains(month)){
            return 11;
        }
        if("DECEMBER".contains(month)){
            return 12;
        }

        return 0;
    }
}
