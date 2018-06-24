package tiagodccosta.assestcoimbra;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GaleryFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GaleryFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GaleryFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FirebaseListAdapter<UploadInfo> adapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Galeria");

    private ImageView post;
    private GridView gridView;
    private List<UploadInfo> posts;

    public GaleryFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GaleryFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GaleryFrag newInstance(String param1, String param2) {
        GaleryFrag fragment = new GaleryFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_galery, container, false);

        gridView = (GridView) rootView.findViewById(R.id.list_of_photos);

        posts = new ArrayList<>();


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

        // Inflate the layout for this fragment
        return rootView;
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
