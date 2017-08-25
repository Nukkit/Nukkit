package cn.nukkit.form.window;

import com.google.gson.Gson;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseSimple;

import java.util.ArrayList;

public class FormWindowSimple extends FormWindow {

    public String type = "form";
    public String title = "";
    public String content = "";
    public ArrayList<ElementButton> buttons;

    private FormResponseSimple response = null;

    public FormWindowSimple(String title, String content){
        this (title, content, new ArrayList<>());
    }
    public FormWindowSimple(String title, String content, ArrayList<ElementButton> buttons){
        this.title = title;
        this.content = content;
        this.buttons = buttons;
    }

    public void addButton(ElementButton button){
        this.buttons.add(button);
    }

    public String getJSONData(){
        return new Gson().toJson(this);
    }

    public FormResponseSimple getResponse() {
        return response;
    }
    public void setResponse(String data) {
        if (data.equals("null")){
            this.closed = true;
            return;
        }
        int buttonID;
        try {
            buttonID = Integer.parseInt(data);
        }
        catch (Exception e) {
            return;
        }
        if (buttonID >= this.buttons.size()){
            this.response = new FormResponseSimple(buttonID, null);
            return;
        }
        this.response = new FormResponseSimple(buttonID, buttons.get(buttonID));
    }

}
