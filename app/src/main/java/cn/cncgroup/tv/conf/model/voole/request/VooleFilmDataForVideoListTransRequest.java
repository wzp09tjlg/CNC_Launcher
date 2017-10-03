package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleFilmDataForVideoList;
import cn.cncgroup.tv.modle.VideoSetListData;
import cn.cncgroup.tv.network.request.BaseRequest;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhang on 2015/10/26.
 */
public class VooleFilmDataForVideoListTransRequest
        extends BaseTransRequest<VooleFilmDataForVideoList, VideoSetListData>
{
	private String url;
	private int pageSize;

	public VooleFilmDataForVideoListTransRequest(String url, Object tag,
	        BaseRequest.Listener listener, boolean isCacheResult,
	        Class<VooleFilmDataForVideoList> from, Class<VideoSetListData> to,
	        int pageSize)
	{
		super(url, tag, listener, isCacheResult, from, to);
		this.url = url;
		this.pageSize = pageSize;
	}

	@Override
	protected Object getTransStruct(VooleFilmDataForVideoList content)
	        throws CloneNotSupportedException
	{
		if (content.allFilmCount == null && content.filmCount != null)
		{
			content.allFilmCount = content.filmCount;
		}
		content.currentUrl = url;
		content.pageSize = pageSize + "";
		content.result = (VooleFilmDataForVideoList) content.clone();
		return content;
	}
}
