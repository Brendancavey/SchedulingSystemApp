/** Author: Brendan Thoeung | Date: 9/19/2022
 * */
package interfaces;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

/** This is a functinalInterface Loader.
 * This interface is to load different pages
 * and get the controller methods of those pages to use
 * within a different scene. Primarily used
 * to send information from one scene to another. */
@FunctionalInterface
public interface Loader {
    void loadPage(FXMLLoader loader, String resource) throws IOException;
}
