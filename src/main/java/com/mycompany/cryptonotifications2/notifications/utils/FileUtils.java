/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptonotifications2.notifications.utils;

import com.mycompany.cryptonotifications2.MainNotificationFrame;
import com.mycompany.cryptonotifications2.pojo.AlertData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author tayyibah
 */
public class FileUtils {

    public static String readFile(String name) {
        try {
            if (!Files.exists(Paths.get(name))) {
                new File(name).createNewFile();
            }
            //String content = new String(Files.readAllBytes(Paths.get(name)));
            final StringBuilder sb = new StringBuilder();
            Stream<String> lines = Files.lines(Paths.get(ClassLoader.getSystemResource(name).getFile()));
            lines.forEach(l -> sb.append(l));
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String readFile2(String name) {
        try {
            if (!Files.exists(Paths.get(name))) {
                new File(name).createNewFile();
            }
            //String content = new String(Files.readAllBytes(Paths.get(name)));
            final StringBuilder sb = new StringBuilder();
            List<String> lines = Files.readAllLines(Paths.get(name));
            lines.forEach(l -> sb.append(l));
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Write notifications data to file
     * @param fileName
     * @param data 
     */
    public static void writeToFile(String fileName, AlertData data) {
        try {
            if (!Files.exists(Paths.get(fileName))) {
                new File(fileName).createNewFile();
            }

            List<String> lines = Files.readAllLines(Paths.get(fileName));
            StringBuilder sb = new StringBuilder();
            lines.stream().forEach(l -> {
                sb.append(l);
            });

            String fileContent = sb.toString();
            if (!fileContent.contains(data.coin)) {
                Files.write(Paths.get(fileName), ("\n" + data.toString()).getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param fileName
     * @param data 
     */
    public static void writeToFile(String fileName, String data) {
        try {
            if (!Files.exists(Paths.get(fileName))) {
                new File(fileName).createNewFile();
            }

            Files.write(Paths.get(fileName), ("\n" + data).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param remove    Remove the file
     * @param fileName  File name
     * @param mode      Append or create new file
     * @param data  
     */
    public static void writeToFile(boolean remove, String fileName, String mode, String data) {
        try {
            File file = new File(fileName);
//            if(remove)
//                file.delete();
            if (!Files.exists(Paths.get(fileName))) {
                file.createNewFile();
            }

            List<String> lines = Files.readAllLines(Paths.get(fileName));
            StringBuilder sb = new StringBuilder();
            lines.stream().forEach(l -> {
                sb.append(l);
            });

            
            if (mode.equalsIgnoreCase("append")) {
                Files.write(Paths.get(fileName), ("\n" + data + "\n" + sb.toString().replace("Date: ", "\n\nDate: ").replace("Notification settings", "\nNotification settings") ).getBytes());
            } else {
                Files.write(Paths.get(fileName), ( "\n" + data ).getBytes());
            }

        } catch (IOException ex) {
            Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void removeFromFile(String coinstxt, String coin) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(coinstxt));
            List<String> result = lines.stream() // convert list to stream
                    .filter(line -> line.contains(coin)) // we dont like mkyong
                    .collect(Collectors.toList());
            System.out.println("");
            lines.remove(result.get(0));

            //Write to file
            StringBuilder sb = new StringBuilder();
            lines.stream().forEach(l -> {
                sb.append(l + "\n");
            });

            String fileContent = sb.toString();
            Files.write(Paths.get(coinstxt), ("\n" + fileContent).getBytes(), StandardOpenOption.CREATE);

        } catch (IOException ex) {
            Logger.getLogger(MainNotificationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
