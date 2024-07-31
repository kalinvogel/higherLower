package com.example.higherlower;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class scraper {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void scrapeRustClashForItems(String file) throws IOException {
        List<String> itemNamesList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://wiki.rustclash.com/skins").get();
            //Elements itemNames = doc.select("a[data-marketable='0']");
            Elements itemNames = doc.select("a[data-marketable='0']:not([data-item='steam-item'])");

            for (Element itemName : itemNames) {
                String name = itemName.attr("data-name");
                itemNamesList.add(name);
            }

            try (FileWriter writer = new FileWriter(file)) {
                String json = gson.toJson(new JsonData(itemNamesList.size(), itemNamesList));
                writer.write(json);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to scrape items from RustClash", e);
        }

    }

    public static void cleanBadResponses(String file) throws IOException {

    }
}
