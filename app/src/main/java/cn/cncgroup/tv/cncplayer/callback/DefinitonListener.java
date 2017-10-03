package cn.cncgroup.tv.cncplayer.callback;

import android.content.Context;
import android.util.SparseArray;

import java.util.ArrayList;

import cn.cncgroup.tv.cncplayer.bean.DefitionBean;
import cn.cncgroup.tv.player.data.Definition;

/**
 * Created by Administrator on 2015/9/24.
 */
public interface DefinitonListener {
    ArrayList<Definition> getDefitionList(Context context,SparseArray<String> result);

    ArrayList<DefitionBean> getDefitionListBean(Context context,SparseArray<String> result);

    Definition getMostCloseDefinition(ArrayList<Definition> mDefinitonList);
}
