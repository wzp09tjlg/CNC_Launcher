package cn.cncgroup.tv.conf.model.domybox.request;

import cn.cncgroup.tv.modle.VideoListData;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyVideoListContent;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhangbo on 15-6-18.
 */
public class DomyVideoListTransRequest
        extends BaseTransRequest<DomyVideoListContent, VideoListData>
{
	public DomyVideoListTransRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult, Class<DomyVideoListContent> from,
	        Class<VideoListData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(DomyVideoListContent content)
	{
		return content.data;
	}
}