package cn.nukkit.form.element;

public class ElementSlider extends Element {

    public String type = "slider";
    public String text = "";
    public float min = 0f;
    public float max = 100f;
    public float step;
    public float defaultValue;

    public ElementSlider (String text, float min, float max){
        this (text, min, max, -1f);
    }
    public ElementSlider (String text, float min, float max, float step){
        this (text, min, max, step, -1f);
    }
    public ElementSlider (String text, float min, float max, float step, float defaultValue){
        this.text = text;
        if (min < 0f) this.min = 0f;
        else this.min = min;
        if (max > min) this.max = this.min;
        else this.max = max;
        if (this.step != -1f && this.step > 0) this.step = step;
        if (defaultValue != -1f) this.defaultValue = defaultValue;
    }

}
