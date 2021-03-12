package com.example.quanlylopmaugiao.HocSinh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.quanlylopmaugiao.DataBase;
import com.example.quanlylopmaugiao.QLGiaoVien.GiaoVien;
import com.example.quanlylopmaugiao.QLGiaoVien.GiaoVienAdapter;
import com.example.quanlylopmaugiao.QLGiaoVien.QLGiaoVien;
import com.example.quanlylopmaugiao.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class QLHocSinh extends AppCompatActivity {
    String DATABASE_NAME = "QLHS_MamNon.db";
    SQLiteDatabase database;
    ImageView imgChonAnh;

    Toolbar toolbar;
    ActionBar actionBar;

    ListView lv_HS;
    HocSinhAdapter adapter;
    ArrayList<HocSinh> dss = new ArrayList<>();

    final int REQUEST_CHOOES_PHOTO = 321;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_l_hoc_sinh);
        Init();
        ReadData();
        Events();
        registerForContextMenu(lv_HS);
    }
    private void Events() {
        lv_HS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
            }
        });
    }

    private void ReadData() {
        database = DataBase.initDatabase(QLHocSinh.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM HocSinh", null);
        dss.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String Ma = cursor.getString(0);
            String Ten = cursor.getString(1);
            int NamSinh = cursor.getInt(2);
            int GT = cursor.getInt(3);
            byte[] Hinh = cursor.getBlob(4);
            String MaLop = cursor.getString(5);
            dss.add(new HocSinh(Ma, Ten, NamSinh, GT, Hinh,MaLop));
        }
        adapter.notifyDataSetChanged();
    }

    private void Init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quản lý học sinh");
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        lv_HS = findViewById(R.id.lv_HS);
        adapter = new HocSinhAdapter(R.layout.item_hocsinh, QLHocSinh.this, dss);
        lv_HS.setAdapter(adapter);
        lv_HS.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos=i;
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_gv, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://Click nút back trên appBar
//                onBackPressed();
                finish();
//                Toast.makeText(QL_ThucDon.this,"Back!",Toast.LENGTH_LONG).show();
            case R.id.mn_LamMoi:
                ReadData();
                finish();
            case R.id.mn_ThemGV:
                final Dialog dialog = new Dialog(QLHocSinh.this);
                dialog.setContentView(R.layout.hocsinh_them);
                dialog.setCanceledOnTouchOutside(false);//Click ra ngoài không bị mất


                final EditText Ma = dialog.findViewById(R.id.edt_add_MaHS);
                final EditText Ten = dialog.findViewById(R.id.edt_add_TenHS);
                final EditText NS = dialog.findViewById(R.id.edt_add_NamSinhHS);
                final EditText ML = dialog.findViewById(R.id.edt_add_MaLop);
                final RadioButton rd_Nam, rd_Nu;
                rd_Nam = dialog.findViewById(R.id.radio_Nam);
                rd_Nu = dialog.findViewById(R.id.radio_Nu);

                Button btn_add = dialog.findViewById(R.id.btn_add_HS);
                Button btn_Huy = dialog.findViewById(R.id.btn_Huy_HS);

                imgChonAnh = dialog.findViewById(R.id.image_add_HS);

                btn_Huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                imgChonAnh.setOnClickListener(new View.OnClickListener() {//sự kiện thêm ảnh
                    @Override
                    public void onClick(View v) {
                        Chooes_Photo();//Chọn ảnh từ máy
                    }
                });
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ma, ten,ml;
                        int ns, gt = 0;
                        ma = Ma.getText().toString().trim();
                        ten = Ten.getText().toString().trim();
                        ns = Integer.parseInt(NS.getText().toString().trim());
                        ml = ML.getText().toString().trim();
                        if (rd_Nam.isChecked() == true) {
                            gt = 0;
                        } else {
                            gt = 1;
                        }
                        byte[] anh = getByteArrayFromImageView(imgChonAnh);
                        if (checkNull(ma, ten, ns) == true) {
                            if (checkMasp(ma) == true) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("MaHS", ma);
                                contentValues.put("TenHS", ten);
                                contentValues.put("NamSinh", ns);
                                contentValues.put("GioiTinh", gt);
                                contentValues.put("Anh", anh);
                                contentValues.put("MaLop", ml);

                                SQLiteDatabase database = DataBase.initDatabase(QLHocSinh.this, DATABASE_NAME);
                                database.insert("HocSinh", null, contentValues);
                                finish();
                            } else {
                                Toast.makeText(QLHocSinh.this, "Mã này đã tồn tại", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(QLHocSinh.this, "Không để thông tin trống", Toast.LENGTH_LONG).show();
                        }
                        ReadData();
                        Toast.makeText(QLHocSinh.this, "Thêm thành công!", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });

                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mn_Sua:
                final Dialog dialog = new Dialog(QLHocSinh.this);
                dialog.setContentView(R.layout.hocsinh_sua);
                dialog.setCanceledOnTouchOutside(false);//Click ra ngoài không bị mất

                final EditText Ten = dialog.findViewById(R.id.edt_add_TenGV);
                final EditText NS = dialog.findViewById(R.id.edt_add_NamSinhGV);
                final EditText ML = dialog.findViewById(R.id.edt_add_MaLop);
                final RadioButton rd_Nam, rd_Nu;
                rd_Nam = dialog.findViewById(R.id.radio_Nam);
                rd_Nu = dialog.findViewById(R.id.radio_Nu);

                Ten.setText(dss.get(pos).getTenHS());
                NS.setText(dss.get(pos).getNamSinh() + "");
                if (dss.get(pos).getGioiTinh() == 0) {
                    rd_Nam.setChecked(true);
                } else {
                    rd_Nu.setChecked(true);
                }
                ML.setText(dss.get(pos).getMaLop());


                Button btn_add = dialog.findViewById(R.id.btn_add_HS);
                Button btn_Huy = dialog.findViewById(R.id.btn_Huy_HS);

                imgChonAnh = dialog.findViewById(R.id.image_add_HS);

                byte[] hinh = dss.get(pos).getAnh();
                Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
                imgChonAnh.setImageBitmap(bitmap);

                btn_Huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                imgChonAnh.setOnClickListener(new View.OnClickListener() {//sự kiện thêm ảnh
                    @Override
                    public void onClick(View v) {
                        Chooes_Photo();//Chọn ảnh từ máy
                    }
                });
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(QLHocSinh.this);
                        alert.setTitle("Sửa thông tin học sinh!");
                        alert.setMessage("Thực hiện thay đổi?");
                        alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String malop, ten;
                                int ns, gt = 1;
                                ten = Ten.getText().toString().trim();
                                ns = Integer.parseInt(NS.getText().toString().trim());
                                if (rd_Nam.isChecked() == true) {
                                    gt = 0;
                                } else if (rd_Nu.isChecked() == true) {
                                    gt = 1;
                                }
                                malop = ML.getText().toString().trim();
                                byte[] anh = getByteArrayFromImageView(imgChonAnh);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("TenHS", ten);
                                contentValues.put("NamSinh", ns);
                                contentValues.put("GioiTinh", gt);
                                contentValues.put("Anh", anh);
                                contentValues.put("MaLop", malop);

                                database = DataBase.initDatabase(QLHocSinh.this, DATABASE_NAME);
                                database.update("HocSinh", contentValues, "MaHS=?", new String[]{dss.get(pos).getMaHS() + ""});

                                ReadData();//Load lại dữ liệu
                                Toast.makeText(QLHocSinh.this, "Sửa thành công!", Toast.LENGTH_LONG).show();
                            }
                        });
                        alert.show();
                        dialog.cancel();
                    }
                });

                dialog.show();
                return true;
            case R.id.mn_Xoa:
                //        Toast.makeText(QL_ThucDon.this,id+"",Toast.LENGTH_LONG).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(QLHocSinh.this);
                alert.setMessage("Bạn có muốn xóa thông tin học sinh?");
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = dss.get(pos).getMaHS();
                        database = DataBase.initDatabase(QLHocSinh.this, DATABASE_NAME);
                        database.delete("HocSinh", "MaHS=?", new String[]{id + ""});
                        ReadData();
                        Toast.makeText(QLHocSinh.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void Chooes_Photo() {//Chọn hình
        Intent it = new Intent(Intent.ACTION_PICK);
        it.setType("image/*");
        startActivityForResult(it, REQUEST_CHOOES_PHOTO);

    }

    private boolean checkNull(String ma, String ten, int ns) {
        if (("").equals(ma)
                || ("").equals(ten)
                || ("").equals(ns)
        ) {
            return false;
        } else return true;
    }

    private boolean checkMasp(String ma) {
        database = DataBase.initDatabase(QLHocSinh.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM HocSinh WHERE MaHS=?", new String[]{ma});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    private byte[] getByteArrayFromImageView(ImageView imgMatHang) {
        BitmapDrawable drawable = (BitmapDrawable) imgMatHang.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOES_PHOTO) {
                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgChonAnh.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}