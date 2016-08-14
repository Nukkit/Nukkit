package cn.nukkit.utils;

public final class ChatFormat {

    public static final char ESCAPE = '\u00a7';

    public static final String BLACK = ChatFormat.ESCAPE + "0";
    public static final String DARK_BLUE = ChatFormat.ESCAPE + "1";
    public static final String DARK_GREEN = ChatFormat.ESCAPE + "2";
    public static final String DARK_AQUA = ChatFormat.ESCAPE + "3";
    public static final String DARK_RED = ChatFormat.ESCAPE + "4";
    public static final String DARK_PURPLE = ChatFormat.ESCAPE + "5";
    public static final String GOLD = ChatFormat.ESCAPE + "6";
    public static final String GRAY = ChatFormat.ESCAPE + "7";
    public static final String DARK_GRAY = ChatFormat.ESCAPE + "8";
    public static final String BLUE = ChatFormat.ESCAPE + "9";
    public static final String GREEN = ChatFormat.ESCAPE + "a";
    public static final String AQUA = ChatFormat.ESCAPE + "b";
    public static final String RED = ChatFormat.ESCAPE + "c";
    public static final String LIGHT_PURPLE = ChatFormat.ESCAPE + "d";
    public static final String YELLOW = ChatFormat.ESCAPE + "e";
    public static final String WHITE = ChatFormat.ESCAPE + "f";

    public static final String OBFUSCATED = ChatFormat.ESCAPE + "k";
    public static final String BOLD = ChatFormat.ESCAPE + "l";
    public static final String STRIKETHROUGH = ChatFormat.ESCAPE + "m";
    public static final String UNDERLINE = ChatFormat.ESCAPE + "n";
    public static final String ITALIC = ChatFormat.ESCAPE + "o";
    public static final String RESET = ChatFormat.ESCAPE + "r";

    public static String clean(String message) {
        return clean(message, true);
    }

    public static String clean(String message, boolean removeFormat) {
        message = message.replaceAll((char) 0x1b + "[0-9;\\[\\(]+[Bm]", "");
        return removeFormat ? message.replaceAll(ESCAPE + "[0123456789abcdefklmnor]", "") : message;
    }

    public static String colorize(String textToColorize) {
        char[] b = textToColorize.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if ((b[i] == '&') && ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[(i + 1)]) > -1)) {
                b[i] = ESCAPE;
                b[(i + 1)] = Character.toLowerCase(b[(i + 1)]);
            }
        }
        return new String(b);
    }

    public static String getLastColors(String input) {
        String result = "";
        int length = input.length();
        for (int index = length - 1; index > -1; index--) {
            char section = input.charAt(index);
            if (section == ESCAPE && index < length - 1) {
                char c = input.charAt(index + 1);
                String color = ESCAPE + c + "";
                result = color + result;

                if (isColor(c) || c == 'r' || c == 'R') {
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isColor(char c) {
        String colors = "0123456789AaBbCcDdEeFf";
        return colors.indexOf(c) != -1;
    }

}