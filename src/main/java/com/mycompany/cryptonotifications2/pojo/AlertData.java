/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptonotifications2.pojo;

/**
 *
 * @author tayyibah
 */
public class AlertData {

    public StringBuilder message = new StringBuilder();
    public boolean generateNotification = false;

    public String SEPARATOR = "_";
    public String coin;
    public String percentUp;
    public String percentDown;
    public Double price;
    public boolean showPercentage;

    public AlertData(String coin, String percentUp, String percentDown, Double price) {
        this.coin = coin;
        this.percentUp = percentUp;
        this.percentDown = percentDown;
        this.price = price;
    }

    public AlertData(String data) {
        if (data != null) {
            try {
                String[] split = data.split(SEPARATOR);
                coin = split[0];
                percentUp = split[1];
                percentDown = split[2];
                if (split.length == 4) {
                    price = Double.parseDouble(split[3]);
                }
            } catch (Exception e) {
                System.out.println("Error data : " + data);
            }
        }
    }

    @Override
    public String toString() {
        return this.coin + SEPARATOR + percentUp + SEPARATOR + percentDown + SEPARATOR
                + String.format("%.11f", price);
    }
    
    public String fullSettings() {
        return this.coin + SEPARATOR + percentUp + SEPARATOR + percentDown 
                + SEPARATOR + truncate(toXDecimalPlace(price, 7),7)
                + SEPARATOR + percentAmount(price, Double.parseDouble(percentUp), 7) 
                + SEPARATOR + percentAmount(price, -1*Double.parseDouble(percentDown), 7);
    }

    public Double getUpPercent() {
        return Double.parseDouble(this.percentUp);
    }

    public Double getDownPrice() {
        return Double.parseDouble(this.percentDown);
    }

    public boolean isNotificationValueReached(Double currentPrice) {
        currentPrice = to2D(currentPrice);
        final Double price = to2D(this.price);
        final Double upPercent = this.getUpPercent();
        final Double downPrice = this.getDownPrice();

        //Has it increase by more than X percent
        double up = price * to2D((upPercent / 100) + 1);
        double dp = price * to2D(1 - (downPrice / 100));
        double percentDiffCurrentAndPastPrice = to2D((currentPrice - price) / price * 100);
        //message.append(String.format("\nPercentage difference between current price %s and previous price %s is : \n\n%s Percent", currentPrice, price, percentDiffCurrentAndPastPrice));
        message.append(String.format("\nPercentage: %s", toXDecimalPlace(percentDiffCurrentAndPastPrice, 2)));
        if (currentPrice > up) {
            message.append(String.format("\n\nCurrent price %s is atleast %s percent MORE than previous price %s", toXDecimalPlace(currentPrice, 6), upPercent, toXDecimalPlace(price, 6)));
            generateNotification = true;
        }

        if (currentPrice < dp) {
            message.append(String.format("\n\nCurrent price %s is atleast %s percent LESS than previous price %s", toXDecimalPlace(currentPrice, 6), downPrice, toXDecimalPlace(price, 6)));
            generateNotification = true;
        }

        if (generateNotification && showPercentage) {
            //message.append(String.format("\n\nPercentage difference : %s", percentDiffCurrentAndPastPrice));
        }

        return generateNotification;
    }

    private Double to2D(Double price) {
//        int decimals = 1000000;
//        return ((int)(price*decimals))/(decimals * 1.00);
        return price;
    }

    /**
     * Scientific format String.format("%.11f", price)
     *
     * @param upPercent
     * @param percent
     * @return
     */
    public String calculatePrice(boolean upPercent, int percent) {
        if (upPercent) {
            Double p = price + ((percent / 100.0) * price);
            //return String.format("%s percent UP from price %s is : %s.\nPrice difference : %s", percent, price, String.format("%.11f", p), String.format("%.11f", p-price));
            return String.format("+ %s percent : %s\t", percent, String.format("%.11f", p));
        } else if (percent <= 100) {
            Double p = price - ((percent / 100.0) * price);
            //return String.format("%s percent DOWN from price %s is : %s.\nPrice difference : %s", percent, price, String.format("%.11f", p), String.format("%.11f", price-p));
            return String.format("- %s percent : %s\t", percent, String.format("%.11f", p));
        } else {
            return "COIN HAS NO VALUE";
        }
    }

    public String toXDecimalPlace(double price, int decimalPlaces) {
        return String.format("%." + decimalPlaces + "f", price);
    }

    public String calculatePercentChanged(double prevPrice, double currentPrice, int decimalPlace) {
        double percent = ((currentPrice - prevPrice) / prevPrice) * 100.0;
        return toXDecimalPlace(percent, decimalPlace) + "%";
    }

    public String calculateRatio(double anotherPrice, int decimalPlace) {
        double percent = ( anotherPrice / price);
        String ratio = toXDecimalPlace(percent, decimalPlace);
        double dr = Double.parseDouble(ratio);
        if(dr < 1){
            double nr = 1.0 / dr;
            ratio = toXDecimalPlace(nr, 2) + ":1";
        }else{
            ratio = "1:" + toXDecimalPlace(dr, 2);
        }
        return ratio;
    }

    private String percentAmount(Double price, double percent, int decimalPlace) {
        double amount = price * ((100 + percent)/100.0);
        return truncate(toXDecimalPlace(amount, decimalPlace), decimalPlace);
    }

    private String truncate(String price, int decimalPlace) {
        String val = price;
        if(price.length() > decimalPlace){
            val = val.substring(0, decimalPlace);
        }
        return val;
    }
}
