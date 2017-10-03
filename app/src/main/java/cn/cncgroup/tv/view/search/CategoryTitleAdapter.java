package cn.cncgroup.tv.view.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;
import cn.cncgroup.tv.ui.listener.OnItemFocusListener;

/**
 * Created by futuo on 2015/8/27.
 */
public class CategoryTitleAdapter
        extends RecyclerView.Adapter<CategoryTitleAdapter.ViewHolder>
{
    private OnItemFocusListener<SearchMenu> onItemFocusListener;
    private ArrayList<SearchMenu> mList;
    private RecyclerView.ViewHolder mLastViewHolder;

    public CategoryTitleAdapter(ArrayList<SearchMenu> List, OnItemFocusListener<SearchMenu> onItemFocusListener)
    {
        this.onItemFocusListener=onItemFocusListener;
        this.mList = List;
    }

    public RecyclerView.ViewHolder getLastViewHolder()
    {
        return mLastViewHolder;
    }

    public void setLastViewHolder(RecyclerView.ViewHolder holder)
    {
        this.mLastViewHolder = holder;
    }

    @Override
    public CategoryTitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_search_title, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryTitleAdapter.ViewHolder holder,
                                 final int position)
    {
        final SearchMenu menu = mList.get(position);
        holder.text.setText(menu.name);
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onItemFocusListener.onItemFocusLister(v,position,menu,hasFocus);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text;
        public ViewHolder(View itemView)
        {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    public static class SearchMenu
    {
        public String name;
        public String mtype;


        public SearchMenu(String name,@NonNull String mtype)
        {
            this.name = name;
            this.mtype = mtype;
        }

        @Override
        public String toString() {
            return "SearchMenu{" +
                    "name='" + name + '\'' +
                    ", mtype=" + mtype +
                    '}';
        }
    }

}
