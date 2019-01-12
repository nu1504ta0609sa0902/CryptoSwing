/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptonotifications2.notifications.utils;


import com.mycompany.cryptonotifications2.pojo.AlertData;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tayyibah on 09/12/2018.
 */
public class NotificationUtil {
    
    /**
     * DELETE IF NOT USED
     * @param currentPrice
     * @param pastPrice
     * @param upPercent
     * @param downPercent
     * @return 
     */
    public static boolean isNotificationValueReached(Double currentPrice, Double pastPrice, double upPercent, double downPercent) {

        //Has it increase by more than X percent
        double up = pastPrice * ((upPercent/100) + 1);
        double dp = pastPrice * (1 - (downPercent/100));
        double percentDiffCurrentAndPastPrice = (currentPrice - pastPrice)/pastPrice * 100;
        System.out.println(String.format("Percentage difference between current price %s and previous price %s is : %s", currentPrice, pastPrice, percentDiffCurrentAndPastPrice));
        if(currentPrice > up){
            System.out.println(String.format("Current price %s is atleast %s percent MORE than previous price %s", currentPrice, upPercent, pastPrice));
            return true;
        }

        if(currentPrice < dp){
            System.out.println(String.format("Current price %s is atleast %s percent LESS than previous price %s", currentPrice, downPercent, pastPrice));
            return true;
        }

        return false;
    }
    
    /**
     * DELETE IF NOT USED
     * @param currentPrice
     * @param data
     * @return 
     */
    public static boolean isNotificationValueReached(Double currentPrice, AlertData data) {
        final Double price = data.price;
        final Double upPercent = data.getUpPercent();
        
        //Has it increase by more than X percent
        double up = price * ((upPercent/100) + 1);
        final Double downPrice = data.getDownPrice();
        double dp = price * (1 - (downPrice/100));
        double percentDiffCurrentAndPastPrice = (currentPrice - price)/price * 100;
        System.out.println(String.format("Percentage difference between current price %s and previous price %s is : %s", currentPrice, price, percentDiffCurrentAndPastPrice));
        if(currentPrice > up){
            System.out.println(String.format("Current price %s is atleast %s percent MORE than previous price %s", currentPrice, upPercent, price));
            return true;
        }

        if(currentPrice < dp){
            System.out.println(String.format("Current price %s is atleast %s percent LESS than previous price %s", currentPrice, downPrice, price));
            return true;
        }

        return false;
    }

}
