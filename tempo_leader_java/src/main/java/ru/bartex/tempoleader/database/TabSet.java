package ru.bartex.tempoleader.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.tempoleader.data.DataFile;
import ru.bartex.tempoleader.data.DataSet;

public class TabSet {


    public static final String TAG ="33333";


    public final static String TABLE_NAME = "TimeReps";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_SET_FILE_ID = "File_ID";
    public final static String COLUMN_SET_TIME = "Set_Time";
    public final static String COLUMN_SET_REPS = "Set_Reps";
    public final static String COLUMN_SET_FRAG_NUMBER = "FragNumber";

    public TabSet(){
        //пустой конструктор
    }

    public static void createTable(SQLiteDatabase database){
        // Строка для создания таблицы TabSet
        String SQL_CREATE_TAB_SET = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SET_FILE_ID + " INTEGER NOT NULL, "
                + COLUMN_SET_FRAG_NUMBER + " INTEGER NOT NULL, "
                + COLUMN_SET_TIME + " REAL NOT NULL, "
                + COLUMN_SET_REPS + " INTEGER NOT NULL DEFAULT 1);";


        database.execSQL(SQL_CREATE_TAB_SET);
    }

    //получаем количество фрагментов подхода в подходе
    public static int getSetFragmentsCount(SQLiteDatabase database, long fileId) {
        Log.i(TAG, "TempDBHelper.getSetFragmentsCount");

        String query = "select " + COLUMN_SET_TIME + " from " + TABLE_NAME +
                " where " +COLUMN_SET_FILE_ID + " = ? ";
        Cursor mCursor = database.rawQuery(query, new String[]{String.valueOf(fileId)});

        int count = mCursor.getCount();
        mCursor.close();
        return count;
    }

    //Метод для добавления нового фрагмента подхода в список
    public static void addSet(SQLiteDatabase database, DataSet set, long file_id) {
        Log.d(TAG, "MyDatabaseHelper.addSet ... " + set.getNumberOfFrag());

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SET_FILE_ID, file_id);
        cv.put(COLUMN_SET_TIME, set.getTimeOfRep());
        cv.put(COLUMN_SET_REPS, set.getReps());
        cv.put(COLUMN_SET_FRAG_NUMBER, set.getNumberOfFrag());
        // вставляем строку
        database.insert(TABLE_NAME, null, cv);
    }

    //Метод для вставки нового фрагмента подхода в список ПОСЛЕ выделенного фрагмента
    public static void addSetAfter(SQLiteDatabase database, DataSet set, long file_id, int numberOfString) {
        Log.d(TAG, "MyDatabaseHelper.addSetAfter ... " + set.getNumberOfFrag());

        //получаем  фрагменты подхода с ID = file_id
        String query = "select " + _ID + " , " +  COLUMN_SET_FRAG_NUMBER +
                " from " + TABLE_NAME +
                " where " + COLUMN_SET_FILE_ID + " = ? " +
                " order by " + COLUMN_SET_FRAG_NUMBER;
        Cursor mCursor = database.rawQuery(query, new String[]{String.valueOf(file_id)});
        mCursor.moveToPosition(numberOfString-1);
        int fragCount = mCursor.getCount();
        Log.d(TAG, "MyDatabaseHelper.addSetAfter mCursor.getCount()= " + fragCount +
                " numberOfString-1 = " + (numberOfString-1));
        try {
            // Проходим через все строки в курсоре начиная с position
            while (mCursor.moveToNext()){
                //вычисляем id текущей строки курсора
                long id = mCursor.getLong(mCursor.getColumnIndex(_ID));

                ContentValues updatedValues = new ContentValues();
                updatedValues.put(COLUMN_SET_FRAG_NUMBER, (mCursor.getPosition() + 2));
                database.update(TABLE_NAME, updatedValues,
                        COLUMN_SET_FILE_ID + "=" + file_id +
                                " AND " + _ID + "=" + id, null);
                Log.d(TAG, "addSetAfter mCursor.getPosition() = " +
                        mCursor.getPosition() + " id строки =" + id);
            }
            //Добавляем фрагмент подхода созданный в DetailActiviti
            TabSet.addSet(database, set, file_id);
            //пересчитывает  номера фрагментов подхода
            //this.rerangeSetFragments(file_id);

        } finally {
            //закрываем курсор
            mCursor.close();
        }
    }

    //Метод для вставки нового фрагмента подхода в список ДО  выделенного фрагмента
    public static void addSetBefore(SQLiteDatabase database, DataSet set, long file_id, int numberOfString) {
        Log.d(TAG, "MyDatabaseHelper.addSetBefore ... getNumberOfFrag = " + set.getNumberOfFrag());

        //получаем  фрагменты подхода с ID = file_id
        String query = "select " +_ID + " , " +  COLUMN_SET_FRAG_NUMBER +
                " from " + TABLE_NAME +
                " where " + COLUMN_SET_FILE_ID + " = ? " +
                " order by " + COLUMN_SET_FRAG_NUMBER;
        Cursor mCursor = database.rawQuery(query, new String[]{String.valueOf(file_id)});
        mCursor.moveToPosition(numberOfString-2);
        Log.d(TAG, "MyDatabaseHelper.addSetBefore isBeforeFirst() =" + mCursor.isBeforeFirst());
        int fragCount = mCursor.getCount();
        Log.d(TAG, "MyDatabaseHelper.addSetBefore mCursor.getCount()= " + fragCount +
                " numberOfString = " + (numberOfString-2));
        try {
            // Проходим через все строки в курсоре начиная с position
            while (mCursor.moveToNext()){
                //вычисляем id текущей строки курсора
                long id = mCursor.getLong(mCursor.getColumnIndex(_ID));

                ContentValues updatedValues = new ContentValues();
                updatedValues.put(COLUMN_SET_FRAG_NUMBER, (mCursor.getPosition() + 2));
                database.update(TABLE_NAME, updatedValues,
                        COLUMN_SET_FILE_ID + "=" + file_id +
                                " AND " + _ID + "=" + id, null);
                Log.d(TAG, "addSetBefore mCursor.getPosition() = " +
                        mCursor.getPosition() + " id строки =" + id);
            }
            //Добавляем фрагмент подхода созданный в DetailActiviti
            TabSet.addSet(database, set, file_id);
            //пересчитывает  номера фрагментов подхода
            //this.rerangeSetFragments(file_id);

        } finally {
            //закрываем курсор
            mCursor.close();
        }
    }

    /**
     * Пересчитывает  номера фрагментов подхода после удаления какого либо фрагмента подхода
     */
    public static void rerangeSetFragments(SQLiteDatabase database, long fileId) throws SQLException {

        String query = "select " + _ID + " , " +  COLUMN_SET_FRAG_NUMBER +
                " from " + TABLE_NAME +
                " where " + COLUMN_SET_FILE_ID + " = ? " +
                " order by " + COLUMN_SET_FRAG_NUMBER;

        Cursor mCursor = database.rawQuery(query, new String[]{String.valueOf(fileId)});

        try {
            // Проходим через все строки в курсоре
            while (mCursor.moveToNext()) {

                //вычисляем id текущей строки курсора
                long id = mCursor.getLong(mCursor.getColumnIndex(TabSet._ID));

                ContentValues updatedValues = new ContentValues();
                updatedValues.put(COLUMN_SET_FRAG_NUMBER, (mCursor.getPosition()+1));
                database.update(TABLE_NAME, updatedValues,
                        COLUMN_SET_FILE_ID + "=" + fileId +
                                " AND " + _ID + "=" + id, null);
                Log.d(TAG, "rerangeSetFragments mCursor.getPosition() = " +
                        mCursor.getPosition() + " id строки =" + id);
            }
        } finally {
            // Всегда закрываем курсор после чтения
            mCursor.close();
        }
    }

    //Метод для изменения времени и количества повторений фрагмента подхода  по известному DataSet
    public static void updateSetFragment(SQLiteDatabase database, DataSet mDataSet) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(COLUMN_SET_TIME, mDataSet.getTimeOfRep());
        updatedValues.put(COLUMN_SET_REPS, mDataSet.getReps());

        database.update(TABLE_NAME, updatedValues,
                COLUMN_SET_FILE_ID + "=" + mDataSet.getFile_id()+
                        " AND " + _ID + "=" + mDataSet.getSet_id(), null);

    }

    //удаляем строку с номером fragmentNumber, относящуюся к файлу с id =fileId
    public static void deleteSet(SQLiteDatabase database, long fileId, long fragmentNumber) {

        database.delete(TabSet.TABLE_NAME, COLUMN_SET_FILE_ID +
                " =? " + " AND " + COLUMN_SET_FRAG_NUMBER +
                " =? ", new String[]{String.valueOf(fileId),String.valueOf(fragmentNumber)});
    }

    /**
     * Возвращает курсор с набором данных времени, количества повторений
     * и порядкового номера фрагмента для всех фрагментов одного подхода с id = rowId
     * отсортировано по COLUMN_SET_FRAG_NUMBER, иначе в Андроид 4 работает неправильно
     */
    public static Cursor getAllSetFragments(SQLiteDatabase database, long rowId) throws SQLException {

        Cursor mCursor = database.query(true, TABLE_NAME,
                new String[]{COLUMN_SET_TIME, COLUMN_SET_REPS, COLUMN_SET_FRAG_NUMBER},
                COLUMN_SET_FILE_ID + "=" + rowId,
                null, null, null, COLUMN_SET_FRAG_NUMBER, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Возвращает DataSet с номером фрагмента подхода position из файла с номером ID = fileId
     */
    public static DataSet getOneSetFragmentData(SQLiteDatabase database, long fileId, int position) throws SQLException {

        Cursor cursorFile = database.query(true, TABLE_NAME,
                new String[]{_ID, COLUMN_SET_FILE_ID, COLUMN_SET_FRAG_NUMBER,
                        COLUMN_SET_TIME, COLUMN_SET_REPS},
                COLUMN_SET_FILE_ID + "=" + fileId,
                null, null, null, COLUMN_SET_FRAG_NUMBER, null);
        if ((cursorFile != null)&& (cursorFile.getCount()>=position)) {
            cursorFile.moveToPosition(position);
        }else {
            Log.d(TAG, "getAllSetData cursorFile.getCount() = " + cursorFile.getCount()+
                    "  position" + position);
        }
        // Используем индекс для получения строки или числа
        long current_ID = cursorFile.getLong(
                cursorFile.getColumnIndex(_ID));
        long current_fileId = cursorFile.getLong(
                cursorFile.getColumnIndex(COLUMN_SET_FILE_ID));
        float current_setTime = cursorFile.getFloat(
                cursorFile.getColumnIndex(COLUMN_SET_TIME));
        int current_setReps = cursorFile.getInt(
                cursorFile.getColumnIndex(COLUMN_SET_REPS));
        int current_nameFragNumber = cursorFile.getInt(
                cursorFile.getColumnIndex(COLUMN_SET_FRAG_NUMBER));

        DataSet dataset = new DataSet(current_ID, current_fileId,
                current_setTime, current_setReps, current_nameFragNumber);

        cursorFile.close();
        return dataset;
    }

    /**
     * Возвращает курсор с набором данных времени всех фрагментов одного подхода с id = rowId
     */
    public static Cursor getAllSetFragmentsRaw(SQLiteDatabase database, long rowId) throws SQLException {

        String query = "select " + COLUMN_SET_TIME + " from " + TABLE_NAME +
                " where " + COLUMN_SET_FILE_ID + " = ? ";

        Cursor mCursor = database.rawQuery(query, new String[]{String.valueOf(rowId)});
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Возвращает время фрагмента подхода с номером position для файла подхода с id = rowId
     */
    public static float getTimeOfRepInPosition(SQLiteDatabase database, long rowId, int position) throws SQLException {

        String query = "select " + COLUMN_SET_TIME + " from " + TABLE_NAME +
                " where " + COLUMN_SET_FILE_ID + " = ? ";
        Cursor mCursor = database.rawQuery(query, new String[]{String.valueOf(rowId)});

        if ((mCursor != null) && (mCursor.getCount() != 0)) {
            mCursor.moveToPosition(position);
            // Используем индекс для получения времени фрагмента подхода с номером position
            float currentTime = mCursor.getFloat(mCursor.getColumnIndex(COLUMN_SET_TIME));
            Log.d(TAG, "getTimeOfRepInPosition position = " + position +
                    " currentTime = " + currentTime);

            return currentTime;
        }
        if (mCursor != null) {
            mCursor.close();
        }
        //если курсор = null или в курсоре нет данных, то
        return -1;
    }

    /**
     * Возвращает количество повторений для фрагмента подхода с номером position
     * для файла подхода с id = rowId
     */
    public static int getRepsInPosition(SQLiteDatabase database, long rowId, int position) throws SQLException {


        String query = "select " + COLUMN_SET_REPS + " from " + TABLE_NAME +
                " where " + COLUMN_SET_FILE_ID + " = ? ";
        Cursor mCursor = database.rawQuery(query, new String[]{String.valueOf(rowId)});

        if ((mCursor != null) && (mCursor.getCount() != 0)) {
            mCursor.moveToPosition(position);
            // Используем индекс для получения времени фрагмента подхода с номером position
            int currentReps = mCursor.getInt(mCursor.getColumnIndex(COLUMN_SET_REPS));
            Log.d(TAG, "getTimeOfRepInPosition position = " + position +
                    " currentReps = " + currentReps);

            return currentReps;
        }
        if (mCursor != null) {
            mCursor.close();
        }
        //если курсор = null или в курсоре нет данных, то
        return -1;
    }

    public static float getSumOfTimeSet(SQLiteDatabase database, long rowId) throws SQLException {

        String query = " select " + " sum( " + COLUMN_SET_TIME +
                " * " + COLUMN_SET_REPS + " ) " +
                " from " + TABLE_NAME + " where " + COLUMN_SET_FILE_ID + " = ?";

        Cursor mCursor = database.rawQuery(query, new String[]{String.valueOf(rowId)});
        float sum = -1;
        if ((mCursor != null) && (mCursor.getCount() != 0)) {
            mCursor.moveToFirst();

            // Используем индекс для получения строки или числа
            sum = mCursor.getFloat(0);
        }
        Log.d(TAG, "getSumOfTimeSet sum = " + sum);
        if (mCursor != null) {
            mCursor.close();
        }
        return sum;
    }
    public static int getSumOfRepsSet(SQLiteDatabase database, long rowId) throws SQLException {

        String query = " select " + " sum( " + COLUMN_SET_REPS + " ) " +
                " from " + TABLE_NAME + " where " + COLUMN_SET_FILE_ID + " = ?";

        Cursor mCursor = database.rawQuery(query, new String[]{String.valueOf(rowId)});
        int sum = -1;
        if ((mCursor != null) && (mCursor.getCount() != 0)) {
            mCursor.moveToFirst();

            // Используем индекс для получения строки или числа
            sum = mCursor.getInt(0);
        }
        Log.d(TAG, "getSumOfRepsSet sum = " + sum);
        if (mCursor != null) {
            mCursor.close();
        }
        return sum;
    }

    // создать копию файла  в базе данных и получить его id
    public static long createFileCopy(SQLiteDatabase database, String finishFileName, long fileId, String endName){

        //количество фрагментов подхода
        int countOfSet =TabSet.getSetFragmentsCount(database,  fileId);
        String newFileName = finishFileName + endName;
        Log.d(TAG, "createFileCopy newFileName = " + newFileName);
        //создаём и записываем в базу копию файла на случай отмены изменений
        DataFile dataFileCopy = TabFile.getAllFilesData(database, fileId);
        dataFileCopy.setFileName(newFileName);
        long fileIdCopy = TabFile.addFile(database, dataFileCopy);
        //записываем фрагменты подхода в файл-копию
        for (int i = 0; i<countOfSet; i++ ){
            DataSet dataSet = TabSet.getOneSetFragmentData(database, fileId, i);
            TabSet.addSet(database, dataSet,fileIdCopy);
        }
        Log.d(TAG, "createFileCopy фрагментов  = " +
                TabSet.getSetFragmentsCount(database, fileIdCopy));
        return  fileIdCopy;
    }
}
