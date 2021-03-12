package com.example.quanlylopmaugiao.HocSinh;

public class HocSinh {
    private String MaHS, TenHS,MaLop;
    private int NamSinh, GioiTinh;
    private byte[] Anh;

    public HocSinh(String maHS, String tenHS, int namSinh, int gioiTinh, byte[] anh , String maLop) {
        MaHS = maHS;
        TenHS = tenHS;
        MaLop = maLop;
        NamSinh = namSinh;
        GioiTinh = gioiTinh;
        Anh = anh;
    }

    public String getMaHS() {
        return MaHS;
    }

    public void setMaHS(String maHS) {
        MaHS = maHS;
    }

    public String getTenHS() {
        return TenHS;
    }

    public void setTenHS(String tenHS) {
        TenHS = tenHS;
    }

    public String getMaLop() {
        return MaLop;
    }

    public void setMaLop(String maLop) {
        MaLop = maLop;
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
