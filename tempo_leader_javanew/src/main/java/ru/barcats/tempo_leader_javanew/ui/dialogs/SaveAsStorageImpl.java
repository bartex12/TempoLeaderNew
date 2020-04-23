package ru.barcats.tempo_leader_javanew.ui.dialogs;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;

public class SaveAsStorageImpl implements SaveAsStorage {

    private static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;

    public SaveAsStorageImpl(Application application) {
        tempDBHelper = new TempDBHelper(application);
        database = tempDBHelper.getWritableDatabase();
    }

    //если имя не изменилось, удаляем копию файла - она больше не нужна-
    //и возвращаем fileId старого файла
    //если имя будет другое - меняем имя старого файла с новыми данными на новое имя
    //а затем меняем имя копии старого файла на имя старого файла
    //и уже ничего не удаляем, так как копия используется, а возвращаем fileId с новыми данными
    @Override
    public long saveAsFile(String oldFileName, String newFileName, long fileOldIdCopy) {
        if (oldFileName.equals(newFileName)){
            Log.d(TAG, " SaveAsStorageImpl oldFileName.equals ");
            //удаляем копию файла - она больше не нужна
            tempDBHelper.deleteFileAndSets(database, fileOldIdCopy);
            return TabFile.getIdFromFileName(database, oldFileName);
        }else {
            Log.d(TAG, " SaveAsStorageImpl oldFileName.NOT equals ");
            //получаем id старого файла
            long fileId = TabFile.getIdFromFileName(database, oldFileName);
            Log.d(TAG, "SaverFragment fileId = " + fileId + " oldFileName" + oldFileName);
            //меняем имя старого файла с новыми данными на новое имя
            TabFile.updateFileName(database, newFileName, fileId);
            //меняем имя копии на имя старого файла
            TabFile.updateFileName(database, oldFileName, fileOldIdCopy);
            return fileId;
        }
    }
}
