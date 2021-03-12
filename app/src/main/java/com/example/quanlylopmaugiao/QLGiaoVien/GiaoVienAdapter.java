package com.example.quanlylopmaugiao.QLGiaoVien;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlylopmaugiao.R;

import java.util.ArrayList;

public class GiaoVienAdapter extends BaseAdapter {

    int Layout;
    Context context;
    ArrayList<GiaoVien> ds;

    public GiaoVienAdapter(int layout, Context context, ArrayList<GiaoVien> ds) {
        Layout = layout;
        this.context = context;
        this.ds = ds;
    }

    @Override
    public int getCount() {
        return ds.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(Layout,null);
        TextView tvTen, tvNamSinh, tvGioiTinh;
        ImageView img_Anh;

        tvTen=view.findViewById(R.id.tv_TenGV);
        tvNamSinh=view.findViewById(R.id.tv_NamSinh);
        tvGioiTinh=view.findViewById(R.id.tv_GioiTinhGV);
        img_Anh=view.findViewById(R.id.image_GV);

        GiaoVien gv=ds.get(i);
        tvTen.setText("Tên giáo viên: "+gv.getTenGV());
        tvNamSinh.setText("Năm sinh: "+gv.getNamSinh());
        if(gv.getGioiTinh()==0) tvGioiTinh.setText("Giới tính: Nam");
        else tvGioiTinh.setText("Giới tính: Nữ");

        //Convert byte[] --> bitmap
        byte[] Hinh=gv.getAnh();
        Bitmap bm_Hinh_MonAn= BitmapFactory.decodeByteArray(Hinh,0,Hinh.length);
        img_Anh.setImageBitmap(bm_Hinh_MonAn);

        return view;
    }
}
