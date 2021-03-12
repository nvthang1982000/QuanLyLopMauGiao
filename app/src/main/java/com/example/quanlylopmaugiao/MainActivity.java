package com.example.quanlylopmaugiao;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quanlylopmaugiao.HocSinh.QLHocSinh;
import com.example.quanlylopmaugiao.QLGiaoVien.QLGiaoVien;
import com.example.quanlylopmaugiao.QLLop.QLLop;

public class MainActivity extends AppCompatActivity {

    Button btn_QLLop, btn_QLGV, btn_QLHS, btn_TT, btn_TK;

    Toolbar toolbar;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        Event();
    }

    private void Event() {
        btn_QLLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(MainActivity.this, QLLop.class);
                startActivity(it);
            }
        });
        btn_QLGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(MainActivity.this, QLGiaoVien.class);
                startActivity(it);
            }
        });
        btn_QLHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(MainActivity.this, QLHocSinh.class);
                startActivity(it);
            }
        });

    }

    private void Init() {
        btn_QLLop=findViewById(R.id.btn_QLLop);
        btn_QLGV=findViewById(R.id.btnQLGV);
        btn_QLHS=findViewById(R.id.btnQLHS);
        btn_TT=findViewById(R.id.btnTT);
        btn_TK=findViewById(R.id.btnTK);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quản lý trường mầm non");
        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}