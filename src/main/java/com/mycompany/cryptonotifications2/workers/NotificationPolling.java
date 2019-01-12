/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptonotifications2.workers;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;
import com.mycompany.cryptonotifications2.notifications.PriceListOfTickers;
import com.mycompany.cryptonotifications2.notifications.PricePerTicker;
import com.mycompany.cryptonotifications2.notifications.utils.FileUtils;
import com.mycompany.cryptonotifications2.notifications.utils.NotificationUtil;
import com.mycompany.cryptonotifications2.notifications.utils.email.EmailUtils;
import com.mycompany.cryptonotifications2.pojo.AlertData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author tayyibah
 */
public class NotificationPolling extends SwingWorker<List<String>, Void> {

    boolean invalidKey = false;
    boolean useOnlyTop100Coins = true;
    List<String> coinsNotInTop100 = new ArrayList<>();
    private Map<String, Double> mapOfCoinToPrice = new HashMap<>();
    private final Map<String, AlertData> mapOfCoins;
    String key;
    private final JTextArea ta;

    public NotificationPolling(JCheckBox cb, JTextArea taNotifications, Map<String, AlertData> mapOfData, String key) {
        mapOfCoinToPrice = new HashMap<>();
        this.mapOfCoins = mapOfData;
        this.key = key;
        this.ta = taNotifications;
    }

    @Override
    protected List<String> doInBackground() throws Exception {

        /**
         * Coins in the top 100
         */
        String json = PriceListOfTickers.getDataForAllTickers(key);
        if (json.contains("This API Key is invalid")) {
            invalidKey = true;
            throw new Exception("CMC API Key is invalid");
        }
        invalidKey = false;
        Set<String> coins = mapOfCoins.keySet();
        coins.stream().forEach((coin) -> {
            Double currentPrice = PricePerTicker.getCurrentPriceOfTickerDynamic(json, coin, "USD");
            if (currentPrice == null || currentPrice == 0) {
                if (!coinsNotInTop100.contains(coin)) {
                    coinsNotInTop100.add(coin);
                }
            } else {
                mapOfCoinToPrice.put(coin, currentPrice);
            }
        });

        /**
         * Separate calls for coins outside of top 100
         */
        if (!useOnlyTop100Coins) {
            coinsNotInTop100.stream().forEach(c -> {
                try {
                    String dJson = PricePerTicker.getDataForTicker(key, c);
                    Double currentPrice = PricePerTicker.getCurrentPriceOfTicker(dJson, c, "USD");
                    if (currentPrice != null && currentPrice > 0) {
                        mapOfCoinToPrice.put(c, currentPrice);
                    }
                } catch (MalformedURLException ex) {
                    Logger.getLogger(NotificationPolling.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        return coinsNotInTop100;
    }

    @Override
    public void done() {
        if (invalidKey) {
            ta.append("\nCheck CMC API Key Is Valid");
        } else {
            ta.append("\nCoins not in top 100 : " + coinsNotInTop100);

            Set<String> keySet = mapOfCoinToPrice.keySet();
            keySet.stream().forEach(k -> {
                AlertData alert = mapOfCoins.get(k);
                alert.message.setLength(0);
                Double currentPrice = mapOfCoinToPrice.get(k);
                alert.showPercentage = true;
                alert.isNotificationValueReached(currentPrice);
            });

            final StringBuilder message = new StringBuilder();
            mapOfCoinToPrice.forEach((k, v) -> {
                message.append("\n");
                message.append(k);
                message.append("\n");
                message.append(v);
                message.append("\n");
            });
            generateNotifications(mapOfCoins, mapOfCoinToPrice, message.toString());
        }
    }

    private void generateNotifications(Map<String, AlertData> mapOfCoins1, Map<String, Double> mapOfCoinToPrice1, String message) {

        List<String> mes = new ArrayList<>();
        List<String> coins = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        mapOfCoins1.forEach((coin, data) -> {
            if (data.generateNotification) {
                coins.add(data.coin);
                mes.add("Genereated");
                sb.append("\n----------------------------\n\nNotification settings : ");
                sb.append(data);
                sb.append("\nCurrent Price : ");
                sb.append(data.toXDecimalPlace(mapOfCoinToPrice1.get(coin), 6));
                sb.append("\n");
                sb.append(data.message);
                sb.append("\n----------------------------\n");

                ta.append(sb.toString());
                show(data, mapOfCoinToPrice1.get(coin));
            }
        });

        //Send if only we have notifications : aggregated message
        if (!mes.isEmpty()) {
            //sendEmail(coins, sb.toString());
        }

        //Eash time we poll for notification log it
        String time = Calendar.getInstance().getTime().toString();
        String msg = String.format("\n\nNO NOTIFICATIONS GENERATED: %s\n", time);
        if (mes.size() > 0) {
            msg = String.format("\n\nNOTIFICATIONS GENERATED: %s\n", time);
            String txt = "\n----------------------------\nDate: " + time + sb.toString() + "\n";
            FileUtils.writeToFile(true, "notifications.txt", "append", txt);
        }
        ta.append(msg);
    }

    private void sendEmail(List<String> coins, String message) {

        try {
            //Send email if not sent any today
            String content = FileUtils.readFile2("emails.txt");
            if (content == null) {
                content = "Clear";
            }

            Calendar cal = Calendar.getInstance();
            int dom = cal.get(Calendar.DAY_OF_MONTH);
            int mon = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String sentToday = dom + "_" + mon + "_" + year;

            //Only send a single one per day
            if (!content.contains(sentToday)) {

                //No notifications sent today
                EmailUtils.sendFromGMail("CRYPTO NOTIFICATIONS FOR : " + coins, message);

                //Write date to emails.txt
                FileUtils.writeToFile(false, "emails.txt", "append", sentToday);
            } else {
                System.out.println("ALREADY SENT EMAIL NOTIFICATIONS");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate desktop notifications
     *
     * @param data
     */
    public void show(AlertData data, Double currentPrice) {

        try {
            //Obtain only one instance of the SystemTray object
            SystemTray tray = SystemTray.getSystemTray();

            //If the icon is a file
            BufferedImage image = ImageIO.read(ClassLoader.getSystemResourceAsStream("btc.png"));
            //Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            //Alternative (if the icon is on the classpath):
            //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

            TrayIcon trayIcon = new TrayIcon(image, "Price : " + data);
            //Let the system resize the image if needed
            trayIcon.setImageAutoSize(true);
            //Set tooltip text for the tray icon
            String time = Calendar.getInstance().getTime().toString();
            String val = String.format("\nNotification for : %s \nAt price $: %s. \nPercent: %s \nTime: %s", data.coin, data.toXDecimalPlace(currentPrice, 6), data.calculatePercentChanged(data.price, currentPrice, 2), time.split(" ")[3]);
            trayIcon.setToolTip("Forget CMC: " + val);
            tray.add(trayIcon);

            trayIcon.displayMessage(data.coin + "\n" + data.message.toString(), "Notification : " + data.coin, MessageType.INFO);

        } catch (Exception ex) {
            Logger.getLogger(NotificationPolling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void useAllCoins(String allCoin) {
        System.out.println("Use allCoin : " + allCoin);
        if (allCoin != null && allCoin.equals("true")) {
            useOnlyTop100Coins = false;
        } else {
            System.out.println("ONLY USE TOP 100 coins");
        }
    }

}
