package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleVideoListData;
import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by Administrator on 2015/11/2.
 */
public class VooleSearchRecommendRequest extends BaseTransRequest<VooleVideoListData, VideoListData> {
    public VooleSearchRecommendRequest(String url, Object tag, Listener listener, boolean isCacheResult, Class<VooleVideoListData> from, Class<VideoListData> to) {
        super(url, tag, listener, isCacheResult, from, to);
    }
}
