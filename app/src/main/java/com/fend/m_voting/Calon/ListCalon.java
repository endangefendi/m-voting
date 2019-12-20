package com.fend.m_voting.Calon;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fend.m_voting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.fend.m_voting.R.layout.list_hasil;

public class ListCalon extends ArrayAdapter<Calon>{

    private Activity context;
    private List<Calon> candidateList;
    private Float hasil = null;

    public ListCalon(Activity context, List<Calon>candidateList){
        super(context, list_hasil, candidateList);
        this.context = context;
        this.candidateList = candidateList;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(list_hasil, null, true);
        TextView no =  listViewItem.findViewById(R.id.no_urut);
        TextView nama =  listViewItem.findViewById(R.id.namacalon);
        TextView suara =  listViewItem.findViewById(R.id.suara);
        ImageView imageView = listViewItem.findViewById(R.id.foto_calon);
        final TextView persen = listViewItem.findViewById(R.id.persen);

        final Calon candidates = candidateList.get(position);
        no.setText(candidates.getNo_urut());
        nama.setText(candidates.getNama());
        suara.setText(candidates.getSuara());

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Voter").orderByChild("nim").addValueEventListener( new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nim = (String) ds.child("nim").getValue();
                    if (nim != null) {
                        count = (count + 1);
                    }
                    hasil= Float.valueOf(candidates.getSuara())*100/count;
                    persen.setText(hasil.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        // Gets linearlayout
        LinearLayout layout = listViewItem.findViewById(R.id.gra);
        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        //params.height = 100;
        params.width = (int) (Float.parseFloat(candidates.getSuara())*3);

        layout.setLayoutParams(params);

        Picasso.get()
                .load(candidates.getFoto())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);

        return listViewItem;
    }

}
