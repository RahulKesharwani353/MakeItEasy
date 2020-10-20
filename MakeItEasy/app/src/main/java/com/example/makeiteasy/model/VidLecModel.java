package com.example.makeiteasy.model;

public class VidLecModel
{
    private String vidLecTopic,vidLecDuration,vidLecYTLInk;

    public String getVidLecTopic() {
        return vidLecTopic;
    }

    public void setVidLecTopic(String vidLecTopic) {
        this.vidLecTopic = vidLecTopic;
    }

    public String getVidLecDuration() {
        return vidLecDuration;
    }

    public void setVidLecDuration(String vidLecDuration) {
        this.vidLecDuration = vidLecDuration;
    }
    public String getVidLecYTLInk() {
        return vidLecYTLInk;
    }

    public void setVidLecYTLInk(String vidLecYTLInk) {
        this.vidLecYTLInk = vidLecYTLInk;
    }

    public VidLecModel(String vidLecTopic, String vidLecDuration, String vidLecYTLInk) {
        this.vidLecTopic = vidLecTopic;
        this.vidLecDuration = vidLecDuration;
        this.vidLecYTLInk = vidLecYTLInk;
    }
}
