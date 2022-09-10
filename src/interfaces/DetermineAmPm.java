package interfaces;

import java.time.LocalTime;

@FunctionalInterface
public interface DetermineAmPm {
    public String determineAmPm(LocalTime time);
}
