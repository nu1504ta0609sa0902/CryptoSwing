/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptonotifications2.notifications;

import io.restassured.RestAssured;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author tayyibah
 */
public class PriceListOfTickers {
    static String endpoint = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?CMC_PRO_API_KEY=%s";

    /**
     * TOP 100 coins only
     * @param key
     * @return
     * @throws MalformedURLException 
     */
    public static String getDataForAllTickers(String key) throws MalformedURLException {
        String query = String.format(endpoint, key);
        String json = RestAssured.given().get(new URL(query)).getBody().prettyPrint();
        System.out.println("\n-----TOP 100-----\n");
        return json;
    }
}
