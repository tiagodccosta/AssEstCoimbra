package tiagodccosta.assestcoimbra;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudyFragAdmin extends Fragment {


    private FirebaseListAdapter<UploadInfo> adapter;
    private FloatingActionButton fab;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Recursos");

    private ListView listView;

    private TextView descricao;
    private ImageView post;
    private TextView time;

    private List<UploadInfo> posts;

    public StudyFragAdmin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_study_frag_admin, container, false);


        listView = (ListView) rootView.findViewById(R.id.list_of_resources);

        posts = new ArrayList<>();

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Chooser3.class));
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                UploadInfo uploadInfo = posts.get(i);

                showUpdateDialog(uploadInfo.getDescricao(), uploadInfo.getUrl(), uploadInfo.getKey());

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UploadInfo uploadInfo = posts.get(i);

                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadInfo.getUrl()));
                startActivity(intent);
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
                displayResources();
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



        // Inflate the layout for this fragment
        return rootView;
    }

    private void showUpdateDialog(final String descricao, final String url, final String id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialogbox5, null);

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
                Query applesQuery = ref.child("Recursos").orderByChild("key").equalTo(id);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                        Toast.makeText(getContext(), "Ficheiro eliminado com sucesso!", Toast.LENGTH_LONG).show();
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

    private void displayResources() {
        final ListView listOfPosts;
        listOfPosts = (ListView) getActivity().findViewById(R.id.list_of_resources);
        adapter = new FirebaseListAdapter<UploadInfo>(getActivity(), UploadInfo.class, R.layout.list_resources, FirebaseDatabase.getInstance().getReference("Recursos")) {
            @Override
            protected void populateView(View v, UploadInfo model, int position) {

                descricao = (TextView) v.findViewById(R.id.resourceTitle);
                time = (TextView) v.findViewById(R.id.resourceTime);

                descricao.setText(model.getDescricao());
                time.setText(DateFormat.format("dd-MM-yyyy", model.getPostTime()));

            }
        };
        listOfPosts.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listOfPosts.setAdapter(adapter);
    }

}
