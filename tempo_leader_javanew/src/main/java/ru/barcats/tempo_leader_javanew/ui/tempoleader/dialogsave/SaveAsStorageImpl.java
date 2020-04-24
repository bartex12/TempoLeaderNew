package ru.barcats.tempo_leader_javanew.ui.tempoleader.dialogsave;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.dialogsave.SaveAsStorage;

public class SaveAsStorageImpl implements SaveAsStorage {

    private static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;

    public SaveAsStorageImpl(Application application) {
        tempDBHelper = new TempDBHelper(application);
        database = tempDBHelper.getWritableDatabase();
    }

    //если имя не изменилось, - возвращаем fileId старого файла
    //если имя будет другое - 1: меняем имя старого файла с новыми данными на новое имя
    //а затем 2: меняем имя копии старого файла на имя старого файла
    //копию не удаляем, она используется, и 3: возвращаем fileId старого файла с новым именем
    @Override
    public long saveAsFile(String oldFileName, String newFileName, long fileOldIdCopy) {
        if (oldFileName.equals(newFileName)){
            Log.d(TAG, " SaveAsStorageImpl oldFileName.equals ");
            return TabFile.getIdFromFileName(database, oldFileName);
        }else {
            Log.d(TAG, " SaveAsStorageImpl oldFileName.NOT equals ");
            //получаем id старого файла с новыми данными
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
