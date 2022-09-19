package interfaces;

import java.util.ResourceBundle;

@FunctionalInterface
public interface Language {
    void displayInFrench(ResourceBundle rb, String message);
}
