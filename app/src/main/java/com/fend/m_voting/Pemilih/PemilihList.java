package com.fend.m_voting.Pemilih;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fend.m_voting.R;

import java.util.List;

public class PemilihList extends ArrayAdapter<Pemilih> {

private Activity context;
private List<Pemilih> voterList;

        public PemilihList(Activity context, List<Pemilih>voterList){
        super(context, R.layout.list_view_voter, voterList);
        this.context = context;
        this.voterList = voterList;
        }

@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_view_voter, null, true);
        TextView textViewNIM =  listViewItem.findViewById(R.id.textViewNIM);
        TextView textViewName =  listViewItem.findViewById(R.id.textViewName);
        TextView textViewStatus =  listViewItem.findViewById(R.id.textViewStatus);

        Pemilih voters = voterList.get(position);

        textViewNIM.setText(voters.getNIM());
        textViewName.setText(voters.getName());
        textViewStatus.setText(voters.getStatus());

        return listViewItem;
        }




}

