package com.example.quanlylopmaugiao.QLLop;

public class Lop {
    private String MaLop;
    private String TenLop;
    private String GhiChu;
    private String MaGV;

    public Lop(String maLop, String tenLop, String ghiChu) {
        MaLop = maLop;
        TenLop = tenLop;
        GhiChu = ghiChu;
    }

    public Lop(String maLop, String tenLop, String ghiChu, String maGV) {
        MaLop = maLop;
        TenLop = tenLop;
        GhiChu = ghiChu;
        MaGV = maGV;
    }

    public String getMaLop() {
        return MaLop;
    }

    public void setMaLop(String maLop) {
        MaLop = maLop;
    }

    public String getTenLop() {
        return TenLop;
    }

    public void setTenLop(String tenLop) {
        TenLop = tenLop;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public String getMaGV() {
        return MaGV;
    }

    public void setMaGV(String maGV) {
        MaGV = maGV;
    }
}
