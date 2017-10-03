package cn.cncgroup.tv.conf.model.domybox.request;

import cn.cncgroup.tv.modle.VideoSetDetailData;
import cn.cncgroup.tv.conf.model.domybox.modle.DomyVideoSetDetailContent;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhangbo on 15-6-18.
 */
public class DomyVideoSetDetailTransRequest extends
        BaseTransRequest<DomyVideoSetDetailContent, VideoSetDetailData>
{

	public DomyVideoSetDetailTransRequest(String url, Object tag,
	        Listener listener, boolean isCacheResult,
	        Class<DomyVideoSetDetailContent> from, Class<VideoSetDetailData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(DomyVideoSetDetailContent content)
	{
		return content.data;
	}
}
