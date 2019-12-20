package com.fend.m_voting.Admin;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.TextView;

import com.fend.m_voting.R;

import java.util.List;


public class AdminList extends ArrayAdapter<Admin> {
    private Activity context;
    private List<Admin> adminList;

    AdminList(Activity context, List<Admin> adminList){
        super(context, R.layout.list_view_admin, adminList);
        this.context = context;
        this.adminList = adminList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.list_view_admin, null, true);
        TextView textViewNIM = listViewItem.findViewById(R.id.textViewNIM);

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);

        Admin admins = adminList.get(position);

        textViewNIM.setText(admins.getEmail());
        textViewName.setText(admins.getName());

        return listViewItem;
    }
}
