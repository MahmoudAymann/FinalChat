package com.example.mayman.finalchat.ChatA;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.mayman.finalchat.R;
import com.example.mayman.finalchat.adapter.ChatAdp;
import com.example.mayman.finalchat.models.Friendliste;
import com.example.mayman.finalchat.models.Smsitem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import br.com.instachat.emojilibrary.controller.WhatsAppPanel;
import br.com.instachat.emojilibrary.model.layout.EmojiCompatActivity;
import br.com.instachat.emojilibrary.model.layout.WhatsAppPanelEventListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends EmojiCompatActivity implements WhatsAppPanelEventListener {
    // private FireBaseListener mCallback;
    private ArrayList<Smsitem> data;
    private RecyclerView rv;
    private ChatAdp adapter;
    private int children_cnt = 0;
    private int Min;   // opjects
    private WhatsAppPanel mBottomPanel;
    private CircleImageView img;
    private TextView status;
    private String Roomkey;

   private   String other_key, other_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        other_key = getIntent().getStringExtra("user_id_to_chat");
        Log.v("mnm",other_key);
        other_name = getIntent().getStringExtra("user_name_to_chat");
        //    Log.v("hugo", Roomkey.getPkey() + " " + Roomkey.getPname());
        mBottomPanel = new WhatsAppPanel(this, this, R.color.colorPrimary);
        data = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.chat);
        adapter = new ChatAdp(data, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        checkSum();
        try {
      //      getMesseges();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end onCreate()

    private void checkSum()
    {
        FirebaseUser Fu = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase Fdb = FirebaseDatabase.getInstance();
        DatabaseReference Fdbr = Fdb.getReference().child("ChatList").child(Fu.getUid());
        Query query = Fdbr.orderByChild("friend").equalTo(other_key);
        Fdbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0)
                {
                    Friendliste fn=null;
                    for (DataSnapshot datasnap:dataSnapshot.getChildren())
                    {
                       fn =(Friendliste) datasnap.getValue(Friendliste.class);
                        //OK->
                        break;
                    }
                    try {
                        getMesseges(Roomkey=fn.getRoomkey());
                    }catch (Exception e){e.printStackTrace();}


                }
                else
                {
                    // OK->
                    creatRoom();
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }//end checkSum
    private void creatRoom()
    {
        FirebaseUser Fu = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase Fdb = FirebaseDatabase.getInstance();
        DatabaseReference Fdbr = Fdb.getReference().child("mssg");
        DatabaseReference Fdbrmine = Fdb.getReference().child("ChatList").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference Fdbrhis = Fdb.getReference().child("ChatList").child(other_key);
        String push_key=Fdbr.push().getKey();

        Fdbrmine.child(Fdbrmine.push().getKey()).setValue(new Friendliste(other_key,push_key));
        Fdbrhis.child(Fdbrhis.push().getKey()).setValue(new Friendliste(FirebaseAuth.getInstance().getCurrentUser().getUid(),push_key));
        Roomkey=push_key;
        try
        {
            getMesseges(push_key);
        }
        catch (Exception e){e.printStackTrace();}


    }
    @Override
    public void onSendClicked() {
        sendmsg(mBottomPanel.getText());
    }

    void getMesseges(String key) throws Exception
    {
        final FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        final DatabaseReference fdbr = fdb.getReference().child("mssg").child(key);
        fdbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Smsitem nm = (Smsitem) dataSnapshot.getValue(Smsitem.class);
                Log.v("MNMO", nm.getMsg() + nm.getSender());
                data.add(nm);
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void sendmsg(String msg) {
        FirebaseUser Fu = FirebaseAuth.getInstance().getCurrentUser();
        Log.v("MNMO", Fu.getEmail().toString());
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+12:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yy:MM:dd:HH:mm:ss a");
        DateFormat date2 = new SimpleDateFormat("HH:mm a");
        DateFormat date3 = new SimpleDateFormat("MM:dd");
// you can get seconds by adding  "...:ss" to it
        String localTime = date.format(currentLocalTime);
        String localTime2 = date2.format(currentLocalTime);
        final FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        final DatabaseReference fdbr = fdb.getReference().child("mssg").child(Roomkey);
        String pushKey = fdbr.push().getKey();
        Smsitem s = new Smsitem(msg, "camo", localTime, Fu.getEmail().toString());
        fdbr.child(pushKey).setValue(s);
        mBottomPanel.setText("");
        notfy(s);

    }

    void notfy(Smsitem s) {
        //   Intent intent = new Intent(this, NotfyUsers.class);
        //  intent.putExtra(getString(R.string.smsitem), s);
        // intent.putExtra(getString(R.string.userPitem), Roomkey);
        //  startService(intent);
    }
}
