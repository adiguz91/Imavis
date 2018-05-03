package com.drone.imavis.mvp.util.swipelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adigu on 08.05.2017.
 */

public class SwipeItemListViewAdapter<T> extends BaseSwipeAdapter {

    int swipeItemList;
    int swipeLayout;
    int deleteRessource;
    int editRessource;
    //@Inject Context mContext;
    private Context context;
    private List<T> itemList;
    private SwipeItemOnClickListener<T> onItemClickListener;

    //@BindView(R.id.textViewProjectListViewItemProjectname) TextView textViewProjectname;
    //@BindView(R.id.textViewProjectListViewItemDescription) TextView textViewDescription;

    //@Inject
    public SwipeItemListViewAdapter(Context context, SwipeItemOnClickListener<T> onItemClickListener, int swipeItemList,
                                    int swipeLayout, int editRessource, int deleteRessource) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.itemList = new ArrayList<>();

        this.swipeItemList = swipeItemList;
        this.swipeLayout = swipeLayout;

        this.editRessource = editRessource;
        this.deleteRessource = deleteRessource;
    }

    public void setItems(List<T> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return swipeItemList;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(swipeLayout, null);
        SwipeLayout swipeLayout = view.findViewById(getSwipeLayoutResourceId(position));
        //swipeLayout.addSwipeListener(new SimpleSwipeListener() {
        //  @Override
        //  public void onOpen(SwipeLayout layout) {
        //        YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(deleteRessource));
        //    }
        //});
        /*
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        */
        swipeLayout.findViewById(deleteRessource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onCallback(view, SwipeActionButtons.Delete, position, getItem(position));
            }
        });
        swipeLayout.findViewById(editRessource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onCallback(view, SwipeActionButtons.Edit, position, getItem(position));
            }
        });
        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public T getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(T item) {
        if (item != null) {
            itemList.add(item);
            this.notifyDataSetChanged();
        }
    }

    public void updateItem(int position, T item) {
        if (item != null) {
            itemList.set(position, item);
            this.notifyDataSetChanged();
        }
    }

    public void deleteItem(T item) {
        if (item != null) {
            itemList.remove(item);
            this.notifyDataSetChanged();
        }
    }


}