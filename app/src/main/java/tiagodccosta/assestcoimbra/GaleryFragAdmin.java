package tiagodccosta.assestcoimbra;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class GaleryFragAdmin extends Fragment {

    private FirebaseListAdapter<UploadInfo> adapter;
    private FloatingActionButton fab;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Galeria");

    private GridView gridView;

    private ImageView post;

    private List<UploadInfo> posts;

    public GaleryFragAdmin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_galery_frag_admin, container, false);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Chooser4.class));
            }
        });

        gridView = (GridView) rootView.findViewById(R.id.list_of_photos);

        posts = new ArrayList<>();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                UploadInfo uploadInfo = posts.get(i);

                showUpdateDialog(uploadInfo.getDescricao(), uploadInfo.getUrl(), uploadInfo.getKey());

                return false;
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                UploadInfo uploadInfo = posts.get(i);

                Uri imageUri = Uri.parse(uploadInfo.getUrl());

                Intent fullscreenIntent = new Intent(getContext(), FullscreenActivity.class);
                fullscreenIntent.setData(imageUri);
                startActivity(fullscreenIntent);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadInfo uploadInfo = postSnapshot.getValue(UploadInfo.class);
                    posts.add(uploadInfo);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                displayGalery();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        return rootView;
    }

    private void showUpdateDialog(final String descricao, final String url, final String id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialogbox4, null);

        dialogBuilder.setView(dialogView);

        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        final Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);



        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("Galeria").orderByChild("key").equalTo(id);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                        Toast.makeText(getContext(), "Foto eliminada com sucesso!", Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });

    }

    private void displayGalery() {
        final GridView listOfPosts;
        listOfPosts = (GridView) getActivity().findViewById(R.id.list_of_photos);
        adapter = new FirebaseListAdapter<UploadInfo>(getActivity(), UploadInfo.class, R.layout.list_galery, FirebaseDatabase.getInstance().getReference("Galeria")) {
            @Override
            protected void populateView(View v, UploadInfo model, int position) {

                post = (ImageView) v.findViewById(R.id.postImage);

                Glide.with(getContext())
                        .load(model.getUrl())
                        .fitCenter()
                        .centerCrop()
                        .into(post);
            }
        };
        listOfPosts.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listOfPosts.setAdapter(adapter);
    }

}
