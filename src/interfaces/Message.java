package interfaces;


import javafx.scene.control.Alert;


@FunctionalInterface
public interface Message {
    void displayMessage(String m);
}
