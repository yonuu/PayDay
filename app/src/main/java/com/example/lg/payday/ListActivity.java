package com.example.lg.payday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.lg.payday.R.id.item_textview;
import static com.example.lg.payday.R.id.start;
import static com.example.lg.payday.R.id.tv_wage;

public class ListActivity extends AppCompatActivity {

    public static int lstNum=0;
    private ListView recycleView;

    public static List<DTO> dtos = new ArrayList<>();
    List<String> uidLists = new ArrayList<>();
    private FirebaseDatabase database;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        adapter = new ListViewAdapter();
        database = FirebaseDatabase.getInstance();
        recycleView = (ListView)findViewById(R.id.recycleview);
        recycleView.setAdapter(adapter);
        //Listener for result
        recycleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //구현
                lstNum = i;
                Intent intent = new Intent(ListActivity.this, NfcActivity.class);
                startActivityForResult(intent,0);

            }
        });


        recycleView = (ListView) findViewById(R.id.recycleview);
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.resetItem();
                dtos.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DTO DTO = snapshot.getValue(DTO.class);
                    dtos.add(DTO);
                    uidLists.add(DTO.workspace);
                    uidLists.add(DTO.wage);

                    adapter.addItem(DTO.workspace, DTO.wage);
                }
                recycleView.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
//    public void resetTable(){
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, uidLists);
//    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(ListActivity.this, RegisterActivity.class));
                break;

//            case R.id.item:
//                Intent intent = new Intent(ListActivity.this, NfcActivity.class);
//                intent.putExtra("selNum",3);
//                startActivityForResult(intent,0);
//
//                break;
            default:
                break;
        }
    }

    class ListRecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).textView.setText(dtos.get(position).workspace);
            ((CustomViewHolder)holder).textView1.setText(dtos.get(position).wage);
        }

        @Override
        public int getItemCount() {
            return dtos.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            TextView textView1;
            public CustomViewHolder(View view) {
                super(view);
                textView = (TextView)view.findViewById(item_textview);
                textView1 = (TextView)view.findViewById(tv_wage);
            }
        }
    }
}
