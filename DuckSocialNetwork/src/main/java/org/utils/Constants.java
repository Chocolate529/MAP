package org.utils;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class Constants {
    private static final Path BASE_PATH = Path.of(System.getProperty("user.dir"), "src", "main", "java", "org");
    private static final Path BASE_PATH_RESOURCES = Path.of(System.getProperty("user.dir"), "src", "main", "resources");

    public static final String PERSON_INPUT_FILE = BASE_PATH_RESOURCES.resolve("persons").toString();
    public static final String DUCK_INPUT_FILE = BASE_PATH_RESOURCES.resolve("ducks").toString();
    public static final String FRIENDSHIPS_INPUT_FILE = BASE_PATH_RESOURCES.resolve("friendships").toString();
    public static final String FLOCKS_INPUT_FILE = BASE_PATH_RESOURCES.resolve("flocks").toString();

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
}