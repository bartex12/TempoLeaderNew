package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataFile;

public class DateTimeStorageImpl implements DateTimeStorage {


    public static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;
    private Application application;

    public DateTimeStorageImpl(Application application) {
        tempDBHelper = new TempDBHelper(application);
        database =  tempDBHelper.getWritableDatabase();
        this.application =application;
    }

    @Override
    public String getDateAndTime(String fileName) {
        //получаем id по имени
        final long fileIdOld = TabFile.getIdFromFileName(database,fileName);
        //получаем объект с данными строки с id = fileIdOld из  таблицы TabFile
        final DataFile dataFile = TabFile.getAllFilesData(database, fileIdOld);

        String date = dataFile.getFileNameDate();
        String time=  dataFile.getFileNameTime();
        return date + application.getString(R.string.LowMinus) + time;
    }
}
