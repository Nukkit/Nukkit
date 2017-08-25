package cn.nukkit.form.element;

public class ElementButton {

    public String text = "";
    public ElementButtonImageData data;

    public ElementButton(String text){
        this.text = text;
    }
    public ElementButton(String text, ElementButtonImageData data){
        this.text = text;
        if (!data.data.isEmpty() && !data.type.isEmpty()) this.data = data;
    }

    public void addImage(ElementButtonImageData data){
        if (!data.data.isEmpty() && !data.type.isEmpty()) this.data = data;
    }

}
