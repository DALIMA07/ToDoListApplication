package com.example.todolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.MainActivity;
import com.example.todolist.Model.ToDoList;
import com.example.todolist.R;
//import com.example.todolist.Utils.Helper;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

//    private List<ToDoList> mList;
//    private MainActivity activity;
//    private Helper myDB;
//
//    public Adapter(Helper myDB , MainActivity activity){
//        this.activity = activity;
//        this.myDB = myDB;
//    }
//
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
//        return new MyViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        final ToDoList item = mList.get(position);
//        holder.mCheckBox.setText(item.getTask());
//        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
//        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    myDB.updateStatus(item.getId() , 1);
//                }else
//                    myDB.updateStatus(item.getId() , 0);
//            }
//        });
//    }
//
//    public boolean toBoolean(int num){
//        return num!=0;
//    }
//
//    public Context getContext(){
//        return activity;
//    }
//
//    public void setTasks(List<ToDoList> mList){
//        this.mList = mList;
//        notifyDataSetChanged();
//    }
//
//    public void deletTask(int position){
//        ToDoList item = mList.get(position);
//        myDB.deleteTask(item.getId());
//        mList.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public void editItem(int position){
//        ToDoList item = mList.get(position);
//
//        Bundle bundle = new Bundle();
//        bundle.putInt("id" , item.getId());
//        bundle.putString("task" , item.getTask());
//
//        AddNewTask task = new AddNewTask();
//        task.setArguments(bundle);
//        task.show(activity.getSupportFragmentManager() , task.getTag());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//        CheckBox mCheckBox;
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mCheckBox = itemView.findViewById(R.id.mcheckbox);
//        }
//    }


    private List<ToDoList> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public Adapter(MainActivity mainActivity , List<ToDoList> todoList){
        this.todoList = todoList;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.task_layout , parent , false);
        firestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        ToDoList toDoModel = todoList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }
    public void editTask(int position){
        ToDoList toDoModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task" , toDoModel.getTask());
        bundle.putString("due" , toDoModel.getDue());
        bundle.putString("id" , toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager() , addNewTask.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoList toDoModel = todoList.get(position);
        holder.mCheckBox.setText(toDoModel.getTask());

        holder.mDueDateTv.setText("Due On " + toDoModel.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 1);
                }else{
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 0);
                }
            }
        });

    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);

        }
    }
}