package com.fend.m_voting.Calon;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fend.m_voting.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CalonList extends ArrayAdapter<Calon>{
    private Activity context;
    private List<Calon> candidateList;

    public CalonList(Activity context, List<Calon>candidateList){
        super(context, R.layout.list_view_calon, candidateList);
        this.context = context;
        this.candidateList = candidateList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_view_calon, null, true);
        TextView no =  listViewItem.findViewById(R.id.no_urut);
        TextView nama =  listViewItem.findViewById(R.id.namacalon);
        ImageView imageView = listViewItem.findViewById(R.id.image_view_upload);
        Calon candidates = candidateList.get(position);

        no.setText(candidates.getNo_urut());
        nama.setText(candidates.getNama());

        Picasso.get()
                .load(candidates.getFoto())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);
        return listViewItem;
    }

}
