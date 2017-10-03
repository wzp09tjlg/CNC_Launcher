package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleFilmData;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhangbo on 15-6-19.
 */
public class VooleFilmDataTransRequest
        extends BaseTransRequest<VooleFilmData, VideoSetListData>
{
	private String url;
	private int pageSize;

	public VooleFilmDataTransRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult, Class<VooleFilmData> from,
	        Class<VideoSetListData> to, int pageSize)
	{
		super(url, tag, listener, isCacheResult, from, to);
		this.url = url;
		this.pageSize = pageSize;
	}

	@Override
	protected Object getTransStruct(VooleFilmData content)
	        throws CloneNotSupportedException
	{
		content.currentUrl = url;
		content.pageSize = pageSize + "";
		content.result = (VooleFilmData) content.clone();
		return content;
	}
}
