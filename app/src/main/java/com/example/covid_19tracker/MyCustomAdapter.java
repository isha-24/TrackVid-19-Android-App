package com.example.covid_19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<CountryModel> {

    private Context context;
    private List<CountryModel> countryModelList;
    private List<CountryModel> countryModelFiltered;
    public MyCustomAdapter(@NonNull Context context, List<CountryModel> countryModelList) {
        super(context, R.layout.list_custom_item, countryModelList);
        this.context=context;
        this.countryModelList=countryModelList;
        this.countryModelFiltered=countryModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_custom_item,null,true);
        TextView tvCountryName=view.findViewById(R.id.tvCountryName);
        ImageView imageView=view.findViewById(R.id.imageFlag);

        tvCountryName.setText(countryModelFiltered.get(position).getCountry());
        Glide.with(context).load(countryModelFiltered.get(position).getFlag()).into(imageView);
        return view;
    }

    @Override
    public int getCount() {
        return countryModelFiltered.size();
    }

    @Nullable
    @Override
    public CountryModel getItem(int position) {
        return countryModelFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if(constraint==null||constraint.length()==0){
                    filterResults.count=countryModelList.size();
                    filterResults.values=countryModelList;
                }else{
                    List<CountryModel> resultsModel=new ArrayList<>();
                    String searchStr=constraint.toString().toLowerCase();

                    for(CountryModel itemsModel:countryModelList){
                        if(itemsModel.getCountry().toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);;
                        }
                        filterResults.count=resultsModel.size();
                        filterResults.values=resultsModel;
                    }
                }
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countryModelFiltered=(List<CountryModel>)results.values;
                AffectedCountries.countryModelList=(List<CountryModel>)results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
