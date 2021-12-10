package com.example.picturenotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class staggeredAdapter extends RecyclerView.Adapter<staggeredAdapter.customViewHolder>
{
    private List<Post> postItems;
    Context context;
    public staggeredAdapter(Context context,List<Post> postItems)
    {
        this.context = context;
        this.postItems = postItems;
    }


    @NonNull
    @Override
    public customViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new customViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.image_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull customViewHolder holder, int position) {
        holder.setPostItem(postItems.get(position));
    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class customViewHolder extends RecyclerView.ViewHolder
    {
        ImageView postItem;
        public customViewHolder(@NonNull View itemView) {
            super(itemView);
            postItem = itemView.findViewById(R.id.imagePart);

            postItem.setClickable(true);
            postItem.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context,showNote.class);
                    intent.putExtra("imageSrc",postItems.get(position).image);
                    intent.putExtra("Note",postItems.get(position).desc);
                    context.startActivity(intent);
                }
            });

            postItem.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setCancelable(true);
                    builder.setTitle("Delete");
                    builder.setMessage("Do you want to delete the selected item?");
                    builder.setPositiveButton("Delete",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int position = getAdapterPosition();
                            postDatabase database = new postDatabase(context);
                            database.delete(postItems.get(position).getImage());
                            Toast.makeText(context,"Deleted successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context,MainActivity.class);
                            context.startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();

                    return true;
                }
            });
        }
        void setPostItem(Post item)
        {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            Bitmap image = BitmapFactory.decodeFile(item.getImage(),bitmapOptions);
            postItem.setImageBitmap(image);
        }
    }

}
