package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todolist.Adapter.Adapter;
import com.example.todolist.Model.ToDoList;
//import com.example.todolist.Utils.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnDialogCloseListner{

//    private RecyclerView Recyclerview;
//    private FloatingActionButton fab;
//    private Helper myDB;
//    private List<ToDoList> list;
//    private Adapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Recyclerview = findViewById(R.id.recyclerview);
//        fab = findViewById(R.id.fab);
//        myDB = new Helper(MainActivity.this);
//        list = new ArrayList<>();
//        adapter = new Adapter(myDB , MainActivity.this);
//
//            Recyclerview.setHasFixedSize(true);
//            Recyclerview.setLayoutManager(new LinearLayoutManager(this));
//            Recyclerview.setAdapter(adapter);
//
//            list = myDB.getAllTasks();
//            Collections.reverse(list);
//            adapter.setTasks(list);
//
//            fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);
//            }
//        });
//            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
//            itemTouchHelper.attachToRecyclerView(Recyclerview);
//    }
//
//    @Override
//    public void onDialogClose(DialogInterface dialogInterface) {
//        list = myDB.getAllTasks();
//        Collections.reverse(list);
//        adapter.setTasks(list);
//        adapter.notifyDataSetChanged();
//    }


    private RecyclerView recyclerView;
    private FloatingActionButton mFab;
    private FirebaseFirestore firestore;
    private Adapter adapter;
    private List<ToDoList> mList;
    private Query query;
    private ListenerRegistration listenerRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        mFab = findViewById(R.id.fab);
        firestore = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);
            }
        });

        mList = new ArrayList<>();
        adapter = new Adapter(MainActivity.this , mList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        showData();
        recyclerView.setAdapter(adapter);

   }
    private void showData(){
        query = firestore.collection("task").orderBy("time" , Query.Direction.DESCENDING);

        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        ToDoList toDoList = documentChange.getDocument().toObject(ToDoList.class).withId(id);
                        mList.add(toDoList);
                        adapter.notifyDataSetChanged();
                    }
                }
                listenerRegistration.remove();

            }
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}