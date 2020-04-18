package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags;


import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.P;

public class RaskladkiStorageImpl implements RaskladkiStorage {

    public static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;
    private ArrayList<String> data;

    public RaskladkiStorageImpl(Application application) {
        tempDBHelper = new TempDBHelper(application);
        database =  tempDBHelper.getWritableDatabase();
        data = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getRaskladkiList(String tabType) {
        //получаем все файлы для вкладки темполидер
        data = TabFile.getArrayListFilesWhithType(database,  tabType);
        return data;
    }

    @Override
    public ArrayList<String> deleteItem(String fileName, String tabType) {
        Log.d(TAG, "RaskladkiStorageImpl deleteItem");
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileName);
        //Удаление записи из базы данных
        tempDBHelper.deleteFileAndSets(database, fileId);
        Log.d(TAG, "deleteItem удален файл  " +fileName + " id = " +fileId );
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database,  tabType);
        return data;
    }

    @Override
    public ArrayList<String> moveItemInLike(String fileName) {
        return null;
    }

    @Override
    public ArrayList<String> moveItemInTemp(String fileName) {
        return null;
    }

    @Override
    public ArrayList<String> moveFromTo(String fileName,String from,String to) {
        Log.d(TAG, "RaskladkiTempStorageImpl moveItemInSec");
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileName);
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TabFile.COLUMN_TYPE_FROM, to);
        database.update(TabFile.TABLE_NAME, updatedValues,
                TabFile._ID + "=" + fileId, null);
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database,  from);
        return data;
    }

    @Override
    public ArrayList<String> doChangeAction(
            String fileNameOld, String fileNameNew, String tabType) {
        Log.d(TAG, "RaskladkiStorageImpl doChangeAction");
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileNameOld);
        //изменяем имя файла
        TabFile.updateFileName(database, fileNameNew, fileId);
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database, tabType);
        return data;
    }
}
