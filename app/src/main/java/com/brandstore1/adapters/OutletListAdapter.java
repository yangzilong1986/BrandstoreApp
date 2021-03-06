package com.brandstore1.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandstore1.R;
import com.brandstore1.entities.Outlet;
import com.brandstore1.entities.OutletListFilterConstraint;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Ravi on 29-Mar-15.
 */

public class OutletListAdapter extends BaseAdapter implements Filterable {
    ArrayList<Outlet> mOutletList;
    ArrayList<Outlet> origOutletList;
    private LayoutInflater inflater;
    private Filter filter;
    private Filter saleFilter;
    Toolbar toolbar;
    TextView emptyView;
    OutletListFilterConstraint outletListFilterConstraint;

    public OutletListAdapter(ArrayList<Outlet> outlet, Activity context, Toolbar toolbar, TextView emptyView) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOutletList = outlet;
        origOutletList = new ArrayList<Outlet>();
        this.toolbar = toolbar;
        this.emptyView =  emptyView;
    }

    public void setOutletListFilterConstraint(OutletListFilterConstraint outletListFilterConstraint){
        this.outletListFilterConstraint = outletListFilterConstraint;
    }

    @Override
    public int getCount() {
        return mOutletList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 mHolder;

        if (convertView == null) {
            mHolder = new ViewHolder1();
            convertView = inflater.inflate(R.layout.outlet_list_list_view_item, null);
            mHolder.brandNameTextView = (TextView) convertView.findViewById(R.id.outletname);
            mHolder.floorAndHubNameTextView = (TextView) convertView.findViewById(R.id.floorAndHubName);
            mHolder.tagTextView = (TextView) convertView.findViewById(R.id.tag_label);
            mHolder.priceTextView= (TextView) convertView.findViewById(R.id.price_label);
            mHolder.image = (ImageView) convertView.findViewById(R.id.outlet_image);
            mHolder.male = (ImageView) convertView.findViewById(R.id.first);
            mHolder.female = (ImageView) convertView.findViewById(R.id.second);
            mHolder.kids = (ImageView) convertView.findViewById(R.id.third);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder1) convertView.getTag();
        }

        mHolder.male.setVisibility(View.INVISIBLE);
        mHolder.female.setVisibility(View.INVISIBLE);
        mHolder.kids.setVisibility(View.INVISIBLE);
        if (mOutletList.get(position).getGenderCodeString().contains("M"))
            mHolder.male.setVisibility(View.VISIBLE);

        if (mOutletList.get(position).getGenderCodeString().contains("F"))
            mHolder.female.setVisibility(View.VISIBLE);

        if (mOutletList.get(position).getGenderCodeString().contains("K"))
            mHolder.kids.setVisibility(View.VISIBLE);


        mHolder.brandNameTextView.setText(mOutletList.get(position).getBrandOutletName());
        mHolder.floorAndHubNameTextView.setText(mOutletList.get(position).getFloorNumber() + ", " + mOutletList.get(position).getMallName());
        mHolder.tagTextView.setText(mOutletList.get(position).getRelevantTag());
        mHolder.priceTextView.setText(" : ₹ " + mOutletList.get(position).getPrice());

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 1;
        mHolder.tagTextView.setLayoutParams(p);

        ImageLoader.getInstance().displayImage(mOutletList.get(position).getImageUrl(), mHolder.image);

        return convertView;
    }


    static class ViewHolder1 {

        TextView brandNameTextView;
        TextView floorAndHubNameTextView;

        TextView tagTextView;
        TextView priceTextView;

        ImageView image;
        ImageView male;
        ImageView female;
        ImageView kids;
    }

//    public void resetData() {
//        mOutletList = origOutletList;
//    }

    @Override
    public Filter getFilter(){
        if (filter == null){
            filter  = new OutletFilter();
        }
        return filter;
    }


    private class OutletFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence filter) {

            FilterResults result = new FilterResults();
            ArrayList<Outlet> list = new ArrayList<Outlet>();
            for (int i = 0, l = mOutletList.size(); i < l; i++) {
                Outlet m = mOutletList.get(i);

                if (outletListFilterConstraint.satisfiesConstraint(m))
                    list.add(m);
            }

            if(list!=null && list.size()>0 && !outletListFilterConstraint.isSORT_BY_RELEVANCE()){
                list = outletListFilterConstraint.getSortedOutletArrayList(list);
            }

            result.values = list;
            result.count = list.size();
            return result;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence filter, FilterResults results) {

            //mOutletList = (ArrayList<Outlet>) results.values;
            setOutletListFrom((ArrayList<Outlet>) results.values);
            toolbar.setSubtitle(getCount() + " " + "Outlets");
            notifyDataSetChanged();
            if (results.count == 0)
                emptyView.setText("No outlets found");
        }
    }


    public void setOutletListFrom(ArrayList<Outlet> theOrigOutletList){
        int count = theOrigOutletList.size();
        mOutletList.clear();
        for (int i = 0; i < count; i++) {
            mOutletList.add(theOrigOutletList.get(i));
        }
        notifyDataSetChanged();
    }

    public void setOutletListFromOriginal(){
        int count = origOutletList.size();
        mOutletList.clear();
        for (int i = 0; i < count; i++) {
            mOutletList.add(origOutletList.get(i));
        }
        notifyDataSetChanged();
    }


    public void setOrigOutletListFrom(ArrayList<Outlet> theOutletList){
        int count = theOutletList.size();
        origOutletList.clear();
        for (int i = 0; i < count; i++) {
            origOutletList.add(theOutletList.get(i));
        }
    }

}
