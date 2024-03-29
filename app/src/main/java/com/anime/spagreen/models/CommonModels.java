package com.anime.spagreen.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class CommonModels {

    public String id,episodeName;
    public int image;
    public String imageUrl;
    public Drawable imageDrw;
    public String title;
    public String brief;
    public String stremURL;
    public String videoType;
    public String serverType;
    public List<EpiModel> listEpi=new ArrayList<>();
    String aired,runTime,status,type,episodes;
    //-----tv item------------
    String tvName,posterUrl;
    //ArrayList<EpiModel> epiModels;


    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEpisodes() {
        return episodes;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getAired() {
        return aired;
    }

    public void setAired(String aired) {
        this.aired = aired;
    }

    public List<EpiModel> getListEpi() {
        return listEpi;
    }

    public void setListEpi(List<EpiModel> listEpi) {
        this.listEpi = listEpi;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getStremURL() {
        return stremURL;
    }

    public void setStremURL(String stremURL) {
        this.stremURL = stremURL;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Drawable getImageDrw() {
        return imageDrw;
    }

    public void setImageDrw(Drawable imageDrw) {
        this.imageDrw = imageDrw;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}
