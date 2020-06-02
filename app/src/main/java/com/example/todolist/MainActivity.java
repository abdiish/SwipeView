package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements ListItemClickListener{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ToDoAdapter adapter;
    SQLiteDatabase mDb;
    private View view;
    private Paint p = new Paint();
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab          = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        ToDoDbHelper dbHelper = new ToDoDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = getAllTasks();
        adapter = new ToDoAdapter(this, cursor, this);
        recyclerView.setAdapter(adapter);
        initSwipe();
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    id = (long)viewHolder.itemView.getTag();
                    removeTask(id);
                    adapter.swapCursor(getAllTasks());
                }else {
                    id = (long) viewHolder.itemView.getTag();
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#64dd17"));
                        RectF background = new RectF((float) itemView.getLeft(), (float)itemView.getTop(), dX, (float)itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float)itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest,p);
                    }else {
                        p.setColor(Color.parseColor("#d50000"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float)itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void removeTask(long id) {
        mDb.delete(ToDoContract.ToDoListEntry.TABLE_NAME,
                ToDoContract.ToDoListEntry._ID + "=" + id,
                null);
        Toast.makeText(this, "Task is deleted!", Toast.LENGTH_SHORT).show();
    }

    private Cursor getAllTasks() {

        return mDb.query(ToDoContract.ToDoListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ToDoContract.ToDoListEntry.COLUMN_TIMESTAMP);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.swapCursor(getAllTasks());
    }

    @Override
    public void onClick(View view, int position) {
        long id = (long) view.getTag();
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        Toast.makeText(this, "id: "+id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            mDb.delete(ToDoContract.ToDoListEntry.TABLE_NAME, null, null);
            adapter.swapCursor(getAllTasks());
            Toast.makeText(this, "All tasks are deleted! ", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}