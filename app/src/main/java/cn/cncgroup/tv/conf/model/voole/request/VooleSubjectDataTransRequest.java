package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleSubjectData;
import cn.cncgroup.tv.modle.SubjectData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhang on 2015/10/21.
 */
public class VooleSubjectDataTransRequest
        extends BaseTransRequest<VooleSubjectData, SubjectData>
{
	public VooleSubjectDataTransRequest(String url, Object tag,
	        Listener listener, boolean isCacheResult,
	        Class<VooleSubjectData> from, Class<SubjectData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}
}
