package com.libirsoft.workhard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuşa Kopuz on 13.12.2016.
 */

public class VeriTabanı extends SQLiteOpenHelper {
    private   static String VERİTABANİ_NAME="veritabani";
    private   static int VERSİON=1;
    private   static String TABLE_NAME="Ders_takip_tablosu";

    private static String ID="_İD";
    private static String DERS="Ders";
    private static String SARIYSAYISI="SARU_SAYISI";
    private static String TARİH="TARİH";

    public VeriTabanı(Context context) {
        super(context,VERİTABANİ_NAME, null,VERSİON);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String Table_create="create table if not exists "+TABLE_NAME+
                "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                DERS+" TEXT, "+
                SARIYSAYISI+" INTEGER NOT NULL, "+
                TARİH+" INTEGER NOT NULL );";

        db.execSQL(Table_create);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long Kayitekle(Kisi kisi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DERS,kisi.getDers());
        cv.put(SARIYSAYISI,kisi.getSoru());
        cv.put(TARİH,kisi.getTarih());
      long id=  db.insert(TABLE_NAME,null,cv);
        db.close();
        return id;
    }

    public List<Kisi> Tumkayıtlar() {
        SQLiteDatabase db =this.getReadableDatabase();
        String []stunlar=new String[]{DERS,SARIYSAYISI,TARİH,ID};
        Cursor c=db.query(TABLE_NAME,stunlar,null,null,null,null,TARİH+" desc ");
        int dersno=c.getColumnIndex(DERS);
        int sarısayısıno=c.getColumnIndex(SARIYSAYISI);
        int tarihno=c.getColumnIndex(TARİH);
        int idno=c.getColumnIndex(ID);
List<Kisi> kisilist=new ArrayList<Kisi>();
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            Kisi kisi = new Kisi();
            kisi.setDers(c.getString(dersno));
            kisi.setSoru(c.getInt(sarısayısıno));
            kisi.setTarih(c.getLong(tarihno));
            kisi.setId(c.getLong(idno));
            kisilist.add(kisi);


        }
db.close();



return kisilist;
    }

    public void Sil(long id) {
        SQLiteDatabase db =this.getWritableDatabase();
db.delete(TABLE_NAME,ID+" = "+id,null);
db.close();

    }
    public void Güncelle(long id,long tarih,String ders,int soru){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DERS,ders);
        cv.put(TARİH,tarih);
        cv.put(SARIYSAYISI,soru);
        db.update(TABLE_NAME,cv,ID+" = "+id,null);
        db.close();

    }

    public List<Kisi> IkiTariharası(long tarih_ilk, long tarih_son) {
        SQLiteDatabase db =this.getReadableDatabase();
        String []stunlar=new String[]{DERS,SARIYSAYISI,TARİH,ID};
        String []Tarihler=new String[]{String.valueOf(tarih_ilk),String.valueOf(tarih_son)};
        Cursor c=db.query(TABLE_NAME,stunlar," BETWEEN ? AND ?",Tarihler,null,null,TARİH+" desc ");
        int dersno=c.getColumnIndex(DERS);
        int sarısayısıno=c.getColumnIndex(SARIYSAYISI);
        int tarihno=c.getColumnIndex(TARİH);
        int idno=c.getColumnIndex(ID);
        List<Kisi> kisilist=new ArrayList<Kisi>();
       /* for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            Kisi kisi = new Kisi();
            kisi.setDers(c.getString(dersno));
            kisi.setSoru(c.getInt(sarısayısıno));
            kisi.setTarih(c.getLong(tarihno));
            kisi.setId(c.getLong(idno));
            kisilist.add(kisi);


        }
        */
        if (c.moveToFirst()){
do{
    Kisi kisi = new Kisi();
    kisi.setDers(c.getString(dersno));
    kisi.setSoru(c.getInt(sarısayısıno));
    kisi.setTarih(c.getLong(tarihno));
    kisi.setId(c.getLong(idno));
    kisilist.add(kisi);
}while (c.moveToNext());

        }else
        {
            kisilist=null;
        }
        db.close();



        return kisilist ;
    }
}
