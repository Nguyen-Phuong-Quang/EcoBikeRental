module Programming {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires lombok;
    requires mongo.java.driver;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires commons.lang3;
    requires reflections;
    requires java.sql;
    requires java.base;

    opens org.hust to javafx.fxml;
    opens org.hust.views to javafx.fxml;
    opens org.hust.views.home to javafx.fxml;
    opens org.hust.views.payment to javafx.fxml;
    opens org.hust.views.popup to javafx.fxml;
    opens org.hust.views.rentbike to javafx.fxml;
    opens org.hust.views.returnbike to javafx.fxml;
    opens org.hust.entity.station to com.fasterxml.jackson.databind;
    opens org.hust.entity.bike to com.fasterxml.jackson.databind;
    opens org.hust.entity.invoice to com.fasterxml.jackson.databind;
    opens org.hust.entity.payment to com.fasterxml.jackson.databind;
    opens org.hust.utils to com.fasterxml.jackson.databind;
    opens org.hust.views.invoice to javafx.fxml;
    opens org.hust.views.menu to javafx.fxml;
    exports org.hust;
}