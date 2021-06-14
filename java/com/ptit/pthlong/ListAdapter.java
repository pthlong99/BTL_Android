package com.ptit.pthlong;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ptit.pthlong.Model.Note;

import java.util.List;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<Note> notes;
    private Context context;

    public ListAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View noteView =
                inflater.inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(noteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.item_description.setText(note.getDescription());
        holder.item_title.setText(note.getTitle());
        holder.item_date.setText(note.getDate());
        holder.item_done.setChecked(note.getDone());
        holder.item_done.setEnabled(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewItemActivity.class);
                intent.putExtra("title", note.getTitle());
                intent.putExtra("date", note.getDate());
                intent.putExtra("description", note.getDescription());
                intent.putExtra("done", note.getDone().toString());
                intent.putExtra("id", note.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(notes != null)
            return notes.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Note note;
        public TextView item_title, item_description, item_date;
        public CheckBox item_done;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_description = itemView.findViewById(R.id.item_description);
            item_title = itemView.findViewById(R.id.item_title);
            item_date = itemView.findViewById(R.id.item_date);
            item_done = itemView.findViewById(R.id.item_done);
        }
    }
}