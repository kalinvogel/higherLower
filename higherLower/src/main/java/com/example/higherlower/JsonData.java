package com.example.higherlower;

import java.util.List;

public class JsonData {
    public final int fileLength;
    public final List<String> items;

    public JsonData(int fileLength, List<String> items) {
        this.fileLength = fileLength;
        this.items = items;
    }

    // Getters for JSON serialization
    public int getFileLength() {
        return fileLength;
    }

    public List<String> getItems() {
        return items;
    }
}
