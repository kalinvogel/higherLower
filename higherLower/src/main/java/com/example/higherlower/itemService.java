package com.example.higherlower;

import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.util.*;

public class itemService {
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private List<item> items = new ArrayList<>();
    private int highScore;
    private Map<String, List<item>> itemLists;
    private final int MAX_RETRIES = 3;

    private List<item> rustList;
    private List<item> csgoList;
    private List<item> pubgList;
    public itemService() {
        itemLists = new HashMap<>();
    }


    public List<item> chooseList(String game) {
        switch (game){
            case "Rust":
                List<item> rustList = new ArrayList<>();
                return rustList;
            case "Csgo":
                return csgoList;
            case "Pubg":
                return pubgList;
            default:
                throw new IllegalArgumentException("Invalid game: "+game);
        }
    }

    public List<item> returnItems() {
        return items;
    }

    public int getItemsLength(){
        return items.size();
    }


    public void add2ToList() throws FileNotFoundException {
        addItemToList();
        addItemToList();
    }

    public void remove2FromList(){
        items.removeFirst();
        items.removeFirst();
    }



    public void addItemToList() throws FileNotFoundException {
        item item1 = null;
        int count = 0;
        while (item1 == null && count < MAX_RETRIES) {
            String item1Name = getRandomItem("data/RustItemsList.json");
            item1 = getItemDetails(item1Name, "http://steamcommunity.com/market/listings/252490/");
            count++;
        }
        items.add(item1);
    }

    public void add2ItemsToList() throws FileNotFoundException {
        addItemToList();
        addItemToList();
    }

    private String getRandomItem(String file) {
        try (Reader reader = new FileReader(file)) {
            JsonData jsonData = gson.fromJson(reader, JsonData.class);
            String[] items = jsonData.getItems().toArray(new String[0]);
            Random random = new Random();
            return items[random.nextInt(items.length)];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private item getItemDetails(String itemName, String steamUrl) throws FileNotFoundException {
        String modifiedItemName = itemName.replace(" ", "%20");
        modifiedItemName = itemName.replace("'", "%27");
        // count - only want one item to be returned in response, currency 1 - return in USD
        String fullUrl = steamUrl + modifiedItemName + "/render?start=0&count=1&currency=1";
        Request request = new Request.Builder().url(fullUrl).build();
        //System.out.println(request);

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println(itemName);
                assert response.body() != null; // Check if nothing there
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

                if (jsonObject.get("success").getAsString().equals("false")) {
                    System.out.println("Request failed or no results found.");
                    return null;
                }

                String salePriceString = null;
                String iconUrl = null;

                if (jsonObject.has("listinginfo")) { //
                    if (jsonObject.get("listinginfo").isJsonObject()) {
                        JsonObject listingInfo = jsonObject.getAsJsonObject("listinginfo");
                        int price = 0;
                        int fee = 0;
                        for (String key : listingInfo.keySet()) {
                            JsonObject listing = listingInfo.getAsJsonObject(key);
                            price = listing.get("converted_price_per_unit").getAsInt();
                            fee = listing.get("converted_fee_per_unit").getAsInt();
                        }
                        int salePrice = price + fee;
                        salePriceString = String.valueOf(salePrice);
                        System.out.println("Converted Price: " + salePrice);
                    } else if (jsonObject.get("listingInfo").isJsonArray()) {
                        JsonArray listingInfoArray = jsonObject.getAsJsonArray("listinginfo");
                        JsonObject listingInfo = listingInfoArray.get(0).getAsJsonObject();
                        int price = listingInfo.get("converted_price_per_unit").getAsInt();
                        int fee = listingInfo.get("converted_fee_per_unit").getAsInt();
                        int salePrice = price + fee;
                        salePriceString = String.valueOf(salePrice);
                        System.out.println("Converted Price: " + salePrice);
                    }
                } else {
                    System.out.println("Unexpected JSON format for listinginfo.");
                    return null;
                }

                if (jsonObject.has("assets")) {
                    JsonObject assets = jsonObject.getAsJsonObject("assets");
                    JsonObject gameAssets = assets.getAsJsonObject("252490");
                    JsonObject contextAssets = gameAssets.getAsJsonObject("2");
                    assert contextAssets != null;
                    for (String assetID : contextAssets.keySet()) {
                        JsonObject asset = contextAssets.getAsJsonObject(assetID);
                        iconUrl = asset.get("icon_url").getAsString();
                    }
                    iconUrl = "https://steamcommunity-a.akamaihd.net/economy/image/" + iconUrl;
                }

                return new item(null, null, itemName, salePriceString, iconUrl);
            } else {
                System.out.println("Response code" + response.code());
                System.out.println("Failed to get item details for item: " + itemName);
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void badAPICall() throws FileNotFoundException {
        System.out.println("badAPICall called");
        items.removeLast();
        addItemToList();
        System.out.println("Items after badAPICall" + returnItems());
    }


    public boolean correctButtonPressed(boolean isButton1) {
        //System.out.println("Doing correct button pressed");
        item item1 = items.get(0);
        item item2 = items.get(1);
        int price1 = Integer.parseInt(item1.getPrice());
        int price2 = Integer.parseInt(item2.getPrice());
        //System.out.println("Price1: " + price1);
        //System.out.println("Price2: " + price2);
        if ((price1 > price2 && isButton1) || (price1 < price2 && !isButton1)) {
            return true;
        }
        return false;
    }
}