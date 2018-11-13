package com.libirsoft.workhard;

/**
 * Created by Yuşa Kopuz on 13.12.2016.
 */

public class Kisi {
    private String ders;
    private int soru;
    private long tarih;
    private  long Id;

    Kisi(){



    }
    public Kisi(String ders, int soru, long tarih) {
        this.setDers(ders);
        this.setSoru(soru);
        this.setTarih(tarih);
    }
    public  void setId(long id){Id=id; }
    public long getId(){return Id;}

    public String getDers() {
        return ders;
    }

    public void setDers(String ders) {
        this.ders = ders;
    }

    public int getSoru() {
        return soru;
    }

    public void setSoru(int soru) {
        this.soru = soru;
    }

    public long getTarih() {
        return tarih;
    }

    public void setTarih(long tarih) {
        this.tarih = tarih;
    }
}
