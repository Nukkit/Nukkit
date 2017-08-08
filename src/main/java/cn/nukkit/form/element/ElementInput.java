package cn.nukkit.form.element;

public class ElementInput extends Element {

    public String type = "input";
    public String text = "";
    public String placeholder = "";
    public String defaultText = "";

    public ElementInput (String text){
        this (text, "");
    }
    public ElementInput (String text, String placeholder){
        this (text, placeholder, "");
    }
    public ElementInput (String text, String placeholder, String defaultText){
        this.text = text;
        this.placeholder = placeholder;
        this.defaultText = defaultText;
    }

}
