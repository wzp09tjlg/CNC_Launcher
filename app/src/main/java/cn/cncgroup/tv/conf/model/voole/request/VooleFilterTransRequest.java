package cn.cncgroup.tv.conf.model.voole.request;

import java.util.ArrayList;

import cn.cncgroup.tv.conf.model.voole.modle.VooleFilter;
import cn.cncgroup.tv.conf.model.voole.modle.VooleFilterData;
import cn.cncgroup.tv.modle.FilterListData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhang on 2015/10/10.
 */
public class VooleFilterTransRequest
        extends BaseTransRequest<VooleFilterData, FilterListData>
{
	public VooleFilterTransRequest(String url, Object tag, Listener listener,
	        boolean isCacheResult, Class<VooleFilterData> from,
	        Class<FilterListData> to)
	{
		super(url, tag, listener, isCacheResult, from, to);
	}

	@Override
	protected Object getTransStruct(VooleFilterData vooleFilterData)
	        throws CloneNotSupportedException
	{
		for (VooleFilter filter : vooleFilterData.label)
		{
			if (filter.lableDesc.equals("genre"))
			{
				filter.lableDesc = "剧情";
			}
			else if (filter.lableDesc.equals("country"))
			{
				filter.lableDesc = "地区";
			}
			else if (filter.lableDesc.equals("years"))
			{
				filter.lableDesc = "时间";
			}
			filter.result = new ArrayList<VooleFilter>();
			VooleFilter f = new VooleFilter();
			f.lableDesc = "全部";
			f.upName = filter.lableDesc;
			filter.result.add(f);
			for (String name : filter.labelName)
			{
				VooleFilter v = new VooleFilter();
				v.lableDesc = name;
				v.upName = filter.lableDesc;
				filter.result.add(v);
			}
		}
		return vooleFilterData;
	}
}
