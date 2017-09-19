package com.example.mayman.finalchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mayman.finalchat.AndroidEmoji;
import com.example.mayman.finalchat.R;
import com.example.mayman.finalchat.genericviewholders.GenericViewHolder;
import com.example.mayman.finalchat.models.Smsitem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by MahmoudAyman on 3/18/2017.
 */

public class ChatAdp extends RecyclerView.Adapter<GenericViewHolder> {
    private static int selectedItem = -1;
    ArrayList<Smsitem> data1;
    Context C;

    public ChatAdp(ArrayList<Smsitem> data1, Context c) {
        this.data1 = data1;
        C = c;
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
    }

    @Override
    public int getItemViewType(int position) //1 ,2
    {
        int ret;
        if (data1 == null) {
            return 1;
        }
        ret = get_type(data1.get(position));
        // Log.v(ContractDepug.PUBTAG,data1.get(position).getMsg()+"  "+data1.get(position).getSender()+"  ret->"+ret);
        return ret;
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    //1 text pubblechatme  -> vh 1->list item 2
    //2 img pubblechatme -> vh 2 ->  item 1
    //3 hybet txt pubblechatme  vh3-> iem 3
    //4 text him  vh 1 other -> l2 other
    //5 img him  v2 l 1 other
    //6 hyber him l 3 other v 3
    {
        View view;
        if (viewType == 1)
        {
            Log.v("mnm","view1");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item2, parent, false);
            return new Viewholder1(view);
        } else if (viewType == 3)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item3, parent, false);
            return new Viewholder3(view);
        } //-----------------------------------------------------------------------------------------------------------------
        else if (viewType == 4) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item2other, parent, false);
            return new Viewholder1Other(view);
        } else if (viewType == 6) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item3other, parent, false);
            return new Viewholder3Other(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position)
    {
        try {
            holder.bind(position);
        } catch (Exception ignore) {
        }


    }

    @Override
    public int getItemCount() {
        return (data1.size());
    }

    private int get_type(Smsitem msg) {
        String demo1 = "http";
        String imgTag1 = "wallbapers";
        if (msg.getMsg().startsWith(demo1)) {
            if (msg.getMsg().contains(imgTag1)) {
                return getUser(2, msg);
            }
            return getUser(3, msg);
        }
        return getUser(1, msg);
    }

    private int getUser(int ret, Smsitem msg)
    {
        FirebaseUser fu= FirebaseAuth.getInstance().getCurrentUser();
        if (msg.getSender().equals(fu.getEmail()))
        {
            return ret;
        }
        switch (ret)
        {

            case 1:
                return 4;
            case 2:
                return 5;
            case 3:
                return 6;
        }
        return 2;
    }

    //_-_veiw holder-------------------------------------------------
    private class Viewholder1 extends GenericViewHolder {
        View x;
        TextView e;

        private Viewholder1(View itemView) {
            super(itemView);

            x = itemView;
            e = (TextView) x.findViewById(R.id.item2text);
        }

        @Override

        public void bind(int position) {
            //   Typeface custom_font = Typeface.createFromAsset(C.getAssets(), "fonts/AndroidEmoji.ttf");
            // e.setTypeface(custom_font);
            Log.v("mnm","view1 bind");
            String em = data1.get(position).getMsg();
            e.setText(AndroidEmoji.ensure(em, C));
            //  e.setTypeface(custom_font);
        }
    }

    //--------------------------
    private class Viewholder3 extends GenericViewHolder implements View.OnClickListener {
        View x;
        TextView e;

        private Viewholder3(View itemView) {
            super(itemView);
            x = itemView;
            e = (TextView) x.findViewById(R.id.item2text2);
            e.setOnClickListener(this);
        }

        @Override
        public void bind(int position) {
            e.setText(data1.get(position).getMsg());
        }

        @Override
        public void onClick(View view)
        {

            Uri uri = Uri.parse(e.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // Create and start the chooser
            Intent chooser = Intent.createChooser(intent, "Open with");
            C.startActivity(chooser);
        }
    }

    //-----------------------------other--------------------------------------
    private class Viewholder1Other extends GenericViewHolder {
        View x;
        TextView e;

        private Viewholder1Other(View itemView) {
            super(itemView);

            x = itemView;
            e = (TextView) x.findViewById(R.id.item2textother);
        }

        @Override
        public void bind(int position) {
            String em = data1.get(position).getMsg();
            e.setText(AndroidEmoji.ensure(em, C));
        }
    }

    //--------------------------
    private class Viewholder3Other extends GenericViewHolder implements View.OnClickListener {
        View x;
        TextView e;

        private Viewholder3Other(View itemView) {
            super(itemView);
            x = itemView;
            e = (TextView) x.findViewById(R.id.item2text2other);
            e.setOnClickListener(this);
        }

        @Override
        public void bind(int position) {
            e.setText(data1.get(position).getMsg());
        }

        @Override
        public void onClick(View view) {

            Uri uri = Uri.parse(e.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // Create and start the chooser
            Intent chooser = Intent.createChooser(intent, "Open with");
            C.startActivity(chooser);
        }
    }
}
//1 text pubblechatme
//2 img pubblechatme
//3 hybet txt pubblechatme
//4 text him
//5 img him
//6 hyber him