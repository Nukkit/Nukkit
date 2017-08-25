package cn.nukkit.form.window;

import com.google.gson.Gson;
import cn.nukkit.form.response.FormResponseModal;

public class FormWindowModal extends FormWindow {

    private String type = "modal";
    public String title = "";
    public String content = "";
    public String button1 = "";
    public String button2 = "";

    private FormResponseModal response = null;

    public FormWindowModal(String title, String content, String trueButonText, String falseButtonText){
        this.title = title;
        this.content = content;
        this.button1 = trueButonText;
        this.button2 = falseButtonText;
    }

    public String getJSONData(){
        return new Gson().toJson(this);
    }

    public FormResponseModal getResponse() {
        return response;
    }
    public void setResponse(String data){
        if (data.equals("null")){
            closed = true;
            return;
        }
        if (data.equals("true")) response = new FormResponseModal(0, button1);
        else response = new FormResponseModal(1, button2);
    }

}
