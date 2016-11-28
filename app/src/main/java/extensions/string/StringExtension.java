package extensions.string;

/**
 * Created by Adrian on 27.11.2016.
 */

public class StringExtension {
    public static boolean isNullOrEmpty(String source) {
        return !(source != null && !source.isEmpty());
    }
}
