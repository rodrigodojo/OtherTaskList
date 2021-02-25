package com.dojo.tasklist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dojo.tasklist.db.TaskContract;
import com.dojo.tasklist.db.TaskDBHelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TaskDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        updateUI();
    }

    private void updateUI(){
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TAREFA},
                null,null,null,null,null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                R.layout.celula,cursor,
                new String[]{TaskContract.Columns.TAREFA},
                new int[] {R.id.textoCelula},
                0);
        ListView listView = findViewById(R.id.listaTarefas);
        listView.setAdapter(listAdapter);
    }

    public void adicionarItem(View view){
        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setTitle("Adicione uma Tarefa");
        buider.setMessage("O que você precisa fazer?");

        final EditText inputField = new EditText(this);
        buider.setView(inputField);
        buider.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tarefa = inputField.getText().toString();
                Log.d("MainActivity", tarefa);

                helper = new TaskDBHelper(MainActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.clear();
                values.put(TaskContract.Columns.TAREFA, tarefa);
                db.insertWithOnConflict(TaskContract.TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);

                updateUI();
            }
        });

        buider.setNegativeButton("Cancelar", null);
        buider.create().show();

    }

    public void apagarItem(View view){
        View v = (View) view.getParent();
        TextView taskTextView = v.findViewById(R.id.textoCelula);
        String tarefa = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'", TaskContract.TABLE,TaskContract.Columns.TAREFA,tarefa);

        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);

        updateUI();
    }
}