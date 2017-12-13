package com.example.lg.payday;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText workspace;
    private EditText wage;
    private FirebaseDatabase database;
    private List<DTO> dtos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database = FirebaseDatabase.getInstance();

        workspace = (EditText) findViewById(R.id.workspace);
        wage = (EditText) findViewById(R.id.wage);
        auth = FirebaseAuth.getInstance();

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.delete:
                workspace.setText(null);
                wage.setText(null);
                break;

            case R.id.register:
                DTO dtos = new DTO();
                dtos.workspace= workspace.getText().toString();
                dtos.wage = wage.getText().toString();
                dtos.uid = auth.getCurrentUser().getUid();
                dtos.userId = auth.getCurrentUser().getEmail();
                dtos.ago = "";
                dtos.now = "";
                dtos.state =0;
                database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(workspace.getText().toString()).setValue(dtos);
                startActivity(new Intent(RegisterActivity.this, ListActivity.class));
                break;

            default:

        }
    }


}
