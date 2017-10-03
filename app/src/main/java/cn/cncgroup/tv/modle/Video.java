package cn.cncgroup.tv.modle;

import android.text.TextUtils;

import java.io.Serializable;

import cn.cncgroup.tv.conf.Project;

/**
 * Created by zhangbo on 15-6-18.
 */
public class Video implements Serializable {
    public String channelId;
    public String channelName;
    public String desc;
    public String directors;
    /**
     * 关注点
     */
    public String focus;
    /**
     * 时长
     */
    public int length;
    public String name;
    public int index;
    public String actors;
    /**
     * 剧集图片地址
     */
    public String image;
    public String id;
    public String videoSetId;
    public String year;
    /**
     * 正片-片花区分标识
     */
    public int type;
    /**
     * 清晰度信息
     */
    public String epvid;
    /**
     *
     */
    private String showTime;

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    /**
     * 标记视频的播放记录
     */
    public int startTime;

    public String getVerticalPosterUrl() {
        if (TextUtils.isEmpty(image)) {
            return "";
        }
        int pointIndex = image.lastIndexOf(".");
        return image.substring(0, pointIndex) + "_320_180"
                + image.substring(pointIndex);
    }

    public String getHorizentalPosterUrl() {
        if (TextUtils.isEmpty(image)) {
            return "";
        }
        int pointIndex = image.lastIndexOf(".");
        return image.substring(0, pointIndex) + "_260_360"
                + image.substring(pointIndex);
    }

    /**
     * @param obj obj
     * @return true:对象的值相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (!(obj instanceof Video)) {
            return false;
        }
        Video video = (Video) obj;
        if (video.channelId.equals(channelId) && video.id.equals(id)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[focuse:" + focus + ",id:" + id + ",eqID:" + epvid + ",name:"
                + name + "]";
    }

    public String getChannelId() {
        return channelId;
    }

    public String getName() {
        return name;
    }

    public String getFocus() {
        return focus;
    }

    public String getID() {
        return id;
    }

    public String getEpvid() {
        return epvid;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoSetId() {
        return videoSetId;
    }

    public void setVideoSetId(String videoSetId) {
        this.videoSetId = videoSetId;
    }

    public String getYear() {
        return Project.get().getConfig().getVarietyYear(this);
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setEpvid(String epvid) {
        this.epvid = epvid;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}