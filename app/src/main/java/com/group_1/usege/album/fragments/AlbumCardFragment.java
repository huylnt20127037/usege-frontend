package com.group_1.usege.album.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.layout.adapter.AlbumAdapter;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.library.service.MasterAlbumService;
import com.group_1.usege.library.service.MasterAlbumServiceGenerator;
import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.library.service.MasterFileServiceGenerator;
import com.group_1.usege.library.service.MasterTrashService;
import com.group_1.usege.library.service.MasterTrashServiceGenerator;
import com.group_1.usege.model.Album;
import com.group_1.usege.model.UserAlbum;
import com.group_1.usege.model.UserFile;
import com.group_1.usege.utilities.view.DialogueUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;

@AndroidEntryPoint
public class AlbumCardFragment extends Fragment {
    FragmentTransaction ft;
    TextView totalImage;

    public RecyclerView rcvPhoto;

    public AlbumAdapter albumAdapter;
    private final List<UserAlbum> lstAlbum = new ArrayList<UserAlbum>() {
        {
            add(new UserAlbum("", "favorite", "", 0, null)); // favorite album
            add(new UserAlbum("", "trash", "", 0, null)); // trash album
        }
    };
    private Context context = null;

    public AlbumCardFragment() {
        // Required empty public constructor
    }
    @Inject
    public MasterTrashServiceGenerator masterTrashServiceGeneratior;
    @Inject
    public MasterFileServiceGenerator masterFileServiceGenerator;
    @Inject
    public MasterAlbumServiceGenerator masterAlbumServiceGenerator;
    @Inject
    public TokenRepository tokenRepository;

    public int LIMIT = 999;

    public static AlbumCardFragment newInstance(List<Album> images) {
        AlbumCardFragment fragment = new AlbumCardFragment();
        Bundle args = new Bundle();
        args.putSerializable("List_album_images", (Serializable) images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            lstAlbum = (List<UserAlbum>) getArguments().getSerializable("List_album_images");
        }

        try {
            context = getActivity();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }

        albumAdapter = new AlbumAdapter(lstAlbum, context, "card");
        albumAdapter.setOnClickListener(new AlbumAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                System.out.println("Click :" + position);
                // Handle click event here
                if (context.getClass().equals(LibraryActivity.class)) {
                    Activity activity = (Activity) context;
                    if (activity instanceof LibraryActivity) {
                        LibraryActivity libActivity = (LibraryActivity) activity;
                        UserAlbum selectedAlbum = lstAlbum.get(position);
                        final String password = selectedAlbum.getPassword();
                        if (password != null)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Input your album password!");
                            final EditText input = new EditText(context);
                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            builder.setView(input);
                            builder.setPositiveButton("OK", (dialog, which) -> {

                                dialog.dismiss();
                                if (input.getText().toString().equals(password))
                                {
                                    libActivity.clickOpenAlbumImageList(selectedAlbum);
                                }
                                else
                                {
                                    DialogueUtilities.showNormalDialogue(context, R.string.album_password_is_not_right, null);
                                }
                            });
                            builder.setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.cancel();
                            });
                            builder.show();
                        }
                        else
                            libActivity.clickOpenAlbumImageList(selectedAlbum);

                        System.out.println("Album size: " + lstAlbum.size());
                    }
                }
            }
        });



        Single<MasterAlbumService.QueryResponse<UserAlbum>> results = getAlbums();
        results
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe(this::handleAfterCall);
    }

    private void handleAfterCall(MasterAlbumService.QueryResponse<UserAlbum> response, Throwable throwable) {
        if (throwable != null)
            System.out.println("Get Album error!");
        else {
            List<UserAlbum> albums = response.getResponse();
            lstAlbum.addAll(albums);
            System.out.println("Album size " + lstAlbum.size());
            albumAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutImageCard = inflater.inflate(R.layout.fragment_image_card, null);

        rcvPhoto = layoutImageCard.findViewById(R.id.rcv_photo);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setAdapter(albumAdapter);

        return layoutImageCard;
    }

    private Single<MasterAlbumService.QueryResponse<UserAlbum>> getAlbums() {
        return masterAlbumServiceGenerator
                .getService()
                .getAlbums(tokenRepository.getToken().getUserId(), LIMIT);
    }


}
