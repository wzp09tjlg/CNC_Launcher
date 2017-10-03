package cn.cncgroup.tv.view.setting.adapter;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.CApplication;
import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;
import cn.cncgroup.tv.utils.GlobalConstant;
import cn.cncgroup.tv.utils.NetworkUtils;
import cn.cncgroup.tv.utils.Preferences;
import cn.cncgroup.tv.view.homesecond.utils.HomeTabManager;
import cn.cncgroup.tv.view.network.NetworkSettingActivity;
import cn.cncgroup.tv.view.setting.AutoShutdownActivity;
import cn.cncgroup.tv.view.setting.GeneralSettingsActivity;
import cn.cncgroup.tv.view.setting.SettingBootCustomActivity;
import cn.cncgroup.tv.view.setting.media.MediaSettingActivity;
import cn.cncgroup.tv.view.weather.WeatherCityActivity;

/**
 * Created by zhang on 2015/11/25.
 */
public class SettingHomeAdapter extends RecyclerView.Adapter<SettingHomeAdapter.ViewHolder> {
    private ArrayList<SettingItem> mList = getHomeSettingList();
    private OnItemClickListener<SettingItem> mClickListener;
    private OnItemFocusListener<SettingItem> mFocusListener;

    public SettingHomeAdapter(OnItemClickListener<SettingItem> clickListener, OnItemFocusListener<SettingItem> focusListener) {
        this.mClickListener = clickListener;
        this.mFocusListener = focusListener;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mClickListener.onItemClickLister(v, position, getItem(position));
        }
    };

    private View.OnFocusChangeListener mOnFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = ((RecyclerView.LayoutParams) v.getLayoutParams()).getViewAdapterPosition();
            mFocusListener.onItemFocusLister(v, position, getItem(position), hasFocus);
        }
    };

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_home, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SettingItem item = getItem(position);
        holder.name.setText(item.getName());
        holder.image.setImageResource(item.getImage());
        holder.desc.setText(item.getDesc());
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setOnFocusChangeListener(mOnFocusListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public SettingItem getItem(int position) {
        return mList.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
            desc = (TextView) itemView.findViewById(R.id.desc);
        }

        TextView name;
        ImageView image;
        TextView desc;
    }

    public abstract static class SettingItem {
        public abstract String getName();

        public abstract int getImage();

        public abstract String getDesc();

        public abstract Class<? extends Activity> getActivity();
    }

    public static class NetworSettingItem extends SettingItem {

        @Override
        public String getName() {
            return "网络设置";
        }

        @Override
        public int getImage() {
            return R.drawable.icon_setting_network;
        }

        @Override
        public String getDesc() {
            return NetworkUtils.isNetworkUp(CApplication.getInstance()) ? "已连接" : "未连接";
        }

        @Override
        public Class<? extends Activity> getActivity() {
            return NetworkSettingActivity.class;
        }
    }

    public static class GeneralSettingItem extends SettingItem {

        @Override
        public String getName() {
            return "通用设置";
        }

        @Override
        public int getImage() {
            return R.drawable.icon_setting_general;
        }

        @Override
        public String getDesc() {
            return "当前已是最新版本";
        }

        @Override
        public Class<? extends Activity> getActivity() {
            return GeneralSettingsActivity.class;
        }
    }

    public static class MediaSettingItem extends SettingItem {

        @Override
        public String getName() {
            return "显示与声音";
        }

        @Override
        public int getImage() {
            return R.drawable.icon_setting_media;
        }

        @Override
        public String getDesc() {
            return "鸠羽紫";
        }

        @Override
        public Class<? extends Activity> getActivity() {
            return MediaSettingActivity.class;
        }
    }

    public static class WeatherSettingItem extends SettingItem {

        @Override
        public String getName() {
            return "天气设置";
        }

        @Override
        public int getImage() {
            return R.drawable.icon_setting_weather;
        }

        @Override
        public String getDesc() {
            return Preferences.build(CApplication.getInstance()).getString(GlobalConstant.WEATHERCITY, "北京");
        }

        @Override
        public Class<? extends Activity> getActivity() {
            return WeatherCityActivity.class;
        }
    }

    public static class BootSettingItem extends SettingItem {

        @Override
        public String getName() {
            return "开机自定义";
        }

        @Override
        public int getImage() {
            return R.drawable.icon_setting_boot;
        }

        @Override
        public String getDesc() {
            return "默认频道：" + HomeTabManager.getInstance().getDefaultTabName();
        }

        @Override
        public Class<? extends Activity> getActivity() {
            return SettingBootCustomActivity.class;
        }
    }

    public static class ShutdownSettingItem extends SettingItem {

        @Override
        public String getName() {
            return "定时关机";
        }

        @Override
        public int getImage() {
            return R.drawable.icon_setting_shutup;
        }

        @Override
        public String getDesc() {
            return "23:45定时关闭";
        }

        @Override
        public Class<? extends Activity> getActivity() {
            return AutoShutdownActivity.class;
        }
    }

    static ArrayList<SettingItem> getHomeSettingList() {
        ArrayList<SettingItem> list = new ArrayList<SettingItem>();
        list.add(new NetworSettingItem());
        list.add(new GeneralSettingItem());
        list.add(new MediaSettingItem());
        list.add(new WeatherSettingItem());
        list.add(new BootSettingItem());
        list.add(new ShutdownSettingItem());
        return list;
    }
}
