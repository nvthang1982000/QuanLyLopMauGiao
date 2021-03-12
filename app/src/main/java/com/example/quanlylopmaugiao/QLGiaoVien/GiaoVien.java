package com.example.quanlylopmaugiao.QLGiaoVien;

public class GiaoVien {
    private String MaGV, TenGV;
    private int NamSinh, GioiTinh;
    private byte[] Anh;

    public GiaoVien(String maGV, String tenGV, int namSinh, int gioiTinh, byte[] anh) {
        MaGV = maGV;
        TenGV = tenGV;
        NamSinh = namSinh;
        GioiTinh = gioiTinh;
        Anh = anh;
    }

    public String getMaGV() {
        return MaGV;
    }

    public void setMaGV(String maGV) {
        MaGV = maGV;
    }

    public String getTenGV() {
        return TenGV;
    }

    public void setTenGV(String tenGV) {
        TenGV = tenGV;
    }

    public int getNamSinh() {
        return NamSinh;
    }

    public void setNamSinh(int namSinh) {
        NamSinh = namSinh;
    }

    public int getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(int gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public byte[] getAnh() {
        return Anh;
    }

    public void setAnh(byte[] anh) {
        Anh = anh;
    }
}
