package cn.cncgroup.tv.view.setting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;

/**
 * Created by zhangtao on 2015/11/24.
 */
public class FacilityInformationAddressAdapter extends RecyclerView.Adapter<FacilityInformationAddressAdapter.ViewHolder> {
    ArrayList<AddressNameModel> mAddressNameModel ;
    Context context;
    private int mSelectedPosition = -1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facility_information_adress, parent, false));
    }

    public FacilityInformationAddressAdapter(Context context,
                                             ArrayList<AddressNameModel> mAddressNameModel) {
        this.context = context;
        this.mAddressNameModel = mAddressNameModel;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AddressNameModel addressModel = mAddressNameModel.get(position);
        Log.i("addressModel","addressModel="+addressModel);
        holder.mAddressname.setText(addressModel.getMAddressname());
        holder.itemView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddressNameModel.size();
    }

    public static class AddressNameModel {
        public String mAddressname;

        public AddressNameModel(String mAddressname) {
            this.mAddressname = mAddressname;

        }

        public AddressNameModel setMAddressname(String mAddressname) {
            this.mAddressname = mAddressname;
            return this;
        }

        public String getMAddressname() {
            return mAddressname;
        }
    }

    public void setList(ArrayList<AddressNameModel> mAddressNameModel) {
        this.mAddressNameModel = mAddressNameModel;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            mAddressname = (TextView) itemView.findViewById(R.id.address_tv);
        }

        TextView mAddressname;
    }
}
