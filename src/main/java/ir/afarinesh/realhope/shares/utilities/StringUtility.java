package ir.afarinesh.realhope.shares.utilities;

public class StringUtility {

    public static String firstLowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String convertCamelToDash(String str) {
        return convertCamelTo(str, "-");
    }

    public static String convertCamelToUnderLine(String str) {
        return convertCamelTo(str, "_");
    }

    protected static String convertCamelTo(String str, String separator) {
        if (str == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch =str.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i > 0) {
                    result.append(separator);
                }
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static String space(int count) {
        return " ".repeat(Math.max(0, count));
    }
}
