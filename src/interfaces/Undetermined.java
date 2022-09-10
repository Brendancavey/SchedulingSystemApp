package interfaces;

import java.time.LocalTime;

@FunctionalInterface
public interface Undetermined {
    public String determineAmPm(LocalTime time);
}
