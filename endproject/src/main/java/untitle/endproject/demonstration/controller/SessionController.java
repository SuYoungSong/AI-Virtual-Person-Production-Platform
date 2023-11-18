package untitle.endproject.demonstration.controller;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import untitle.endproject.demonstration.domain.Member;
import java.util.UUID;

@Service
@SessionScope
public class SessionController {
    private Member member;
    private UUID UUID;
    private String temp;
    private String imageMessage;
    private String audioMessage;
    private String pose_num;
    private String pose_num_text;
    private String model_info;
    private String model_info_text;
    private String audio_model_info;
    private String audio_model_text;
    public String getPose_num_text() {
        return pose_num_text;
    }

    public void setPose_num_text(String pose_num_text) {
        this.pose_num_text = pose_num_text;
    }


    public String getModel_info_text() {
        return model_info_text;
    }

    public void setModel_info_text(String model_info_text) {
        this.model_info_text = model_info_text;
    }

    public String getAudio_model_text() {
        return audio_model_text;
    }

    public void setAudio_model_text(String audio_model_text) {
        this.audio_model_text = audio_model_text;
    }



    public String getAudio_model_info() {
        return audio_model_info;
    }

    public void setAudio_model_info(String audio_model_info) {
        this.audio_model_info = audio_model_info;
    }

    public String getPose_num() {
        return pose_num;
    }

    public void setPose_num(String pose_num) {
        this.pose_num = pose_num;
    }

    public String getModel_info() {
        return model_info;
    }

    public void setModel_info(String model_info) {
        this.model_info = model_info;
    }

    public String getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(String imageMessage) {
        this.imageMessage = imageMessage;
    }

    public String getAudioMessage() {
        return audioMessage;
    }

    public void setAudioMessage(String audioMessage) {
        this.audioMessage = audioMessage;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public UUID getUUID() {
        return UUID;
    }

    public void setUUID(UUID UUID) {
        this.UUID = UUID;
    }

    public void setMember(Member member){
        this.member = member;
    }

    public Member getMember() { return member; }
}
