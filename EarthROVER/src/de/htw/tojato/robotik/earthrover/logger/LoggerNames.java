package de.htw.tojato.robotik.earthrover.logger;

/**
 * Beinhaltet die einzelnen Logger
 *
 * @author Tobias Kalmes
 */
public enum LoggerNames {
    MAIN_LOGGER("mainLogger"),
    BRICK_LOGGER("BRICK_LOGGER");
    private String name;

    LoggerNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
