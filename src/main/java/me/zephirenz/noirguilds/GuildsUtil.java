package me.zephirenz.noirguilds;

public class GuildsUtil {

    /**
     * Check whether a tag is valid (ie on alphanumeric, period, dash or underscore)
     * @param tag the tag to be checked
     */
    public static boolean isValidTag(String tag) {
        return tag.matches("[a-zA-Z0-9.-_]+") && tag.length() <= 4;
    }

    public static String arrayToString(String[] arr, int start, int end, String conn) {
        StringBuilder buffer = new StringBuilder();
        for (int i = start; i <= end; i++) {
            buffer.append(conn).append(arr[i]);
        }
        return buffer.toString();
    }

}
