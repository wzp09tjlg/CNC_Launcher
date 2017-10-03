package cn.cncgroup.tv.settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;

/**
 * Created by zhang on 2015/11/20.
 */
public class GeneralMenuAdapter extends RecyclerView.Adapter<GeneralMenuAdapter.ViewHolder> {
    private int mSelectPosition = 0;
    private ArrayList<GeneralMenu> mList;

    public GeneralMenuAdapter(ArrayList<GeneralMenu> list) {
        this.mList = list;
    }

    public void setSelectPosition(int position) {
        int temp = mSelectPosition;
        mSelectPosition = position;
        notifyItemChanged(temp);
        notifyItemChanged(mSelectPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_general_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GeneralMenu menu = getItem(position);
        holder.text.setText(menu.getName());
        if (position == mSelectPosition) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
    }

    public GeneralMenu getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        TextView text;
    }
}
