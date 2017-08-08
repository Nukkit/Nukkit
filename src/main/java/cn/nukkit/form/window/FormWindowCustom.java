package cn.nukkit.form.window;

import com.google.gson.Gson;
import cn.nukkit.form.element.*;
import cn.nukkit.form.response.FormResponseData;
import cn.nukkit.form.response.FormResponseCustom;

import java.util.ArrayList;
import java.util.HashMap;

public class FormWindowCustom extends FormWindow {

    public String type = "custom_form";
    public String title = "";
    public ElementButtonImageData icon;
    public ArrayList<Element> content;

    private FormResponseCustom response;

    public FormWindowCustom(String title){
        this (title, new ArrayList<>());
    }
    public FormWindowCustom(String title, ArrayList<Element> contents){
        this (title, contents, "");
    }
    public FormWindowCustom(String title, ArrayList<Element> contents, String icon){
        this.title = title;
        this.content = contents;
        if (!icon.isEmpty()) this.icon = new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_URL, icon);
    }

    public void addElement(Element element){
        content.add(element);
    }
    public void setIcon(String icon){
        if (!icon.isEmpty()) this.icon = new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_URL, icon);
    }

    public String getJSONData(){
        String toModify = new Gson().toJson(this);
        //We need to replace this due to Java not supporting declaring class field 'default'
        return toModify.replace("defaultOptionIndex", "default")
                .replace("defaultText", "default")
                .replace("defaultValue", "default")
                .replace("defaultStepIndex", "default");
    }

    public FormResponseCustom getResponse(){
        return response;
    }
    public void setResponse(String data) {
        if (data.equals("null")){
            this.closed = true;
            return;
        }
        String[] elementResponses = data.replace("[", "").replace("]",
                "").split(",");
        int i = 0;

        HashMap<Integer, FormResponseData> dropdownResponses = new HashMap<>();
        HashMap<Integer, String> inputResponses = new HashMap<>();
        HashMap<Integer, Float> sliderResponses = new HashMap<>();
        HashMap<Integer, FormResponseData> stepSliderResponses = new HashMap<>();
        HashMap<Integer, Boolean> toggleResponses = new HashMap<>();
        HashMap<Integer, Object> responses = new HashMap<>();

        for (String elementData : elementResponses){
            Element e = content.get(i);
            if (e == null) break;
            if (e instanceof ElementLabel) {
                i++;
                continue;
            }
            if (e instanceof ElementDropdown){
                String answer = ((ElementDropdown) e).options.get(Integer.parseInt(elementData));
                dropdownResponses.put(i, new FormResponseData(Integer.parseInt(elementData), answer));
                responses.put(i, answer);
            }
            else if (e instanceof ElementInput){
                String answer = elementData.substring(0, elementData.length()-1);
                inputResponses.put(i, answer);
                responses.put(i, answer);
            }
            else if (e instanceof ElementSlider){
                Float answer = Float.parseFloat(elementData);
                sliderResponses.put(i, answer);
                responses.put(i, answer);
            }
            else if (e instanceof ElementStepSlider){
                String answer = ((ElementStepSlider) e).steps.get(Integer.parseInt(elementData));
                stepSliderResponses.put(i, new FormResponseData(Integer.parseInt(elementData), answer));
                responses.put(i, answer);
            }
            else if (e instanceof ElementToggle){
                Boolean answer = Boolean.parseBoolean(elementData);
                toggleResponses.put(i, answer);
                responses.put(i, answer);
            }
            i++;
        }

        this.response = new FormResponseCustom(responses, dropdownResponses, inputResponses,
                sliderResponses, stepSliderResponses, toggleResponses);
    }

}
