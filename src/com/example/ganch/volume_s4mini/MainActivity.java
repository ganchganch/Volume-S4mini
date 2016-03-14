package com.example.ganch.volume_s4mini;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private final static String file_name = "/system/etc/snd_soc_msm/snd_soc_msm_2x"; // имя файла
    private final static String file_name_out = "/system/etc/snd_soc_msm/snd_soc_msm_2x.bak"; // имя файла
    private final static String file_name_local="snd_soc_msm_2x";
    private final static String name_speaker="\"Speaker\"";
    private final static String name_headphones="\"Headphones\"";
    private final static String name_rx4_digital="RX4 Digital Volume";
    private final static String name_rx1_digital="RX1 Digital Volume";
    private final static String name_rx2_digital="RX2 Digital Volume";
    private String speaker;
    private String headphone1;
    private String headphone2;
    private boolean flag=false;
    private EditText mEditText;
    private EditText curdinamic;
    private EditText curhead1;
    private EditText curhead2;
    private EditText head1;
    private EditText head2;
    private Button btn_save;
    private Button btn_exit;
    StringBuilder builder;
    AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.editText);
        curdinamic=(EditText) findViewById(R.id.curVolDinamic);
        curdinamic.setEnabled(false);
        curhead1=(EditText) findViewById(R.id.curVolHead1);
        curhead1.setEnabled(false);
        curhead2=(EditText) findViewById(R.id.curVolHead2);
        curhead2.setEnabled(false);
        head1=(EditText) findViewById(R.id.VolHead1);
        head2=(EditText) findViewById(R.id.VolHead2);
        btn_save=(Button)findViewById(R.id.save_btn);
        btn_exit=(Button) findViewById(R.id.exit_btn);
        btn_save.setText(R.string.action_save);
        btn_exit.setText(R.string.exit_mes);
        StringBuilder builder;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ad = new AlertDialog.Builder(this);
        // заголовок
        //ad.setTitle(R.string.exit);
        // сообщение
        ad.setMessage(R.string.reboot_mes);
        // иконка
        ad.setIcon(android.R.drawable.ic_dialog_info);
        // кнопка положительного ответа
        ad.setPositiveButton(R.string.exit_mes, myClickListener);
        // кнопка отрицательного ответа
        // ad.setNegativeButton(R.string.no, myClickListener);
        // кнопка нейтрального ответа
        //ad.setNeutralButton(R.string.cancel, myClickListener);

        openFile(file_name);

    }
    public void onClickBtn(View v) {
        switch(v.getId())   {
            case R.id.save_btn:
                saveFile(file_name_local);
                break;
            case R.id.exit_btn:
                finish();
                break;

        }
    }


    public void onBackPressed() {
        finish();
    }
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    //saveData();
                    finish();
                    break;
                // негаитвная кнопка
               // case Dialog.BUTTON_NEGATIVE:
                //    finish();
                //    break;
                // нейтральная кнопка
               // case Dialog.BUTTON_NEUTRAL:
                 //   break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_save:
                saveFile(file_name_local);
                return true;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    // Метод для открытия файла
    int strt,end;
    private void openFile(String fileName) {

        try {
               BufferedReader br = new BufferedReader(new FileReader(fileName));
                String line,temp;
                while ((line = br.readLine()) != null) {
                    if(line.indexOf(name_speaker)!=-1){
                        flag=true;
                    }
                    if(flag&&(line.indexOf(name_rx4_digital)!=-1)) {
                       flag=false;
                        speaker=line;
                        strt=speaker.lastIndexOf(':');
                        end=speaker.lastIndexOf('%');
                        temp=speaker.substring(strt + 1, end)+'%';
                        curdinamic.setText(speaker.substring(strt + 1, end));
                        mEditText.setText("72");
                        speaker=line.replace(temp, "");
                    }
                    if(line.indexOf(name_headphones)!=-1){
                        flag=true;
                    }
                    if(flag&&(line.indexOf(name_rx1_digital)!=-1)) {
                        headphone1 = line;
                        strt = headphone1.lastIndexOf(':');
                        end = headphone1.lastIndexOf('%');
                        temp=headphone1.substring(strt + 1, end)+'%';
                        curhead1.setText(headphone1.substring(strt + 1, end));
                        head1.setText("62.9");
                        headphone1=line.replace(temp, "");
                    }
                    if(flag&&(line.indexOf(name_rx2_digital)!=-1)) {
                        flag=false;
                        headphone2 = line;
                        strt = headphone2.lastIndexOf(':');
                        end = headphone2.lastIndexOf('%');
                        temp=headphone2.substring(strt + 1, end)+'%';
                        curhead2.setText(headphone2.substring(strt + 1, end));
                        head2.setText("62.9");
                        headphone2=line.replace(temp, "");
                    }
                }
                br.close();
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // Метод для сохранения файла
    private void make_changes(String fileName) {

        String str;
        str=speaker+mEditText.getText()+'%';
        speaker=str;
        str=headphone1+head1.getText()+'%';
        headphone1=str;
        str=headphone2+head2.getText()+'%';
        headphone2=str;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line,temp;
            builder = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if(line.indexOf(name_speaker)!=-1){
                    flag=true;
                }
                if(flag&&(line.indexOf(name_rx4_digital) != -1)) {
                   flag=false;
                    line=speaker;

                }
                if(line.indexOf(name_headphones)!=-1){
                    flag=true;
                }
                if(flag&&(line.indexOf(name_rx1_digital)!=-1)) {
                    line=headphone1;
                }
                if(flag&&(line.indexOf(name_rx2_digital)!=-1)) {
                    flag=false;
                    line=headphone2;
                }
                builder.append(line + "\n");
            }

            br.close();

        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void saveFile(String fileName) {
        Process process = null;
        File file;
        make_changes(file_name);
        try {
            file = new File(getApplicationContext().getFilesDir(), fileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(builder.toString());
            bw.flush();
            bw.close();
            String cmd_cop="cp "+file.getAbsolutePath()+" /system/etc/snd_soc_msm/\n";
            process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("mount -o remount,rw /system\n" );
            os.writeBytes("cp "+file_name+" "+file_name_out+"\n");
            os.writeBytes(cmd_cop);
            os.writeBytes("chmod 644 "+file_name+"\n");
            os.writeBytes("mount -o remount,ro /system\n" );
            os.writeBytes("exit\n");
            os.flush();
           ad.show();
            //process.waitFor();
        }
        catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            return;
        }


    }
}

