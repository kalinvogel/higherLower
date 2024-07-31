package com.example.higherlower;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class item {
    private String name;
    private String price;
    private String imageUrl;
    private String dateCreated;
    private String type;

    public item(String dateCreated, String type, String name, String price, String imageUrl) {
        this.dateCreated = dateCreated;
        this.type = type;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getDateCreated() {
        return dateCreated;
    }
    public String getType() {
        return type;
    }

    public int getPriceAsInt() {
        try {
            return Integer.parseInt(price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0; // or handle the error as needed
        }
    }





    public item higherPriceItem(item item1,item item2){
        /*
        int item1Price = item1.getPriceAsInt();
        int item2Price = item2.getPriceAsInt();
        if (item1Price>item2Price) {
            return item1;
        }else{
            return item2;
        }*/
        return item1.getPriceAsInt()>item2.getPriceAsInt()?item1:item2;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
