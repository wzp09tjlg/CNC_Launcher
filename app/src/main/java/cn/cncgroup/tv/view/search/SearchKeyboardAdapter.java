package cn.cncgroup.tv.view.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemClickListener;

/**
 * Created by zhangbo on 15-9-15.
 */
public class SearchKeyboardAdapter extends RecyclerView.Adapter<SearchKeyboardAdapter.ViewHolder> {
    private ArrayList<SearchKeyItem> mList = new ArrayList<SearchKeyItem>();
    private OnItemClickListener<SearchKeyItem> mListener;

    public SearchKeyboardAdapter(OnItemClickListener<SearchKeyItem> listener) {
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("1"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("2").setLetter1("A").setLetter2("B").setLetter3("C"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("3").setLetter1("D").setLetter2("E").setLetter3("F"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("4").setLetter1("G").setLetter2("H").setLetter3("I"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("5").setLetter1("J").setLetter2("K").setLetter3("L"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("6").setLetter1("M").setLetter2("N").setLetter3("O"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("7").setLetter1("P").setLetter2("Q").setLetter3("R").setLetter4("S"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("8").setLetter1("T").setLetter2("U").setLetter3("V"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("9").setLetter1("W").setLetter2("X").setLetter3("Y").setLetter4("Z"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.DELETE).setIcon(R.drawable.icon_delete).setDesc("删除"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.INPUT).setNumber("0"));
        mList.add(new SearchKeyItem(SearchKeyItem.KeyType.CLEAR).setIcon(R.drawable.icon_clear).setDesc("清空"));
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_key, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SearchKeyItem item = getItem(position);
        if (SearchKeyItem.KeyType.INPUT == item.type) {
            holder.number.setText(item.number);
            holder.desc.setText(item.getLetters());
            holder.icon.setVisibility(View.GONE);
        } else {
            holder.icon.setImageResource(item.icon);
            holder.desc.setText(item.desc);
            holder.number.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClickLister(view, position, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public SearchKeyItem getItem(int position) {
        return mList.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView number;
        ImageView icon;
        TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.number);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            desc = (TextView) itemView.findViewById(R.id.desc);
        }
    }
}
