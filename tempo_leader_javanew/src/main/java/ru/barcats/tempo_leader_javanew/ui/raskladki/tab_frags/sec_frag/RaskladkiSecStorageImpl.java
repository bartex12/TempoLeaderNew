package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;


public class RaskladkiSecStorageImpl implements RaskladkiSecStorage {

    public static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;
    private ArrayList<String> data;
    private Application application;

    public RaskladkiSecStorageImpl( Application application) {
        this.application = application;
        tempDBHelper = new TempDBHelper(application);
        database =  tempDBHelper.getWritableDatabase();
        data = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getRaskladkiList() {
        //если в базе нет данных - пишем файл автосохранение_секундомера
        tempDBHelper.createDefaultSetIfNeed(database);
        //получаем все файлы для вкладки секундомера
        data = TabFile.getArrayListFilesWhithType(database, P.TYPE_TIMEMETER);
        return data;
    }

    @Override
    public ArrayList<String> deleteItem(String fileName) {
        Log.d(TAG, "RaskladkiSecStorageImpl deleteItem");
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileName);
        //Удаление записи из базы данных
        tempDBHelper.deleteFileAndSets(database, fileId);
        Log.d(TAG, "deleteItem удален файл  " +fileName + " id = " +fileId );
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database, P.TYPE_TIMEMETER);
        return data;
    }

    @Override
    public ArrayList<String> moveItemInTemp(String fileName) {
        Log.d(TAG, "RaskladkiSecStorageImpl moveItemInTemp");
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileName);
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TabFile.COLUMN_TYPE_FROM, P.TYPE_TEMPOLEADER);
        database.update(TabFile.TABLE_NAME, updatedValues,
                    TabFile._ID + "=" + fileId, null);
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database, P.TYPE_TIMEMETER);
        return data;
    }

    @Override
    public ArrayList<String> moveItemInLike(String fileName) {
        Log.d(TAG, "RaskladkiSecStorageImpl moveItemInLike");
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileName);
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TabFile.COLUMN_TYPE_FROM, P.TYPE_LIKE);
        database.update(TabFile.TABLE_NAME, updatedValues,
                TabFile._ID + "=" + fileId, null);
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database, P.TYPE_TIMEMETER);
        return data;
    }

    @Override
    public ArrayList<String> doChangeAction(String fileNameOld, String fileNameNew) {
        Log.d(TAG, "RaskladkiSecStorageImpl doChangeAction");
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileNameOld);
        //изменяем имя файла
         TabFile.updateFileName(database, fileNameNew, fileId);
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database, P.TYPE_TIMEMETER);
        return data;
    }

    @Override
    public long getIdFromFileName(String fileName) {
        return TabFile.getIdFromFileName(database,fileName);
    }
}
