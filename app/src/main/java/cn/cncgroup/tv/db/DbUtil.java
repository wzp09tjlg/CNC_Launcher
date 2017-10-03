package cn.cncgroup.tv.db;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.Collections;
import java.util.List;

/**
 * Created by jinwenchao123 on 15/7/1.
 */
public class DbUtil {
    private static final String TAG = "DbUtil";

    /**
     * true代表是足迹
     * false代表是收藏
     * 数据按照保存时间savetime从大到小排序
     * <p/>
     * update数据的时候逻辑有问题
     *
     * @param isFootmark
     */

    public static List<VideoSetDao> queryAll(Boolean isFootmark) {
        List<VideoSetDao> data = null;
        if (isFootmark) {
            data = new Select().from(VideoSetDao.class).where("Footmark=?", 1).orderBy("SaveTime desc").execute();
        } else {
            data = new Select().from(VideoSetDao.class).where("Collect=?", 1).orderBy("SaveTime desc").execute();
        }
        return data;
    }


    public static VideoSetDao queryByChannelId(String VideoSetChannelId) {
        List<VideoSetDao> datas = new Select().from(VideoSetDao.class).where("ChannelId=?", VideoSetChannelId).orderBy("SaveTime desc").execute();
        VideoSetDao data;
        if (datas.size() > 0) {
            data = datas.get(0);
        } else {
            return null;
        }
        return data;
    }

    public static VideoSetDao queryById(String VideoSetId) {
        List<VideoSetDao> datas = new Select().from(VideoSetDao.class).where("VideoSetId=?", VideoSetId).execute();
        VideoSetDao data;
        if (datas.size() > 0) {
            data = datas.get(0);
        } else {
            return null;
        }
        return data;
    }

    /**
     * true代表是足迹
     * false代表是收藏
     * <p/>
     * 这个地方有一个bug,当收藏和历史都是一的时候，删除收藏就会将历史也删除
     * 这里需要添加一个判断，防止将另一部分的代码删除
     *
     * @param isFootmark
     */
    public static void deleteAll(Boolean isFootmark) {
        List<VideoSetDao> data = new Select().from(VideoSetDao.class).orderBy("SaveTime desc").execute();
        if (isFootmark) {
            for (int i = 0; i < data.size(); i++) {
                VideoSetDao videoSetDao = data.get(i);

                if (videoSetDao.footmark == 1) {
                    if (videoSetDao.collect == 1) {
                        videoSetDao.footmark = 0;//测试下看是不是需要保存。需要就加上保存方法
                        videoSetDao.save();
                    } else {
                        deleteById(videoSetDao);
                    }
                }
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                VideoSetDao videoSetDao = data.get(i);
                if (videoSetDao.collect == 1) {
                    if (videoSetDao.footmark == 1) {
                        videoSetDao.collect = 0;//测试下看是不是需要保存。需要就加上保存方法
                        videoSetDao.save();
                    } else {
                        deleteById(videoSetDao);
                    }
                }
            }
        }
    }

    public static void deleteById(VideoSetDao videoSetDao) {
        new Delete().from(VideoSetDao.class).where("Id=?", videoSetDao.getId()).execute();
    }

    /**
     * 该方法覆盖了数据库中的add和update方法，传入一个实体
     * <p/>
     * 如果是添加收藏保存历史状态，要是添加历史保存收藏状态
     *
     * @param videoSetDao
     */
    public static void addByObject(VideoSetDao videoSetDao, boolean isFootmark) {
        List<VideoSetDao> foundVideo = new Select().from(VideoSetDao.class).where("VideoSetId=?", videoSetDao.videoSetId).execute();
        int footMark = 0;
        int collect = 0;
        if (foundVideo.size() > 0) {
            footMark = foundVideo.get(0).footmark;
            collect = foundVideo.get(0).collect;
            foundVideo.get(0).delete();
        }
        if (isFootmark) {
            videoSetDao.collect = collect;
        } else {
            videoSetDao.footmark = footMark;
        }
        videoSetDao.saveTime = System.currentTimeMillis();
        videoSetDao.save();
    }


    //这里的update的时候也会有问题
    public static void addCollect(String videoSetid, String image, String name) {
        VideoSetDao videoSetDao = new VideoSetDao();
        List<VideoSetDao> videoSetDaoList = new Select().from(VideoSetDao.class).where("VideoSetId=?", videoSetid).execute();
        if (videoSetDaoList.size() > 0) {
            videoSetDao = videoSetDaoList.get(0);
        }
        videoSetDao.image = image;
        videoSetDao.name = name;
        videoSetDao.collect = 1;
        videoSetDao.videoSetId = videoSetid;
        videoSetDao.saveTime = System.currentTimeMillis();
        videoSetDao.save();
    }

    /**
     * 电视剧还没有播放完，后台数据在不断更新的部分，是需要给用户提示的
     */
    public static void addCollect(String videoSetid, String image, String name, int count, int total) {
        VideoSetDao videoSetDao = new VideoSetDao();
        List<VideoSetDao> videoSetDaoList = new Select().from(VideoSetDao.class).where("VideoSetId=?", videoSetid).execute();
        if (videoSetDaoList.size() > 0) {
            videoSetDao = videoSetDaoList.get(0);
        }
        videoSetDao.image = image;
        videoSetDao.name = name;
        videoSetDao.collect = 1;
        videoSetDao.videoSetId = videoSetid;
        videoSetDao.seriesType = 1;
        videoSetDao.count = count;
        videoSetDao.total = total;
        videoSetDao.saveTime = System.currentTimeMillis();
        videoSetDao.save();
    }

    public static void deleteCollect(String videoSetid) {
        VideoSetDao videoSetDao;
        List<VideoSetDao> videoSetDaoList = new Select().from(VideoSetDao.class).where("VideoSetId=?", videoSetid).execute();
        if (videoSetDaoList.size() > 0) {
            videoSetDao = videoSetDaoList.get(0);
            if (videoSetDao.footmark == 1) {
                videoSetDao.collect = 0;
                videoSetDao.save();
            } else {
                videoSetDao.delete();
            }

        }

    }

    public static void deleteFootmark(String videoSetid) {
        VideoSetDao videoSetDao;
        List<VideoSetDao> videoSetDaoList = new Select().from(VideoSetDao.class).where("VideoSetId=?", videoSetid).execute();
        if (videoSetDaoList.size() > 0) {
            videoSetDao = videoSetDaoList.get(0);
            if (videoSetDao.collect == 1) {
                videoSetDao.footmark = 0;
                videoSetDao.save();
            } else {
                videoSetDao.delete();
            }

        }

    }

}
