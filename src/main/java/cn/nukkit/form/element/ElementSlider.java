package cn.nukkit.form.element;

public class ElementSlider extends Element {

    public String type = "slider";
    public String text = "";
    public float min = 0f;
    public float max = 100f;
    public int step;
    public float defaultValue;

    public ElementSlider(String text, float min, float max) {
        this(text, min, max, -1);
    }

    public ElementSlider(String text, float min, float max, int step) {
        this(text, min, max, step, -1);
    }

    public ElementSlider(String text, float min, float max, int step, float defaultValue) {
        this.text = text;
        this.min = min < 0f ? 0f : min;
        this.max = max > this.min ? max : this.min;
        if (step != -1f && step > 0) this.step = step;
        if (defaultValue != -1f) this.defaultValue = defaultValue;
    }

}
