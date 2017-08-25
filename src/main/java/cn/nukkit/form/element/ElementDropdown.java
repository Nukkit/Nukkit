package cn.nukkit.form.element;

import java.util.ArrayList;

public class ElementDropdown extends Element {

    public String type = "dropdown";
    public String text = "";
    public ArrayList<String> options;
    public int defaultOptionIndex = 0;

    public ElementDropdown(String text){
        this (text, new ArrayList<>());
    }
    public ElementDropdown(String text, ArrayList<String> options){
        this (text, options, 0);
    }
    public ElementDropdown(String text, ArrayList<String> options, int defaultOption){
        this.text = text;
        this.options = options;
        this.defaultOptionIndex = defaultOption;
    }

    public void setDefaultOptionIndex(int index){
        if (index >= options.size()) return;
        this.defaultOptionIndex = index;
    }
    public void addOption(String option){
        addOption(option, false);
    }
    public void addOption(String option, boolean isDefault){
        options.add(option);
        if (isDefault) this.defaultOptionIndex = options.size()-1;
    }

}
