package com.example.doanmobile.dangsanpham;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
    private Context context;
    private List<Category> categoryList;


    private OnCategoryClickListener onCategoryClickListener;

    public CategoryAdapter (Context context, List<Category> categoryList)
    {
        this.context = context;
        this.categoryList = categoryList;
    }
    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryid,parent,false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {

        Category category = categoryList.get(position);
        holder.tentheloaiidnha = holder.itemView.findViewById(R.id.tentheloaiidnha);
        holder.tentheloaiidnha.setText(category.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.viewtheloai.setBackgroundResource(R.drawable.item_theloaisanpham);
                if (onCategoryClickListener != null) {
                    onCategoryClickListener.onCategoryClick(category);
                }
            }
        });



    }

    @Override
    public int getItemCount() {

        return categoryList.size();
    }
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }
    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.onCategoryClickListener = listener;
    }

}
class CategoryHolder extends RecyclerView.ViewHolder {
    TextView  tentheloaiidnha;
    View viewtheloai;
    public  CategoryHolder(@NonNull View itemView)
    {

        super(itemView);
        tentheloaiidnha = itemView.findViewById(R.id.tentheloaiidnha);
        viewtheloai = itemView.findViewById(R.id.viewtheloai);
    }
    public void setOnCategoryClickListener(CategoryAdapter.OnCategoryClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCategoryClick(new Category());
            }
        });
    }
}
