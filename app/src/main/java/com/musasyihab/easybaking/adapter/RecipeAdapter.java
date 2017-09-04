package com.musasyihab.easybaking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.musasyihab.easybaking.R;
import com.musasyihab.easybaking.model.RecipeModel;

import java.util.List;

/**
 * Created by musasyihab on 9/1/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private List<RecipeModel> recipeList;
    private final RecipeAdapterClickListener mOnClickListener;
    private Context context;

    public interface RecipeAdapterClickListener {
        void onRecipeClick(RecipeModel recipeClicked);
    }

    public RecipeAdapter (Context context, List<RecipeModel> recipeList, RecipeAdapterClickListener listener) {
        this.context = context;
        this.recipeList = recipeList;
        mOnClickListener = listener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: ");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(recipeList.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView recipeImage;
        TextView recipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_item_image);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_item_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnClickListener.onRecipeClick(recipeList.get(clickedPosition));
                }
            });
        }

        void bind(RecipeModel recipe) {
            if(recipe!=null) {
                Glide.with(context).load(recipe.getImage())
                        .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_restaurant_menu_green)
                        .into(recipeImage);
                recipeName.setText(recipe.getName());
            }
        }
    }
}
