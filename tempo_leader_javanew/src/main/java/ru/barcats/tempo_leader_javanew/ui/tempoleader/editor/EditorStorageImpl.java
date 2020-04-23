package ru.barcats.tempo_leader_javanew.ui.tempoleader.editor;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

public class EditorStorageImpl implements EditorStorage {

    private static final String TAG = "33333";
    private  String endName = "copy";  //добавка к имени в копии файла
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;

    public EditorStorageImpl(Application application) {
        tempDBHelper = new TempDBHelper(application);
        database =  tempDBHelper.getWritableDatabase();
    }

    //получение раскладки по имени файла
    @Override
    public ArrayList<DataSet> getDataSet(String fileName) {
        //получаем id записи с таким именем
        long fileId = TabFile.getIdFromFileName (database, fileName);
        Log.d(TAG,"EditorStorageImpl  имя =" + fileName + "  Id = " + fileId );
        return TabSet.getAllSetFragments(database, fileId);
    }

    //редактирование файла по кнопкам
    @Override
    public ArrayList<DataSet> minus5Action( String fileName,float deltaTime, int countReps,
                                            boolean redactTime,  boolean isChecked, int position) {
        //получаем id записи с таким именем
        long fileId = TabFile.getIdFromFileName (database, fileName);
        Log.d(TAG,"EditorStorageImpl  имя =" + fileName + "  Id = " + fileId );
        //количество фрагментов подхода
        int countOfSet =TabSet.getSetFragmentsCount( database,fileId);

        //пересчитываем раскладку на deltaTime процентов времени или countReps раз
        reductAction(deltaTime,countReps, redactTime, isChecked, countOfSet, fileId , position);

        return TabSet.getAllSetFragments(database, fileId);
    }

    //создание копии файла перед редактированием
    @Override
    public long getCopyFile(String fileName) {

        //получаем id файла
       long fileId = TabFile.getIdFromFileName(database, fileName);
        //создаём и записываем в базу копию файла на случай отмены изменений
        return TabSet.createFileCopy(database, fileName, fileId, endName);
    }

    //отмена внесённых при редактировании изменений
    @Override
    public ArrayList<DataSet> revertEdit(String fileName, long fileIdCopy) {

        Log.d(TAG,"EditorStorageImpl  имя =" + fileName + "  fileIdCopy = " + fileIdCopy );
        //получаем id записи с таким именем
        long fileId = TabFile.getIdFromFileName (database, fileName);
        Log.d(TAG,"EditorStorageImpl  имя =" + fileName + "  fileId = " + fileId );
        //удаляем изменённый при редактировании файл - теперь такого имени нет
        tempDBHelper.deleteFileAndSets(database, fileId);
        //теперь первоначальный файл содержится в копии
        //изменяем имя у копии файла на первоначальное имя
        TabFile.updateFileName(database, fileName,fileIdCopy);
        Log.d(TAG,"EditorStorageImpl  теперь имя стало=" +
                TabFile.getFileNameFromTabFile(database, fileIdCopy) );
        // возвращаем данные для обновления адаптера
        return  TabSet.getAllSetFragments(database, fileIdCopy);
    }

    @Override
    public void clearCopyFile(long fileIdCopy) {
        //удаляем копию файла
        tempDBHelper.deleteFileAndSets(database, fileIdCopy);
    }

    // собственно редактирование
    private void reductAction(float ff, int ii, boolean redactTime, boolean isChecked,
                                int countOfSet, long fileId, int position){
        //если редактируем время
        if (redactTime){
            if (isChecked){   //если сразу во всех строках
                //обновляем фрагменты по очереди
                for (int i = 0; i<countOfSet; i++ ){
                    DataSet dataSet = TabSet.getOneSetFragmentData(database, fileId, i);
                    dataSet.setTimeOfRep((dataSet.getTimeOfRep())*ff);
                    TabSet.updateSetFragment(database, dataSet);
                    // Log.d(TAG, "ChangeTempActivity dataSet Time = " + dataSet.getTimeOfRep());
                }
                //если только в одной - выбранной -  строке
            }else {
                DataSet dataSet = TabSet.getOneSetFragmentData(database, fileId, position);
                dataSet.setTimeOfRep((dataSet.getTimeOfRep())*ff);
                TabSet.updateSetFragment(database, dataSet);
                //Log.d(TAG, "ChangeTempActivity dataSet Time = " + dataSet.getTimeOfRep());
            }
            //если редактируем количество
        }else {
            if (isChecked){   //если сразу во всех строках
                //обновляем фрагменты по очереди
                for (int i = 0; i<countOfSet; i++ ){
                    DataSet dataSet = TabSet.getOneSetFragmentData(database, fileId, i);
                    dataSet.setReps((dataSet.getReps())+ii);
                    //если число повторений < 0, пишем 0
                    if (dataSet.getReps() < 0){
                        dataSet.setReps(0);
                    }
                    TabSet.updateSetFragment(database, dataSet);
                    // Log.d(TAG, "ChangeTempActivity dataSet Reps = " + dataSet.getReps());
                }
                //если только в одной - выбранной -  строке
            }else {
                DataSet dataSet = TabSet.getOneSetFragmentData(database, fileId, position);
                dataSet.setReps((dataSet.getReps())+ii);
                if (dataSet.getReps() < 0){
                    dataSet.setReps(0);
                }
                TabSet.updateSetFragment(database, dataSet);
                //Log.d(TAG, "ChangeTempActivity dataSet Reps = " + dataSet.getTimeOfRep());
            }
        }
    }

}
