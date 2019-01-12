/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptonotifications2;

import com.mycompany.cryptonotifications2.workers.NotificationPolling;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javax.swing.JFrame;

/**
 *
 * @author tayyibah
 */
public class CryptoNotifications extends JFrame{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String pollingMin;
        if(args.length > 0){
            pollingMin = args[0];
            if(pollingMin!=null)
            System.out.println("Polling every : " + pollingMin);
        }else{
            pollingMin = "10";
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainNotificationFrame notifications;
                try {
                    notifications = new MainNotificationFrame(pollingMin);
                    notifications.setTitle("Desktop Crypto Notification Generator");
                    notifications.setResizable(true);
                    notifications.pack();
                    notifications.setVisible(true);
                } catch (BackingStoreException ex) {
                    Logger.getLogger(CryptoNotifications.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }
    
}
