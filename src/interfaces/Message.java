package interfaces;


import javafx.scene.control.Alert;

/** This is a functinalInterface Message.
 * This interface is to display pop up windows
 * with different alert types.*/
@FunctionalInterface
public interface Message {
    void displayMessage(Alert alert, String message);
}
