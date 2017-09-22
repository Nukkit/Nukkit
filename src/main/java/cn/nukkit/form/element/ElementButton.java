package cn.nukkit.form.element;

public class ElementButton {

    public String text = "";
    public ElementButtonImageData image;

    public ElementButton(String text) {
        this.text = text;
    }

    public ElementButton(String text, ElementButtonImageData image) {
        this.text = text;
        if (!image.data.isEmpty() && !image.type.isEmpty()) this.image = image;
    }

    public void addImage(ElementButtonImageData image) {
        if (!image.data.isEmpty() && !image.type.isEmpty()) this.image = image;
    }

}
