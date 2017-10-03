package cn.cncgroup.tv.view.setting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cncgroup.tv.R;

/**
 * Created by zhangtao on 2015/11/23.
 */
public class FacilityInformationAdapter extends RecyclerView.Adapter<FacilityInformationAdapter.ViewHolder> {
    private static final String TAG = "FacilityInformationAdapter";
    public ArrayList<ModelNumberModel> mModelList ;
    Context context;

    public FacilityInformationAdapter(Context context, ArrayList<ModelNumberModel> modelList) {
        this.context = context;
        this.mModelList = modelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_facility_information, parent, false));
    }
    @Override
    public void onBindViewHolder( ViewHolder holder,  int position) {
        ModelNumberModel modelModel = mModelList.get(position);
        holder.mModel_textview.setText(modelModel.mModel);
        holder.mModel_number_textview.setText(modelModel.mModelNumber);
    }
    @Override
    public int getItemCount() {
        return mModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mModel_textview;
        public TextView mModel_number_textview;

        public ViewHolder(View itemView) {
            super(itemView);
            mModel_textview = (TextView) itemView.findViewById(R.id.model_textview);
            mModel_number_textview = (TextView) itemView.findViewById(R.id.model_number_textview);
        }
    }

    public static class ModelNumberModel {
        public String mModelNumber;
        public String mModel;

        public ModelNumberModel(String mModel, String mModelNumber) {
            this.mModelNumber = mModelNumber;
            this.mModel = mModel;
        }

        @Override
        public String toString() {
            return "ModelNumberModel{" +
                    "mModelNumber='" + mModelNumber + '\'' +
                    ", mModel='" + mModel + '\'' +
                    '}';
        }
    }
}
