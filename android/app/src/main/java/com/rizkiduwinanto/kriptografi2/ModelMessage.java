package com.rizkiduwinanto.kriptografi2;

public class ModelMessage {
    private String circleText;
    private String headText;
    private String subText;
    private String desText;
    private String dateText;

    public ModelMessage(String circleText, String headText, String subText, String desText, String dateText) {
        this.circleText = circleText;
        this.headText = headText;
        this.subText = subText;
        this.desText = desText;
        this.dateText = dateText;
    }

    public String getCircleText() {
        return circleText;
    }

    public void setCircleText(String circleText) {
        this.circleText = circleText;
    }

    public String getHeadText() {
        return headText;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getDesText() {
        return desText;
    }

    public void setDesText(String desText) {
        this.desText = desText;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }
}
