package tiagodccosta.assestcoimbra;


import android.app.AlertDialog;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
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
 */
public class ChatAdmin extends Fragment {

    private FirebaseListAdapter<ChatMessage> adapter;
    private FloatingActionButton fab;

    FirebaseAuth firebaseAuth;

    private  FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Mensagens");

    private List<ChatMessage> users;
    private ListView listView;

    private TextView questionUser, QuestionText, userTime, adminAns;

    public ChatAdmin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_chat_admin, container, false);

        users = new ArrayList<>();

        listView = (ListView) rootView.findViewById(R.id.list_of_message);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ChatMessage chatMessage = users.get(i);

                showUpdateDialog(chatMessage.getId(), chatMessage.getMessageUser(), chatMessage.getMessageText());

                return false;
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = postSnapshot.getValue(ChatMessage.class);

                    users.add(chatMessage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                displayChat();
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


    private void showUpdateDialog(final String userId, final String userName, final String userQ) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialogbox, null);

        dialogBuilder.setView(dialogView);

        final EditText resposta = (EditText) dialogView.findViewById(R.id.resposta);
        final Button btnResposta = (Button) dialogView.findViewById(R.id.btnOK);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);


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

                update(userId, userName, userQ, answer);

                alertDialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private boolean update(String id, String userName, String userText, String adminAns) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Mensagens").child(id);

        ChatMessage chatMessage = new ChatMessage(userText, userName, adminAns, id);

        databaseReference.setValue(chatMessage);

        Toast.makeText(getContext(), "Resposta efetuada com sucesso!", Toast.LENGTH_LONG).show();

        return true;
    }


    private void displayChat() {
        final ListView listOfMessages;
        listOfMessages = (ListView) getActivity().findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference("Mensagens")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                QuestionText = (TextView)v.findViewById(R.id.userQuestion);
                questionUser = (TextView)v.findViewById(R.id.userNameQ);
                userTime = (TextView)v.findViewById(R.id.userTime);
                adminAns = (TextView)v.findViewById(R.id.adminAns);


                QuestionText.setText(model.getMessageText());
                questionUser.setText(model.getMessageUser());
                userTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                adminAns.setText(model.getAdminAns());

            }
        };
        listOfMessages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listOfMessages.setAdapter(adapter);
        listOfMessages.setStackFromBottom(true);

    }


}
