package ru.barcats.tempo_leader_javanew.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.P;

public class TabFile {

    public static final String TAG ="33333";

    private TabFile(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "FileData";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_FILE_NAME = "FileName";
    public final static String COLUMN_FILE_NAME_DATE = "FileNameDate";
    public final static String COLUMN_FILE_NAME_TIME = "FileNameTime";
    public final static String COLUMN_KIND_OF_SPORT = "KindOfSport";
    public final static String COLUMN_DESCRIPTION_OF_SPORT = "DescriptionOfSport";
    public final static String COLUMN_TYPE_FROM = "Type_From";
    public final static String COLUMN_DELAY = "Delay";


    public static void createTable(SQLiteDatabase database){
        // Строка для создания таблицы TabFile
        String SQL_CREATE_TAB_FILE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FILE_NAME + " TEXT NOT NULL, "
                + COLUMN_FILE_NAME_DATE + " TEXT NOT NULL, "
                + COLUMN_FILE_NAME_TIME + " TEXT NOT NULL, "
                + COLUMN_KIND_OF_SPORT + " TEXT, "
                + COLUMN_DESCRIPTION_OF_SPORT + " TEXT, "
                + COLUMN_TYPE_FROM + " TEXT NOT NULL DEFAULT 'type_timemeter', "
                + COLUMN_DELAY + " INTEGER NOT NULL DEFAULT 6);";

        // Запускаем создание таблицы
        database.execSQL(SQL_CREATE_TAB_FILE);
    }

    //Метод для добавления нового файла с подходом в список
    public static long addFile(SQLiteDatabase database, DataFile file) {
        Log.d(TAG, "TempDBHelper.addFile ... " + file.getFileName());

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FILE_NAME, file.getFileName());
        cv.put(COLUMN_FILE_NAME_DATE, file.getFileNameDate());
        cv.put(COLUMN_FILE_NAME_TIME, file.getFileNameTime());
        cv.put(COLUMN_KIND_OF_SPORT, file.getKindOfSport());
        cv.put(COLUMN_DESCRIPTION_OF_SPORT, file.getDescriptionOfSport());
        cv.put(COLUMN_TYPE_FROM, file.getType_From());
        cv.put(COLUMN_DELAY, file.getDelay());
        // вставляем строку

        return database.insert(TABLE_NAME, null, cv);
    }

    //Метод для изменения имени файла
    public static void updateFileName(SQLiteDatabase database, String nameFile, long fileId) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(COLUMN_FILE_NAME, nameFile);
        database.update(TABLE_NAME, updatedValues,_ID + "=" + fileId, null);
    }

    //Метод для изменения имени файла
    public static void updateDelay(SQLiteDatabase database, int delay, long fileId) {

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(COLUMN_DELAY, delay);
        database.update(TABLE_NAME, updatedValues,_ID + "=" + fileId, null);
    }

    //получаем ID по имени
    public static long getIdFromFileName(SQLiteDatabase database, String name) {
        long currentID;

        Cursor cursor = database.query(
                TABLE_NAME,   // таблица
                new String[]{_ID},            // столбцы
                COLUMN_FILE_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{name},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(_ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromName currentID = " + currentID);
        cursor.close();
        return currentID;
    }
    /**
     * Возвращает задержку старта файла по его ID
     */
    public static int  getFileDelayFromTabFile(SQLiteDatabase database,  String name)  {

        long rowId = getIdFromFileName(database, name);

        Cursor mCursor = database.query(true, TABLE_NAME,
                null,
                _ID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        int delay = mCursor.getInt(mCursor.getColumnIndex(COLUMN_DELAY));

        mCursor.close();
        return delay;
    }

    /**
     * Возвращает имя файла по его ID
     */
    public static String getFileNameFromTabFile(SQLiteDatabase database,   long rowId) throws SQLException {

        Cursor mCursor = database.query(true, TABLE_NAME,
                null,
                _ID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        String fileName = mCursor.getString(mCursor.getColumnIndex(COLUMN_FILE_NAME));
        Log.d(TAG, "getFileNameFromTabFile fileName = " + fileName);
        mCursor.close();

        return fileName;
    }

    /**
     * Возвращает тип файла по его ID
     */
    public static String getFileTypeFromTabFile( SQLiteDatabase database, long rowId) throws SQLException {

        Cursor mCursor = database.query(true, TABLE_NAME,
                null,
                _ID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        String fileType = mCursor.getString(mCursor.getColumnIndex(COLUMN_TYPE_FROM));

        mCursor.close();
        return fileType;
    }

    /**
     * Возвращает объект DataFile с данными файла из таблицы TabFile с номмером ID = rowId
     */
    public static DataFile getAllFilesData(SQLiteDatabase database, long rowId) throws SQLException {

        Cursor cursorFile = database.query(true, TABLE_NAME,
                new String[]{_ID, COLUMN_FILE_NAME,COLUMN_FILE_NAME_DATE,
                        COLUMN_FILE_NAME_TIME, COLUMN_KIND_OF_SPORT,
                        COLUMN_DESCRIPTION_OF_SPORT, COLUMN_TYPE_FROM,
                        COLUMN_DELAY},
                _ID + "=" + rowId,
                null, null, null, null, null);
        if ((cursorFile != null)&& (cursorFile.getCount()>0)) {
            cursorFile.moveToFirst();
        }
        Log.d(TAG, "getAllFilesData cursorFile.getCount() = " + cursorFile.getCount());

        // Используем индекс для получения строки или числа
        long current_ID = cursorFile.getLong(
                cursorFile.getColumnIndex(_ID));
        String current_nameFile = cursorFile.getString(
                cursorFile.getColumnIndex(COLUMN_FILE_NAME));
        String current_nameFileDate = cursorFile.getString(
                cursorFile.getColumnIndex(COLUMN_FILE_NAME_DATE));
        String current_nameFileTime = cursorFile.getString(
                cursorFile.getColumnIndex(COLUMN_FILE_NAME_TIME));
        String current_kindSport = cursorFile.getString(
                cursorFile.getColumnIndex(COLUMN_KIND_OF_SPORT));
        String current_descript = cursorFile.getString(
                cursorFile.getColumnIndex(COLUMN_DESCRIPTION_OF_SPORT));
        String current_typeFrom = cursorFile.getString(
                cursorFile.getColumnIndex(COLUMN_TYPE_FROM));
        int current_delay = cursorFile.getInt(
                cursorFile.getColumnIndex(COLUMN_DELAY));

        DataFile dataFile = new DataFile(current_ID,current_nameFile,
                current_nameFileDate,current_nameFileTime,current_kindSport,
                current_descript,current_typeFrom,current_delay);

        cursorFile.close();
        return dataFile;

    }

//    //получить названия всех файлов с типом файла P.TYPE_TIMEMETER
//    public static ArrayList<String> getArrayListFilesFromTimemeter(SQLiteDatabase database)  {
//
//        String query = "select " + _ID + " , " + COLUMN_FILE_NAME  + " from " + TABLE_NAME +
//                " where " + COLUMN_TYPE_FROM + " = ? ";
//        Cursor mCursor = database.rawQuery(query, new String[]{P.TYPE_TIMEMETER});
//
//        ArrayList<String> list = new ArrayList<String>(mCursor.getCount());
//        // Проходим через все строки в курсоре
//        while (mCursor.moveToNext()){
//            String name  = mCursor.getString(mCursor.getColumnIndex(COLUMN_FILE_NAME));
//            list.add(name);
//            //Log.i(TAG, "SmetaOpenHelper.getArrayTypeId position = " + position);
//        }
//        mCursor.close();
//        return list;
//    }

    //получить названия всех файлов с типом файла type_from
    public static ArrayList<String> getArrayListFilesWhithType(
            SQLiteDatabase database, String type_from)  {

        String query = "select " + _ID + " , " + COLUMN_FILE_NAME  + " from " + TABLE_NAME +
                " where " + COLUMN_TYPE_FROM + " = ? ";
        Cursor mCursor = database.rawQuery(query, new String[]{ type_from});

        ArrayList<String> list = new ArrayList<String>(mCursor.getCount());
        // Проходим через все строки в курсоре
        while (mCursor.moveToNext()){
            String name  = mCursor.getString(mCursor.getColumnIndex(COLUMN_FILE_NAME));
            list.add(name);
            //Log.i(TAG, "SmetaOpenHelper.getArrayTypeId position = " + position);
        }
        mCursor.close();
        return list;
    }

    //получаем количество файлов (сохранённых подходов) в базе
    public static  int getFilesCount(SQLiteDatabase database) {
        Log.i(TAG, "TempDBHelper.getFilesCount ... ");
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
