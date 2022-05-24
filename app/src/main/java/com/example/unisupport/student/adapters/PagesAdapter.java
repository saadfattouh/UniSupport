package com.example.unisupport.student.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unisupport.R;
import com.example.unisupport.model.Page;
import com.example.unisupport.model.Post;
import com.example.unisupport.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class PagesAdapter extends RecyclerView.Adapter<PagesAdapter.ViewHolder> implements Filterable {


    Context context;
    private List<Page> pages;
    private List<Page> itemsFiltered;

    SharedPrefManager prefManager;
    int myId;

    NavController navController;

    // RecyclerView recyclerView;
    public PagesAdapter(Context context, ArrayList<Page> pages) {
        this.context = context;
        this.pages = pages;
        this.itemsFiltered = pages;
        prefManager = SharedPrefManager.getInstance(context);
        myId = prefManager.getUserId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_page, parent, false);

        return new ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Page page = pages.get(position);

        holder.date.setText(page.getDate());

        holder.content.setText(page.getContent());

        holder.page_url.setText(page.getUrl());

        holder.adminName.setText(page.getAdmin_name());

//        holder.itemView.setOnClickListener(v -> {
//            navController = Navigation.findNavController(holder.itemView);
//            navController.navigate(R.id.action_student_community_to_pageDetailsFragment);
//        });

    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = itemsFiltered.size();
                    filterResults.values = itemsFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();
                    ArrayList<Page> resultData = new ArrayList<>();


                    for(Page page: itemsFiltered){
                        if(page.getContent().toLowerCase().contains(searchChr)){
                            resultData.add(page);
                        }
                    }


                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pages = (ArrayList<Page>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }




    @Override
    public int getItemCount() {
        return pages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView adminName, date, page_url, content;

        public ViewHolder(View itemView) {
            super(itemView);
            this.adminName = itemView.findViewById(R.id.admin);
            this.date = itemView.findViewById(R.id.date);
            this.page_url = itemView.findViewById(R.id.page_url);
            this.content = itemView.findViewById(R.id.content);

        }
    }


}