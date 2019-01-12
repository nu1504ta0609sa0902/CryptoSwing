/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cryptonotifications2.notifications;

import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author tayyibah
 */
public class PricePerTicker {
    static String endpoint = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?CMC_PRO_API_KEY=%s&symbol=%s";

    /**
     * 
     * @param key       API key
     * @param ticker    BTC, LTC 
     * @return
     * @throws MalformedURLException 
     */
    public static String getDataForTicker(String key, String ticker) throws MalformedURLException {
        String query = String.format(endpoint, key, ticker);
        ResponseBody body = RestAssured.given().get(new URL(query)).getBody();
        System.out.println(body.prettyPrint());
        return body.prettyPrint();
    }

    /**
     * 
     * @param json
     * @param ticker    BTC, LTC 
     * @param currency  USD
     * @return 
     */
    public static Double getCurrentPriceOfTicker(String json, String ticker, String currency) {
        Double currentPrice = com.jayway.jsonpath.JsonPath.parse(json).read(String.format("$.data.%s.quote.%s.price", ticker, currency));
        return currentPrice;
    }
    
    public static Double getCurrentPriceOfTickerDynamic(String json, String coin, String currency) {
        try{
        net.minidev.json.JSONArray array = com.jayway.jsonpath.JsonPath.parse(json).read(String.format("$.data[?(@.symbol=='%s')].quote.%s.price", coin, currency));
        String x = array.get(0).toString();
        return Double.parseDouble(x);
        }catch(Exception e){
            //Error mainly if its outside of top 100 e.printStackTrace();
            return null;
        }
    }
    
}
