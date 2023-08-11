package org.hust;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hust.utils.Configs;
import org.hust.views.home.HomeScreenHandler;

import java.io.IOException;
import java.util.Objects;

/**
 * This is the Application class for EcoBikeRentalService software.
 */
public class App extends Application {

    @FXML
    ImageView logo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // initialize the scene
            AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Configs.SPLASH_SCREEN_PATH)));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Configs.IMAGE_PATH + "logo.jpg"))));
            primaryStage.setTitle("ECOB - EcoPark Bike Rental service");
            primaryStage.show();

            // Load splash screen with fade in effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            // Finish splash with fade out effect
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            // After fade in, start fade out
            fadeIn.play();
            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            // After fade out, load actual content
            fadeOut.setOnFinished((e) -> {
                try {
                    HomeScreenHandler homeHandler = new HomeScreenHandler(primaryStage, Configs.HOME_PATH);
                    homeHandler.setScreenTitle("EcoBike Rental - Home");
                    homeHandler.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
