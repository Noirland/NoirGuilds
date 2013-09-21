package me.zephirenz.noirguilds;

public class Util {

    /**
     * Check whether a tag is valid (ie on alphanumeric, period, dash or underscore)
     * @param tag the tag to be checked
     */
    public static boolean isValidTag(String tag) {
        return tag.matches("[a-zA-Z0-9.-_]+") && tag.length() <= 4;
    }

}
