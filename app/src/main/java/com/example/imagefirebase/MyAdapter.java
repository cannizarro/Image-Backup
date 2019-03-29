package com.example.imagefirebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.IAccountAccessor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

// Below commented out code is same as the further below code but in the commented out code I have used AsyncTask
// to download the image whereas in the further below code I have used bumptech library Glide.
/*
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    // We could have used glide library to handle the downloading of image but this is also fine
    public class DownloadImg extends AsyncTask<String,Void,Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... urls) {

            URL url;
            Bitmap bmp;
            HttpURLConnection connection;

            try
            {
                url=new URL(urls[0]);
                connection=(HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                Bitmap myBitmap= BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private Uri[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title;
        public MyViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.image);
            title=v.findViewById(R.id.title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Uri> myDataset) {
        mDataset = myDataset.toArray(new Uri[0]);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Uri imageUri=mDataset[position];
        holder.title.setText(imageUri.getLastPathSegment());

        DownloadImg task =new DownloadImg();
        try {
            Bitmap downloaded = task.execute(imageUri.toString()).get();
            holder.imageView.setImageBitmap(downloaded);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}
*/


//Downlaoding images using Glide
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Uri[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title;
        public MyViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.image);
            title=v.findViewById(R.id.title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Uri> myDataset) {
        mDataset = myDataset.toArray(new Uri[0]);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Uri imageUri=mDataset[position];
        holder.title.setText(imageUri.getLastPathSegment());
        Glide.with(holder.imageView.getContext())
                .load(imageUri.toString())
                .into(holder.imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}