module com.example.higherlower {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires org.json;
    requires org.jsoup;
    requires com.google.gson;


    opens com.example.higherlower to javafx.fxml;
    exports com.example.higherlower;
}