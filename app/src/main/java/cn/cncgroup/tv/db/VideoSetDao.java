package cn.cncgroup.tv.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * id字段是默认就有的，自增类型
 * videoSetId 唯一的id
 * footmark 是否在足迹中有保存该数据0为无，1为有
 * collect 是否在收藏中有保存该数据0为无，1为有
 * image 图片的链接
 * name 视频的名字
 */
@Table(name = "videosets")
public class VideoSetDao extends Model {


//    public VideoSetDao(int videoSetId,int footmark,int collect,String image,String name){
//        this.videoSetId = videoSetId;
//        this.footmark = footmark;
//        this.collect = collect;
//        this.image = image;
//
//    }

    /**
     * 播放过，0没有播放过，1播放过
     */
    @Column(name = "Footmark")
    public int footmark;
    /**
     * 收藏，0没有收藏，1已收藏
     */
    @Column(name = "Collect")
    public int collect;
    /**
     * 播放到了哪个时间段
     */
    @Column(name = "StartTime")
    public int startTime;
    /**
     * 唯一的视频ID
     */
    @Column(name = "VideoSetId")
    public String videoSetId;

    /**
     * 唯一的视频ID
     */
    @Column(name = "VideoId")
    public String videoId;
    /**
     *
     */
    @Column(name = "ChannelId")
    public String channelId;

    @Column(name = "ChannelName")
    public String channelName;
    /**
     * 影视id
     */
    @Column(name = "EpisodeId")
    public String episodeId;
    /**
     * 影视名称
     */
    @Column(name = "Name")
    public String name;
    /**
     * 简介
     */
    @Column(name = "Desc")
    public String desc;
//    /**
//     * 导演
//     */
//    @Column(name = "Directors")
//    public String directors;
//    /**
//     * 演员
//     */
//    @Column(name = "Actors")
//    public String actors;
    /**
     * 标签
     */
    @Column(name = "Tags")
    public String tags;
    /**
     * 影视图片
     */
    @Column(name = "Image")
    public String image;
    /**
     * 影视追剧集数
     */
    @Column(name = "Count")
    public int count;
//    /**
//     * 影视发行时间
//     */
//    @Column(name = "Time")
//    public String time;
    /**
     * 评分
     */
    @Column(name = "Score")
    public float score;
    /**
     * 时长
     */
    @Column(name = "Length")
    public int length;
    /**
     * 一句话看点
     */
    @Column(name = "Focus")
    public String focus;
    /**
     * 0:单集,1:多集
     */
    @Column(name = "SeriesType")
    public int seriesType;

    @Column(name = "Total")
    public int total;

    @Column(name = "playOrder")
    public int playOrder;


    @Column(name = "SaveTime")
    public long saveTime;


}
