package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.like_frag;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.P;


public class RaskladkiLikeStorageImpl implements RaskladkiLikeStorage {

    public static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;
    private ArrayList<String> data;
    private Application application;

    public RaskladkiLikeStorageImpl(Application application) {
        this.application = application;
        tempDBHelper = new TempDBHelper(application);
        database =  tempDBHelper.getWritableDatabase();
        data = new ArrayList<>();
    }

    //получаем все файлы для вкладки темполидер
    @Override
    public ArrayList<String> getRaskladkiList() {
        //получаем все файлы для вкладки темполидер
        data = TabFile.getArrayListFilesWhithType(database,P.TYPE_LIKE);
        return data;
    }

    //удаляем строку списка файлов раскладок
    @Override
    public ArrayList<String> deleteItem(final String fileName) {
        Log.d(TAG, "RaskladkiLikeStorageImpl deleteItem");
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileName);
        //Удаление записи из базы данных
        tempDBHelper.deleteFileAndSets(database, fileId);
        Log.d(TAG, "deleteDialog удален файл  " +fileName + " id = " +fileId );
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database,P.TYPE_LIKE);
        return data;
    }

    //перемещение из избранного в темполидер
    @Override
    public ArrayList<String> moveItemInTemp(String fileName) {
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileName);
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TabFile.COLUMN_TYPE_FROM, P.TYPE_TEMPOLEADER);
        database.update(TabFile.TABLE_NAME, updatedValues,
                TabFile._ID + "=" + fileId, null);
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database,P.TYPE_LIKE);
        return data;
    }

    @Override
    public ArrayList<String> moveItemInSec(String fileName) {
        //получаем id по имени
        long fileId = TabFile.getIdFromFileName(database,fileName);
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TabFile.COLUMN_TYPE_FROM, P.TYPE_TIMEMETER);
        database.update(TabFile.TABLE_NAME, updatedValues,
                TabFile._ID + "=" + fileId, null);
        //получаем обновлённый список данных и отдаём его в LiveData
        data = TabFile.getArrayListFilesWhithType(database,P.TYPE_LIKE);
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
        data = TabFile.getArrayListFilesWhithType(database, P.TYPE_LIKE);
        return data;
    }

    @Override
    public long getIdFromFileName(String fileName) {
        return TabFile.getIdFromFileName(database,fileName);
    }
}
