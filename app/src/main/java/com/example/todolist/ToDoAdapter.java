package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private Context mContext;
    private Cursor cursor;
    private ListItemClickListener listItemClickListener;
    long id;

    public ToDoAdapter(Context mContext, Cursor cursor, ListItemClickListener listItemClickListener) {
        this.mContext = mContext;
        this.cursor = cursor;
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public ToDoAdapter.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ToDoViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }
        String task  = cursor.getString(cursor.getColumnIndex(ToDoContract.ToDoListEntry.COLUMN_TASK));
        int priority = cursor.getInt(cursor.getColumnIndex(ToDoContract.ToDoListEntry.COLUMN_PRIORITY));
        String timestamp = cursor.getString(cursor.getColumnIndex(ToDoContract.ToDoListEntry.COLUMN_TIMESTAMP));

        id = cursor.getLong(cursor.getColumnIndex(ToDoContract.ToDoListEntry._ID));
        holder.itemView.setTag(id);

        holder.task_text.setText(task);
        holder.priority_text.setText(String.valueOf(priority));
        holder.timestamp_text.setText(timestamp);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder{

        TextView task_text, priority_text, timestamp_text;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);

            task_text = itemView.findViewById(R.id.task_text);
            priority_text = itemView.findViewById(R.id.priority_text);
            timestamp_text = itemView.findViewById(R.id.timestamp_text);
            //itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCreate = new Intent(mContext, SecondActivity.class);
                    intentCreate.putExtra("id", id); //Send id Group to activitie
                    mContext.startActivity(intentCreate);
                }
            });
        }

        /*@Override
        public void onClick(View v) {
            listItemClickListener.onClick(v, getAdapterPosition());
        }*/
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }
}
