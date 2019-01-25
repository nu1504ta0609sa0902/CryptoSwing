/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptonotifications2;

import com.mycompany.cryptonotifications2.notifications.PricePerTicker;
import com.mycompany.cryptonotifications2.notifications.utils.FileUtils;
import com.mycompany.cryptonotifications2.pojo.AlertData;
import com.mycompany.cryptonotifications2.workers.NotificationPolling;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.TemporalField;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author tayyibah
 */
public class MainNotificationFrame extends javax.swing.JFrame {

    public String apiKey = "27d1830b-1b35-46a4-810f-5d035a8cfb8e";
    Map<String, AlertData> mapOfData = new HashMap<>();

    /**
     * Creates new form Notifications
     */
    public MainNotificationFrame(String pollingMin) throws BackingStoreException {
        initComponents();

        DefaultListModel<String> model = new DefaultListModel<String>();
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        String key = prefs.get("key", null);
        if (key == null) {
            String keyValue = FileUtils.readFile2("key.txt");
            if (StringUtils.isEmpty(keyValue)) {
                key = "Enter CMC API key";
            } else {
                key = keyValue;
                //Key found
                apiKey = key;
                prefs.put("key", key);
            }
        } else {
            apiKey = key;
        }
        txtCMCKey.setText(key);

        String[] keys = prefs.keys();
        System.out.println("Number of coins : " + keys.length);
        if (keys.length > 0) {
            List<String> list = Arrays.asList(keys);
            Collections.sort(list);
            //Collections.sort(list);

            for (String item : list) {
                if (!item.equals("key")) {
                    model.addElement(item);
                    mapOfData.put(item, new AlertData(prefs.get(item, null)));
                }
            }
            listOfCoins.setModel(model);
            listOfCoins.setSelectedIndex(0);
            listOfCoinRatio.setModel(model);
            txtCoinName.setText(listOfCoins.getSelectedValue());
        } else {
            try {
                //Something went wrong : load from 
                List<String> lines = Files.readAllLines(Paths.get("coins.txt"));
                lines.stream().forEach(s -> {
                    String item = s.split("_")[0];
                    model.addElement(item);
                    mapOfData.put(item, new AlertData(s));
                });

                listOfCoins.setModel(model);
                listOfCoins.setSelectedIndex(0);
                txtCoinName.setText(listOfCoins.getSelectedValue());
            } catch (IOException ex) {
                Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        mapOfData.forEach((k, v) -> {
            System.out.println(v.toString());
        });

        //Poll in the background every 60 seconds
        int pollEveryXMin = 5;
        if (pollingMin != null) {
            pollEveryXMin = Integer.parseInt(pollingMin);
        }

        String allCoin = System.getProperty("all.coin");
        taNotifications.append("\nSearch All Coin : " + allCoin);
        taNotifications.append("\nPolling for notifications every : " + pollEveryXMin + " minutes\n");
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            NotificationPolling np1 = new NotificationPolling(null, taNotifications, mapOfData, apiKey);
            np1.useAllCoins(allCoin);
            np1.execute();
        }, 1, 60 * pollEveryXMin, TimeUnit.SECONDS);
        
        //Set default values
        String selectedCoin = listOfCoins.getSelectedValue();
        AlertData alertData = new AlertData(prefs.get(selectedCoin, null));
        txtPercentUp.setText(alertData.percentUp);
        txtPercentDown.setText(alertData.percentDown);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taNotifications = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        txtCoinName = new javax.swing.JTextField();
        btnAddOrUpdateCoinSettings = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtPercentUp = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPercentDown = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        listOfCoins = new javax.swing.JList<>();
        btnRemoveCoin = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtCMCKey = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        listOfCoinRatio = new javax.swing.JList<>();
        btnShowCoinRatio = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnShowAllNotificationsData = new javax.swing.JButton();
        btnShowSelectedNotificationData = new javax.swing.JButton();
        btnClearTextArea = new javax.swing.JButton();
        btnUpdateNotificationSettings = new javax.swing.JButton();
        btnPriceToPercentage = new javax.swing.JButton();
        btnUpdateKey = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(800, 700));
        setPreferredSize(new java.awt.Dimension(800, 700));

        taNotifications.setColumns(20);
        taNotifications.setLineWrap(true);
        taNotifications.setRows(10);
        jScrollPane2.setViewportView(taNotifications);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        btnAddOrUpdateCoinSettings.setText("Add/Update Coin Settings");
        btnAddOrUpdateCoinSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddOrUpdateCoinSettingsActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Name : ");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("% UP : ");

        txtPercentUp.setText("10");
        txtPercentUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPercentUpActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("% Down : ");

        txtPercentDown.setText("10");
        txtPercentDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPercentDownActionPerformed(evt);
            }
        });

        listOfCoins.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listOfCoinsMouseClicked(evt);
            }
        });
        listOfCoins.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                listOfCoinsKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(listOfCoins);

        btnRemoveCoin.setText("Remove Coin");
        btnRemoveCoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveCoinActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Key :");

        listOfCoinRatio.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listOfCoinRatio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listOfCoinRatioMouseClicked(evt);
            }
        });
        listOfCoinRatio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                listOfCoinRatioKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(listOfCoinRatio);

        btnShowCoinRatio.setText("Coin Ratio");
        btnShowCoinRatio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCoinRatioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCoinName)
                            .addComponent(txtPercentUp, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(txtPercentDown)
                            .addComponent(txtCMCKey)))
                    .addComponent(btnAddOrUpdateCoinSettings, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnRemoveCoin, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(btnShowCoinRatio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtCMCKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCoinName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtPercentUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtPercentDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddOrUpdateCoinSettings)
                    .addComponent(btnRemoveCoin)
                    .addComponent(btnShowCoinRatio)))
        );

        btnShowAllNotificationsData.setText("Notification Settings ALL");
        btnShowAllNotificationsData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowAllNotificationsDataActionPerformed(evt);
            }
        });

        btnShowSelectedNotificationData.setText("Show Notifications Settings SELECTED");
        btnShowSelectedNotificationData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowSelectedNotificationDataActionPerformed(evt);
            }
        });

        btnClearTextArea.setText("Clear Text");
        btnClearTextArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearTextAreaActionPerformed(evt);
            }
        });

        btnUpdateNotificationSettings.setText("Update Selected Coin Price To Latest");
        btnUpdateNotificationSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateNotificationSettingsActionPerformed(evt);
            }
        });

        btnPriceToPercentage.setText("Show Price To Percent For Coin");
        btnPriceToPercentage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPriceToPercentageActionPerformed(evt);
            }
        });

        btnUpdateKey.setText("Update Key");
        btnUpdateKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateKeyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnShowAllNotificationsData, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                    .addComponent(btnClearTextArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdateKey, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUpdateNotificationSettings, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnShowSelectedNotificationData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPriceToPercentage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnShowAllNotificationsData)
                    .addComponent(btnShowSelectedNotificationData))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateNotificationSettings)
                    .addComponent(btnClearTextArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPriceToPercentage)
                    .addComponent(btnUpdateKey))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(5108, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddOrUpdateCoinSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddOrUpdateCoinSettingsActionPerformed
        // TODO add your handling code here:
        List<String> selectedValuesList = listOfCoins.getSelectedValuesList();
        if (selectedValuesList.size() == 0) {
            System.out.println("No Coins Selected");
            taNotifications.append("\nNo Coin Settings Updated");
        } else {
            for (String coin : selectedValuesList) {
                //String coin = txtCoinName.getText().trim().toUpperCase();
                String percentUp = txtPercentUp.getText().trim().toUpperCase();
                String percentDown = txtPercentDown.getText().trim().toUpperCase();
                if (coin != null && !coin.equals("")) {
                    //Add coin to list only if we can get data from CMC
                    DefaultListModel model = (DefaultListModel) listOfCoins.getModel();
                    boolean contains = model.contains(coin);
                    if (!contains) {
                        try {
                            System.out.println("New COIN, Calling CMC API");
                            model.addElement(coin);
                            taNotifications.append("\nNEW Coin, Calling CMC API\n");
                            String json = PricePerTicker.getDataForTicker(apiKey, coin);
                            Double price = PricePerTicker.getCurrentPriceOfTicker(json, coin, "USD");
                            AlertData data = saveToPreference(coin, percentUp, percentDown, price);
                            taNotifications.append("\nAdding : " + data);

                            writeToFile("coins.txt", data);
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        //Simply update the data
                        AlertData data = mapOfData.get(coin);
                        data.percentDown = percentDown;
                        data.percentUp = percentUp;

                        data = saveToPreference(coin, percentUp, percentDown, data.price);
                        taNotifications.append("\nUpdated : " + data);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnAddOrUpdateCoinSettingsActionPerformed

    private void btnRemoveCoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveCoinActionPerformed
        // TODO add your handling code here:
        String coin = txtCoinName.getText().trim().toUpperCase();
        if (coin != null && !coin.equals("")) {
            //Add coin to list only if we can get data from CMC
            DefaultListModel model = (DefaultListModel) listOfCoins.getModel();
            boolean contains = model.contains(coin);
            if (contains) {
                model.removeElement(coin);
                //removeFromFile("coins.txt", coin);
            }
            String data = removeFromPreference(coin);
            taNotifications.append("\nRemoved : " + data);
        }
    }//GEN-LAST:event_btnRemoveCoinActionPerformed

    private void btnShowSelectedNotificationDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowSelectedNotificationDataActionPerformed
        // TODO add your handling code here:
        String coin = listOfCoins.getSelectedValue();
        if (coin != null) {
            Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
            String data = prefs.get(coin, null);
            if (data != null) {
                taNotifications.append("\nSettings : " + data);
            }
        }
    }//GEN-LAST:event_btnShowSelectedNotificationDataActionPerformed

    private void btnShowAllNotificationsDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowAllNotificationsDataActionPerformed
        try {

            Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
            String[] keys = prefs.keys();
            StringBuilder sb = new StringBuilder();
            for (String s : keys) {
                if (!s.equals("key")) {
                    AlertData data = new AlertData(prefs.get(s, null));
                    sb.append(data.fullSettings().replace("_", "\t") + "\n");
                    writeToFile("coins.txt", data);
                }
            }
            // TODO add your handling code here:
            taNotifications.append("\n\nNotification Settings: \n");

            taNotifications.append("\nCoin\tUp %\tDown %\tPrice $\tUp $\tDown $");
            taNotifications.append("\n----\t----\t------\t-------\t----\t------");
            taNotifications.append("\n" + sb.toString());
            System.out.println(sb.toString());
        } catch (Exception ex) {
            Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnShowAllNotificationsDataActionPerformed

    private void txtPercentDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPercentDownActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPercentDownActionPerformed

    private void listOfCoinsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listOfCoinsMouseClicked
        // TODO add your handling code here:
        String coin = listOfCoins.getSelectedValue();
        if (coin != null) {
            AlertData data = mapOfData.get(coin);
            if (data != null) {
                txtCoinName.setText(data.coin);
                txtPercentUp.setText(data.percentUp);
                txtPercentDown.setText(data.percentDown);
            }
        }
    }//GEN-LAST:event_listOfCoinsMouseClicked

    private void listOfCoinsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listOfCoinsKeyReleased
        // TODO add your handling code here:
        String coin = listOfCoins.getSelectedValue();
        if (coin != null) {
            AlertData data = mapOfData.get(coin);
            if (data != null) {
                txtCoinName.setText(data.coin);
                txtPercentUp.setText(data.percentUp);
                txtPercentDown.setText(data.percentDown);
            }
        }
    }//GEN-LAST:event_listOfCoinsKeyReleased

    private void btnUpdateNotificationSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateNotificationSettingsActionPerformed
        try {
            // TODO add your handling code here:
            String json = PricePerTicker.getDataForTicker(apiKey, "BTC");
            if (json.contains("This API Key is invalid")) {
                taNotifications.append("\nCheck CMC API Key Is Valid");
                throw new Exception("CMC API Key is invalid");
            }
            List<String> selectedValuesList = listOfCoins.getSelectedValuesList();
            if (selectedValuesList.size() == 0) {
                selectedValuesList.add(txtCoinName.getText().trim().toUpperCase());
            }

            for (String coin : selectedValuesList) {
                double prevPrice = 0;
                double currentPrice = 0;
                //String coin = txtCoinName.getText().trim().toUpperCase();
                if (coin != null && !coin.equals("")) {
                    //Add coin to list only if we can get data from CMC
                    DefaultListModel model = (DefaultListModel) listOfCoins.getModel();
                    boolean contains = model.contains(coin);
                    if (contains) {
                        model.removeElement(coin);
                    }
                    String data = removeFromPreference(coin);
                    prevPrice = Double.parseDouble(data.split("_")[3]);
                    taNotifications.append("\n-----------------------------\nRemoved : \t" + data);
                }

                String percentUp = txtPercentUp.getText().trim().toUpperCase();
                String percentDown = txtPercentDown.getText().trim().toUpperCase();
                if (coin != null && !coin.equals("")) {
                    //Add coin to list only if we can get data from CMC
                    DefaultListModel model = (DefaultListModel) listOfCoins.getModel();
                    boolean contains = model.contains(coin);
                    if (!contains) {
                        try {
                            json = PricePerTicker.getDataForTicker(apiKey, coin);
                            Double price = PricePerTicker.getCurrentPriceOfTicker(json, coin, "USD");
                            AlertData data = saveToPreference(coin, percentUp, percentDown, price);
                            currentPrice = data.price;
                            taNotifications.append("\nAdding : \t" + data);
                            taNotifications.append("\nPercent : \t" + data.calculatePercentChanged(prevPrice, currentPrice, 3));

                            model.addElement(coin);
                            writeToFile("coins.txt", data);
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        //Simply update the data
                        AlertData data = mapOfData.get(coin);
                        data.percentDown = percentDown;
                        data.percentUp = percentUp;

                        data = saveToPreference(coin, percentUp, percentDown, data.price);
                        taNotifications.append("\nUpdated : " + data);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnUpdateNotificationSettingsActionPerformed

    private void btnClearTextAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearTextAreaActionPerformed
        // TODO add your handling code here:
        taNotifications.setText("");
    }//GEN-LAST:event_btnClearTextAreaActionPerformed

    private void btnPriceToPercentageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPriceToPercentageActionPerformed
        // TODO add your handling code here:
        String coin = txtCoinName.getText().trim().toUpperCase();
        if (coin != null && !coin.equals("")) {
            //Simply update the data
            AlertData data = mapOfData.get(coin);

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= 100; i++) {
                sb.append("\n-------------------------\n");
                sb.append(data.calculatePrice(true, i));

                if (i <= 100) {
                    sb.append("\n");
                    sb.append(data.calculatePrice(false, i));
                }
            }
            taNotifications.append("\nPercent to price UP and Down: " + data.price + "\n" + sb.toString());
        }
    }//GEN-LAST:event_btnPriceToPercentageActionPerformed

    private void btnUpdateKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateKeyActionPerformed
        try {
            // TODO add your handling code here:
            String key = txtCMCKey.getText();
            String json = PricePerTicker.getDataForTicker(key, "BTC");
            if (json.contains("This API Key is invalid")) {
                taNotifications.append("\nCheck CMC API Key Is Valid");
                throw new Exception("CMC API Key is invalid");
            }
            //Key found
            Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
            prefs.put("key", key);
            apiKey = key;

            //Update file
            FileUtils.writeToFile(true, "key.txt", "new", key);

            taNotifications.append("\nCMC Key Updated : " + key);

        } catch (Exception ex) {
            Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnUpdateKeyActionPerformed

    private void listOfCoinRatioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listOfCoinRatioMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_listOfCoinRatioMouseClicked

    private void listOfCoinRatioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_listOfCoinRatioKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_listOfCoinRatioKeyReleased

    private void btnShowCoinRatioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCoinRatioActionPerformed
        // TODO add your handling code here:
        String ratioCoin = listOfCoinRatio.getSelectedValue();
        String compareCoin = listOfCoins.getSelectedValue();
        String ratio = "1:1";
        if (ratioCoin != null) {
            if (compareCoin != null && ratioCoin.equals(compareCoin)) {
                taNotifications.append(String.format("\nRatio between %s and %s is 1:1", compareCoin, ratioCoin));
            } else if (compareCoin != null) {
                AlertData from = mapOfData.get(compareCoin);
                AlertData to = mapOfData.get(ratioCoin);
                ratio = to.calculateRatio(from.price, 8);
                taNotifications.append(String.format("\nRatio between %s and %s is %s", compareCoin, ratioCoin, ratio));
            }
        }
        String time = new SimpleDateFormat("YYYY-MM-DD-hh:mm").format(Calendar.getInstance().getTime());
        FileUtils.writeToFile("ratio.txt", compareCoin + "_" + ratioCoin + "_" + time + "_" + ratio);
    }//GEN-LAST:event_btnShowCoinRatioActionPerformed

    private void txtPercentUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPercentUpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPercentUpActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Notifications.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Notifications.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Notifications.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Notifications.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    new Notifications().setVisible(true);
//                } catch (BackingStoreException ex) {
//                    Logger.getLogger(Notifications.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddOrUpdateCoinSettings;
    private javax.swing.JButton btnClearTextArea;
    private javax.swing.JButton btnPriceToPercentage;
    private javax.swing.JButton btnRemoveCoin;
    private javax.swing.JButton btnShowAllNotificationsData;
    private javax.swing.JButton btnShowCoinRatio;
    private javax.swing.JButton btnShowSelectedNotificationData;
    private javax.swing.JButton btnUpdateKey;
    private javax.swing.JButton btnUpdateNotificationSettings;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> listOfCoinRatio;
    private javax.swing.JList<String> listOfCoins;
    private javax.swing.JTextArea taNotifications;
    private javax.swing.JTextField txtCMCKey;
    private javax.swing.JTextField txtCoinName;
    private javax.swing.JTextField txtPercentDown;
    private javax.swing.JTextField txtPercentUp;
    // End of variables declaration//GEN-END:variables

    private AlertData saveToPreference(String coin, String percentUp, String percentDown, Double price) {
        //To change body of generated methods, choose Tools | Templates.
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        AlertData data = new AlertData(coin, percentUp, percentDown, price);
        prefs.put(coin, data.toString());
        mapOfData.put(coin, data);
        return data;
    }

    private String removeFromPreference(String coin) {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        String prefData = prefs.get(coin, null);
        System.out.println("Removing preference data : " + prefData);
        prefs.remove(coin);
        mapOfData.remove(coin);
        return prefData;
    }

    private void writeToFile(String coinstxt, AlertData data) {
        FileUtils.writeToFile(coinstxt, data);
    }

    private void removeFromFile(String coinstxt, String coin) {
        FileUtils.removeFromFile(coinstxt, coin);

    }
}
