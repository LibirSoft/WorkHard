package com.libirsoft.workhard;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.libirsoft.workhard.R.id.tarih;
import static com.libirsoft.workhard.R.menu.contex_menu;
import static com.libirsoft.workhard.R.menu.menu_main;

public class Work extends AppCompatActivity {
    private static final int DIALOG_HAKKINDA = 1;
    private static final int DIALOG_DERS = 2;
    private static final int DIALOG_TARIH = 3;
    private static final int DİALOG_AYARLAR = 4;
    int Hedef = 100;
    private ActionMode actionMode;
    private long id;

    TableLayout tablo;
    TextView tx;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        tablo = (TableLayout) findViewById(R.id.tablo);
        tx = (TextView) findViewById(R.id.Tx1);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Hoşgeldin ");
        ab.setSubtitle("Ders Eklemeyi Unutma :)");
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));

        try {
            Listele();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Herhangi Bir Kayıt Bulunamadı", Toast.LENGTH_SHORT).show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Listele() {


        VeriTabanı db = new VeriTabanı(this);
        tablo.removeAllViews();

        List<Kisi> kisilist = new ArrayList<Kisi>();
        kisilist = db.Tumkayıtlar();
        //soru oplama bölümü

        long enkucuk = kisilist.get(kisilist.size() - 1).getTarih();
        long ebuyuk = kisilist.get(0).getTarih();
        Date fark = new Date(ebuyuk - enkucuk);
        int fark_gun = ((fark.getYear() % 70) * 365) + (fark.getMonth() * 30) + (fark.getDate() - 1);
        fark_gun++;
        int toplam_soru = 0;
        for (Kisi kisi : kisilist) {
            toplam_soru = toplam_soru + kisi.getSoru();

        }

        int ortalama_soru = toplam_soru / fark_gun;
        if (ortalama_soru >= 100) {
            tx.setText("Tebrikler Büyük Hayallerine Ulaşmak İçin Doğru Yoldasın.\nOrtalama Çözülen Soru Sayısı " + ortalama_soru);
            tx.setBackgroundColor(Color.parseColor("#ffffff"));
            tx.setTextColor(Color.parseColor("#000000"));


        } else {

            tx.setText("Hey Birazdaha Soru Çözmeyi Dene .\nOrtalama Çözülen Soru Sayısı " + ortalama_soru);
            tx.setBackgroundColor(Color.parseColor("#d50000"));
            tx.setTextColor(Color.parseColor("#ffffff"));

        }

        //-----------------
        for (final Kisi kisi : kisilist) {
            TableRow satir = new TableRow(getApplicationContext());
            satir.setGravity(Gravity.CENTER);
            satir.setOrientation(TableRow.HORIZONTAL);
            TextView tx_Tarih = new TextView(getApplicationContext());
            tx_Tarih.setPadding(2, 2, 2, 2);
            tx_Tarih.setTextColor(Color.WHITE);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(kisi.getTarih());
            tx_Tarih.setText(df.format(date) + "   ");
            tx_Tarih.setTextSize(18);


            TextView tx_Ders = new TextView(getApplicationContext());
            tx_Ders.setPadding(2, 2, 2, 2);
            tx_Ders.setTextColor(Color.WHITE);
            tx_Ders.setText("" + kisi.getDers() + "   ");
            tx_Ders.setTextSize(18);

            TextView tx_Soru = new TextView(getApplicationContext());
            tx_Soru.setPadding(2, 2, 2, 2);
            tx_Soru.setTextColor(Color.WHITE);
            tx_Soru.setText("" + kisi.getSoru() + "   ");
            tx_Soru.setTextSize(18);

            satir.addView(tx_Tarih);
            satir.addView(tx_Ders);
            satir.addView(tx_Soru);

            tablo.addView(satir);

            satir.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    id = kisi.getId();
                    if (actionMode != null) {
                        return false;
                    }
                    MyActionModeCallback callback = new MyActionModeCallback();
                    actionMode = Work.this.startActionMode(callback);
                    v.setSelected(true);

                    return true;
                }
            });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case tarih:
                showDialog(DIALOG_TARIH);
                return true;
            case R.id.ekle:
                showDialog(DIALOG_DERS);

                return true;
            case R.id.paylas:
                paylasmesaj(tx.getText());


                return true;
            case R.id.ayarlar:
                showDialog(DİALOG_AYARLAR);
                return true;

            case R.id.hakkinda:
                showDialog(DIALOG_HAKKINDA);

                return true;
        }
        return super.onOptionsItemSelected(item);


    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {

            case DIALOG_HAKKINDA:
                dialog = new Dialog(this);
                dialog.setTitle("Hakkında");
                dialog.setContentView(R.layout.hakkinda);
                break;

            case DIALOG_DERS:
                dialog = getEkleDialog();

                break;
            case DIALOG_TARIH:
                dialog = getTarihDialog();
                break;
            case DİALOG_AYARLAR:
                dialog = getAyarlaDialog();
                break;
            default:
                dialog = null;
        }
        return dialog;
    }

    private Dialog getAyarlaDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.ayrlar, null);
        final Button uygulabtn = (Button) layout.findViewById(R.id.uygulabtn);
        final EditText ortalamasoru = (EditText) layout.findViewById(R.id.ortalamatxt);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("İstersen Kendini Biraz Daha Zorla!!");
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        uygulabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ortalamasoru.getText().equals("")) {

                    Toast.makeText(getApplicationContext(), "Hopp Bir hedef Belirle", Toast.LENGTH_LONG).show();
                } else {
                    Hedef = Integer.parseInt(ortalamasoru.getText().toString());
                }


            }
        });


        return dialog;
    }

    private Dialog getEkleDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dersekle, null);
        final Button kaydetbtn = (Button) layout.findViewById(R.id.kaydet);
        final Button vazgecbtn = (Button) layout.findViewById(R.id.vazgec);
        final EditText soru = (EditText) layout.findViewById(R.id.et);
        final Spinner sp = (Spinner) layout.findViewById(R.id.spders);
        final DatePicker tarih1 = (DatePicker) layout.findViewById(R.id.datePicker);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hey!! Hedef Belirle ");
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        kaydetbtn.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try {


                    int gun = tarih1.getDayOfMonth();
                    int ay = tarih1.getMonth() + 1;
                    int yil = tarih1.getYear();
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = df.parse(gun + "/" + ay + "/" + yil);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long zaman = date.getTime();
                    int pozisyon = sp.getSelectedItemPosition();
                    String ders = (String) sp.getItemAtPosition(pozisyon);
                    int soru_sayisi = Integer.valueOf(soru.getText().toString());
                    Kisi kisi = new Kisi(ders, soru_sayisi, zaman);
                    VeriTabanı db = new VeriTabanı(getApplicationContext());
                    long id = db.Kayitekle(kisi);
                    if (id == -1) {

                        Toast.makeText(getApplicationContext(), "Hata Oluştu", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Kayıt Başarı İle Tamamlandı.", Toast.LENGTH_SHORT).show();

                        Listele();
                        dialog.dismiss();

                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Soru Sayısı Giriniz", Toast.LENGTH_LONG).show();
                }

            }
        });

        vazgecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        return dialog;
    }

    private void paylasmesaj(CharSequence mesaj) {

        Intent paylasıntent = new Intent(Intent.ACTION_SEND);
        paylasıntent.setType("text/plain");
        paylasıntent.putExtra(Intent.EXTRA_TEXT, mesaj);
        startActivity(Intent.createChooser(paylasıntent, "Paylaş"));


    }

    public Dialog getTarihDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.tarih, null);
        final Button vazgeç = (Button) layout.findViewById(R.id.vazgec);
        final Button getir = (Button) layout.findViewById(R.id.getir);
        final DatePicker aralık1 = (DatePicker) layout.findViewById(R.id.aralık1);
        final DatePicker aralık2 = (DatePicker) layout.findViewById(R.id.aralık2);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        getir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                int gun_ilk = aralık1.getDayOfMonth();
                int ay_ilk = aralık1.getMonth() + 1;
                int yıl_ilk = aralık1.getYear();
                Date date = null;
                try {
                    date = df.parse(gun_ilk + "/" + ay_ilk + "/" + yıl_ilk);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long Tarih_ilk = date.getTime();

                int gun_son=aralık2.getDayOfMonth();
                int ay_son=aralık2.getMonth()+1;
                int yıl_son=aralık2.getYear();

                Date date_son =null;
                try {
                    date = df.parse(gun_son + "/" + ay_son + "/" + yıl_son);
                }
                catch (Exception e){
e.printStackTrace();
                }
long tarih_son=date_son.getTime();
                VeriTabanı db=new VeriTabanı(getApplicationContext());

                List<Kisi> kisilist=new ArrayList<Kisi>();
                kisilist=db.IkiTariharası(Tarih_ilk,tarih_son);

            }
        });


        return dialog;
    }

    class MyActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(contex_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {

                case R.id.sil:
                    VeriTabanı db = new VeriTabanı(getApplicationContext());
                    db.Sil(id);
                    Listele();

                    mode.finish();
                    return true;
                case R.id.duzenle:

                    mode.finish();
                    return true;
                default:
                    return false;

            }


        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    }


}