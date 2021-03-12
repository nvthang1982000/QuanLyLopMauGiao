package com.example.quanlylopmaugiao.QLGiaoVien;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Database;
import androidx.room.Insert;

import com.example.quanlylopmaugiao.DataBase;
import com.example.quanlylopmaugiao.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class QLGiaoVien extends AppCompatActivity {
    String DATABASE_NAME = "QLHS_MamNon.db";
    SQLiteDatabase database;
    ImageView imgChonAnh;

    Toolbar toolbar;
    ActionBar actionBar;

    ListView lv_GV;
    GiaoVienAdapter adapter;
    ArrayList<GiaoVien> ds = new ArrayList<>();

    final int REQUEST_CHOOES_PHOTO = 321;
    int pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qlgiaovien_layout);
        Init();
        ReadData();
        Events();
        registerForContextMenu(lv_GV);
    }

    private void Events() {
        lv_GV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
            }
        });
    }

    private void ReadData() {
        database = DataBase.initDatabase(QLGiaoVien.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM GiaoVien", null);
        ds.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String Ma = cursor.getString(0);
            String Ten = cursor.getString(1);
            int NamSinh = cursor.getInt(2);
            int GT = cursor.getInt(3);
            byte[] Hinh = cursor.getBlob(4);
            ds.add(new GiaoVien(Ma, Ten, NamSinh, GT, Hinh));
        }
        adapter.notifyDataSetChanged();
    }

    private void Init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quản lý giáo viên");
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        lv_GV = findViewById(R.id.lv_GV);
        adapter = new GiaoVienAdapter(R.layout.item_giaovien, QLGiaoVien.this, ds);
        lv_GV.setAdapter(adapter);
        lv_GV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                final Dialog dialog = new Dialog(QLGiaoVien.this);
                dialog.setContentView(R.layout.giaovien_them);
                dialog.setCanceledOnTouchOutside(false);//Click ra ngoài không bị mất


                final EditText Ma = dialog.findViewById(R.id.edt_add_MaGV);
                final EditText Ten = dialog.findViewById(R.id.edt_add_TenGV);
                final EditText NS = dialog.findViewById(R.id.edt_add_NamSinhGV);
                final RadioButton rd_Nam, rd_Nu;
                rd_Nam = dialog.findViewById(R.id.radio_Nam);
                rd_Nu = dialog.findViewById(R.id.radio_Nu);

                Button btn_add = dialog.findViewById(R.id.btn_add_GV);
                Button btn_Huy = dialog.findViewById(R.id.btn_Huy_GV);

                imgChonAnh = dialog.findViewById(R.id.image_add_GV);

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
                        String ma, ten;
                        int ns, gt = 0;
                        ma = Ma.getText().toString().trim();
                        ten = Ten.getText().toString().trim();
                        ns = Integer.parseInt(NS.getText().toString().trim());
                        if (rd_Nam.isChecked() == true) {
                            gt = 0;
                        } else {
                            gt = 1;
                        }
                        byte[] anh = getByteArrayFromImageView(imgChonAnh);
                        if (checkNull(ma, ten, ns) == true) {
                            if (checkMasp(ma) == true) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("MaGV", ma);
                                contentValues.put("TenGV", ten);
                                contentValues.put("NamSinh", ns);
                                contentValues.put("GioiTinh", gt);
                                contentValues.put("Anh", anh);

                                SQLiteDatabase database = DataBase.initDatabase(QLGiaoVien.this, DATABASE_NAME);
                                database.insert("GiaoVien", null, contentValues);
                                finish();
                            } else {
                                Toast.makeText(QLGiaoVien.this, "Mã này đã tồn tại", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(QLGiaoVien.this, "Không để thông tin trống", Toast.LENGTH_LONG).show();
                        }
                        ReadData();
                        Toast.makeText(QLGiaoVien.this, "Thêm thành công!", Toast.LENGTH_LONG).show();
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
                final Dialog dialog = new Dialog(QLGiaoVien.this);
                dialog.setContentView(R.layout.giaovien_sua);
                dialog.setCanceledOnTouchOutside(false);//Click ra ngoài không bị mất

                final EditText Ten = dialog.findViewById(R.id.edt_add_TenGV);
                final EditText NS = dialog.findViewById(R.id.edt_add_NamSinhGV);
                final RadioButton rd_Nam, rd_Nu;
                rd_Nam = dialog.findViewById(R.id.radio_Nam);
                rd_Nu = dialog.findViewById(R.id.radio_Nu);

                Ten.setText(ds.get(pos).getTenGV());
                NS.setText(ds.get(pos).getNamSinh() + "");
                if (ds.get(pos).getGioiTinh() == 0) {
                    rd_Nam.setChecked(true);
                } else {
                    rd_Nu.setChecked(true);
                }


                Button btn_add = dialog.findViewById(R.id.btn_add_GV);
                Button btn_Huy = dialog.findViewById(R.id.btn_Huy_GV);

                imgChonAnh = dialog.findViewById(R.id.image_add_GV);

                byte[] hinh = ds.get(pos).getAnh();
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(QLGiaoVien.this);
                        alert.setTitle("Sửa thông tin giáo viên!");
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
                                String ma, ten;
                                int ns, gt = 1;
                                ten = Ten.getText().toString().trim();
                                ns = Integer.parseInt(NS.getText().toString().trim());
                                if (rd_Nam.isChecked() == true) {
                                    gt = 0;
                                } else if (rd_Nu.isChecked() == true) {
                                    gt = 1;
                                }
                                byte[] anh = getByteArrayFromImageView(imgChonAnh);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("TenGV", ten);
                                contentValues.put("NamSinh", ns);
                                contentValues.put("GioiTinh", gt);
                                contentValues.put("Anh", anh);

                                database = DataBase.initDatabase(QLGiaoVien.this, DATABASE_NAME);
                                database.update("GiaoVien", contentValues, "MaGV=?", new String[]{ds.get(pos).getMaGV() + ""});

                                ReadData();//Load lại dữ liệu
                                Toast.makeText(QLGiaoVien.this, "Sửa thành công!", Toast.LENGTH_LONG).show();
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
                AlertDialog.Builder alert = new AlertDialog.Builder(QLGiaoVien.this);
                alert.setMessage("Bạn có muốn xóa thông tin giáo viên?");
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = ds.get(pos).getMaGV();
                        database = DataBase.initDatabase(QLGiaoVien.this, DATABASE_NAME);
                        database.delete("GiaoVien", "MaGV=?", new String[]{id + ""});
                        ReadData();
                        Toast.makeText(QLGiaoVien.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
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
        database = DataBase.initDatabase(QLGiaoVien.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM GiaoVien WHERE MaGV=?", new String[]{ma});
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
