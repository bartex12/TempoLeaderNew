package ru.barcats.tempo_leader_javanew.ui.help;

import android.app.Application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ru.barcats.tempo_leader_javanew.R;

public class HelpStorageImpl implements HelpStorage {

    private Application application;

    HelpStorageImpl(Application application){
        this.application = application;
    }

    @Override
    public StringBuilder getText() {
        InputStream iFile = application.getResources().openRawResource(R.raw.help_trener_plus);
        return inputStreamToString(iFile);
    }

    private StringBuilder inputStreamToString(InputStream iFile) {
        StringBuilder strFull = new StringBuilder();
        String str = "";
        try {
            // открываем поток для чтения
            InputStreamReader ir = new InputStreamReader(iFile);
            BufferedReader br = new BufferedReader(ir);
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                //Log.d(TAG, str);
                //Чтобы не было в одну строку, ставим символ новой строки
                strFull.append(str).append("\n");
            }
            //закрываем потоки
            iFile.close();
            ir.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strFull;
    }
}
