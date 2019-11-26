package us.antenado.sthetosample;

public class UrlUtil {

    private UrlUtil() {

    }

    public static String buildUrl(String path) {
        if (path.startsWith("https://")) {
            return path;
        } else if (path.startsWith("content/")) {
            return "https://hippo.com/" + path;
        } else if (path.startsWith("help/")) {
            return "https://help.discovery.co.za/" + path;
        } else {
            return "https://discovery.co.za/" + path;
        }
    }
}
