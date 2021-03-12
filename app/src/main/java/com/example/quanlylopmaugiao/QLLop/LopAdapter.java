package com.example.quanlylopmaugiao.QLLop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlylopmaugiao.R;

import java.util.ArrayList;

public class LopAdapter extends BaseAdapter {
    Context context;
    ArrayList<Lop> ds;
    int SiSo;

    public LopAdapter(Context context, ArrayList<Lop> ds, int SiSo) {
        this.context = context;
        this.ds = ds;
        this.SiSo=SiSo;
    }

    @Override
    public int getCount() {
        return ds.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        TextView tv_MaGVCN, tv_TenLop, tv_SiSo;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.item_lop, null);
            holder=new ViewHolder();
            //Ánh xạ
            holder.tv_MaGVCN=convertView.findViewById(R.id.tv_MaGVCN);
            holder.tv_TenLop=convertView.findViewById(R.id.tv_TenLop);
            holder.tv_SiSo=convertView.findViewById(R.id.tv_SiSo);
            convertView.setTag(holder);
        }else {holder= (ViewHolder) convertView.getTag();}

        Lop l=ds.get(position);
        holder.tv_MaGVCN.setText(l.getMaLop());
        holder.tv_TenLop.setText(l.getTenLop());
        holder.tv_SiSo.setText("Sĩ Số: "+SiSo);

        return convertView;
    }
}
