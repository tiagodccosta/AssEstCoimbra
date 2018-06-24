package tiagodccosta.assestcoimbra;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragAdmin extends Fragment {

    private FirebaseListAdapter<UploadInfo> adapter;
    private FloatingActionButton fab;

    private  FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Feed Posts");

    private ListView listView;

    public static View rootView;

    private TextView descricao;
    private ImageView post;

    private List<UploadInfo> posts;

    private TextView time;


    public FeedFragAdmin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_feed_frag_admin, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_of_posts);

        posts = new ArrayList<>();

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Chooser.class));
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
                displayFeed();
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

        final View dialogView = inflater.inflate(R.layout.dialogbox2, null);

        dialogBuilder.setView(dialogView);

        final EditText resposta = (EditText) dialogView.findViewById(R.id.resposta);
        final Button btnResposta = (Button) dialogView.findViewById(R.id.btnOK);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        final Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);



        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer = resposta.getText().toString();

                if(TextUtils.isEmpty(answer)) {
                    resposta.setError("Campo vazio!");
                    return;
                }

                update(answer, url, id);

                alertDialog.dismiss();

            }
        });

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
                Query applesQuery = ref.child("Feed Posts").orderByChild("key").equalTo(id);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                        Toast.makeText(getContext(), "Post eliminado com sucesso!", Toast.LENGTH_LONG).show();
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

    private boolean update(String descricao, String url, String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feed Posts").child(id);

        UploadInfo uploadInfo = new UploadInfo(descricao, url, id);

        databaseReference.setValue(uploadInfo);

        Toast.makeText(getContext(), "Alteração efetuada com sucesso!", Toast.LENGTH_LONG).show();

        return true;

    }


    private void displayFeed() {
        final ListView listOfPosts;
        listOfPosts = (ListView) getActivity().findViewById(R.id.list_of_posts);
        adapter = new FirebaseListAdapter<UploadInfo>(getActivity(), UploadInfo.class, R.layout.list_feed, FirebaseDatabase.getInstance().getReference("Feed Posts")) {
            @Override
            protected void populateView(View v, UploadInfo model, int position) {

                descricao = (TextView) v.findViewById(R.id.titlePost);
                post = (ImageView) v.findViewById(R.id.postImage);
                time = (TextView) v.findViewById(R.id.timePost);

                descricao.setText(model.getDescricao());
                time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getPostTime()));
                Glide.with(getContext())
                        .load(model.getUrl())
                        .fitCenter()
                        .centerCrop()
                        .into(post);
            }
        };
        listOfPosts.setAdapter(adapter);
        listOfPosts.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }



}
