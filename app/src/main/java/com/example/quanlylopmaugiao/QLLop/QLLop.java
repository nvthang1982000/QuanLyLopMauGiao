package com.example.quanlylopmaugiao.QLLop;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlylopmaugiao.R;

public class QLLop extends AppCompatActivity {

    ImageButton imb_add, imb_edit, imb_delete;
    EditText edt_Ma, edt_Ten, edt_GhiChu;

    ListView lvLop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qllop_layout);
        Init();
        Event();
    }

    private void Event() {

    }

    private void Init() {

        imb_add=findViewById(R.id.imb_Add);
        imb_edit=findViewById(R.id.imb_edit);
        imb_delete=findViewById(R.id.imb_Delete);

        edt_Ma=findViewById(R.id.edt_MaLop);
        edt_Ten=findViewById(R.id.edt_TenLop);
        edt_GhiChu=findViewById(R.id.edt_GhiChu);
    }
}
