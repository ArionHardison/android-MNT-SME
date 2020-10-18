package com.dietmanager.dietician.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.activity.DocumentViewActivity;
import com.dietmanager.dietician.model.TrainingModule;
import com.dietmanager.dietician.utils.Utils;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Prasanth on 21-01-2020
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {

    List<TrainingModule> mList;
    Context mContext;

    public DocumentAdapter(List<TrainingModule> list, Context con) {
        this.mList = list;
        this.mContext = con;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_document, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TrainingModule data = mList.get(position);
//        int value = position + 1;
//        data.setDocTitle(mContext.getString(R.string.document) + " " + value);
//        holder.txtDocument.setText(mContext.getString(R.string.document) + " " + value);
        if (data.getName() != null) {
            String docName = data.getName().substring(0, 1).toUpperCase() + data.getName().substring(1);
            holder.txtDocument.setText(docName);
        }
        if (data.getCreatedAt() != null) {
            try {
                holder.txtDate.setText(mContext.getString(R.string.date) + " : " + Utils.getDate(data.getCreatedAt()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.cardDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DocumentViewActivity.class);
                intent.putExtra("document_name", data.getName());
                intent.putExtra("pdf_url", data.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardDocument;
        TextView txtDocument, txtDate;

        public MyViewHolder(View view) {
            super(view);
            cardDocument = view.findViewById(R.id.card_document);
            txtDocument = view.findViewById(R.id.txt_document);
            txtDate = view.findViewById(R.id.txt_date);
        }
    }
}
