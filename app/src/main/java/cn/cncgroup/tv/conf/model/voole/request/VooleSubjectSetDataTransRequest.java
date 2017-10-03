package cn.cncgroup.tv.conf.model.voole.request;

import cn.cncgroup.tv.conf.model.voole.modle.VooleSubjectSetData;
import cn.cncgroup.tv.modle.SubjectSetData;
import cn.cncgroup.tv.network.request.BaseTransRequest;

/**
 * Created by zhang on 2015/10/21.
 */
public class VooleSubjectSetDataTransRequest
        extends BaseTransRequest<VooleSubjectSetData, SubjectSetData> {
    public VooleSubjectSetDataTransRequest(String url, Object tag, Listener listener, boolean isCacheResult,
                                           Class<VooleSubjectSetData> from, Class<SubjectSetData> to) {
        super(url, tag, listener, isCacheResult, from, to);
    }
}
