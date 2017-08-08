package cn.nukkit.form.element;

public class ElementToggle extends Element {

    public String type = "toggle";
    public String text = "";
    public boolean defaultValue = false;

    public ElementToggle(String text) {
        this (text, false);
    }
    public ElementToggle(String text, boolean defaultValue){
        this.text = text;
        this.defaultValue = defaultValue;
    }

}
