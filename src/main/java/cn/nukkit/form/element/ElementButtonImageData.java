package cn.nukkit.form.element;

public class ElementButtonImageData {

    public static final String IMAGE_DATA_TYPE_PATH = "path";
    public static final String IMAGE_DATA_TYPE_URL = "url";

    public String type;
    public String data;

    public ElementButtonImageData(String type, String data) {
        if (!type.equals(IMAGE_DATA_TYPE_URL) && !type.equals(IMAGE_DATA_TYPE_PATH)) return;
        this.type = type;
        this.data = data;
    }

}
