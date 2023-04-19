package com.group_1.usege.layout.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.group_1.usege.R;
import com.group_1.usege.model.Image;
import com.group_1.usege.layout.adapter.ListAdapter;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.library.activities.LibraryActivity;

import java.io.Serializable;
import java.util.List;

public class ImageListFragment extends Fragment {
    LibraryActivity libraryActivity;

    public RecyclerView rcvPhoto;

    public ListAdapter listAdapter;
    private List<Image> lstImage;
    private Context context = null;
    public ImageListFragment() {
        // Required empty public constructor
    }

    public static ImageListFragment newInstance(List<Image> images) {
        ImageListFragment fragment = new ImageListFragment();
        Bundle args = new Bundle();
        args.putSerializable("List_images", (Serializable) images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            lstImage = (List<Image>) getArguments().getSerializable("List_images");
        }

        try {
            context = getActivity();
            libraryActivity = (LibraryActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layoutImageList = (LinearLayout) inflater.inflate(R.layout.fragment_image_list, null);

        rcvPhoto = layoutImageList.findViewById(R.id.rcv_photo);

        listAdapter = new ListAdapter(lstImage, context, new IClickItemImageListener() {
            @Override
            public void onClickItemImage(Image image, int position) {
                onClickGoToDetails(image, position);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvPhoto.setLayoutManager(linearLayoutManager);
        rcvPhoto.setAdapter(listAdapter);

        return layoutImageList;
    }

    private void onClickGoToDetails(Image image, int position) {
//        Intent intent = new Intent(context, ImageActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("object_image", image);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
        libraryActivity.sendAndReceiveImage(image, position);
    }
}