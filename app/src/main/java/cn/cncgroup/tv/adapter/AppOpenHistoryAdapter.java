package cn.cncgroup.tv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.bean.AppInfo;
import cn.cncgroup.tv.utils.Utils;

/**
 * Created by Wuzhenpeng on 2015/5/7.
 */
public class AppOpenHistoryAdapter extends RecyclerView.Adapter<AppOpenHistoryAdapter.ViewHolder> {
    private ArrayList<AppInfo> mList = new ArrayList<AppInfo>();
    Context context;


    public AppOpenHistoryAdapter(Context context,ArrayList<AppInfo> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_appopen_history, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AppInfo item = mList.get(position);
        holder.icon.setBackground(item.icon);
        holder.name.setText(item.appName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //应用跳转
                Utils.startAppByPackageName(context,
                        item.getPackageName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_open_history);
            name = (TextView) itemView.findViewById(R.id.tv_open_history);
        }
    }

}
