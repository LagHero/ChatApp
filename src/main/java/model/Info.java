package model;

import java.io.Serializable;

public class Info implements Serializable {

    private static final long serialVersionUID = -1157357909876842942L;

    private String text;
    // TODO: Implement other types of messages, like picture, gif, etc.

    public Info() {
    }

    public Info(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
