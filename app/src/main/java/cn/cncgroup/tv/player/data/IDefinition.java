package cn.cncgroup.tv.player.data;

import android.content.Context;

/**
 * Created by Administrator on 2015/9/24.
 */
public interface IDefinition {
    boolean isDolby();

    boolean isH265();

    boolean is4K();

    String getName(Context context);

    int getValue();

    int getSettingWeight();

    String getDefinitionString();
}
