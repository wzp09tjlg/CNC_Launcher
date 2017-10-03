package cn.cncgroup.tv.conf.model.domybox.request;

import cn.cncgroup.tv.conf.model.domybox.modle.DomyVideoSetListContent;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhangbo on 15-6-19.
 */
public class DomyVideoSetListTransRequest
        extends BaseTransRequest<DomyVideoSetListContent, VideoSetListData>
{
	private String url;

	public DomyVideoSetListTransRequest(String url, Object tag,
	        Listener listener, boolean isCacheResult,
	        Class<DomyVideoSetListContent> from, Class<VideoSetListData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
		this.url = url;
	}

	@Override
	protected Object getTransStruct(DomyVideoSetListContent content)
	{
		content.data.currentUrl = url;
		return content.data;
	}
}
