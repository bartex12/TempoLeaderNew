package ru.bartex.tempoleader.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.bartex.tempoleader.data.DataFile;
import ru.bartex.tempoleader.data.DataSet;


public class TempDBHelper extends SQLiteOpenHelper {

    public static final String TAG = "33333";

    //Имя файла базы данных
    private static final String DATABASE_NAME = "Tempolider.db";
    // Версия базы данных. При изменении схемы увеличить на единицу
    private static final int DATABASE_VERSION = 1;


    public TempDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TabFile.createTable(db);
        TabSet.createTable(db);
        Log.d(TAG, "Создана база данных  " + DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.d(TAG, "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
    }

    //***********************************************

    // Если записей в базе нет, вносим запись
    public void createDefaultSetIfNeed() {

        SQLiteDatabase db = this.getReadableDatabase();

        int count = this.getFilesCount();
        if (count == 0) {

            //получаем дату и время в нужном для базы данных формате
            String dateFormat = getDateString();
            String timeFormat = getTimeString();

            //создаём экземпляр класса DataFile в конструкторе
            DataFile file1 = new DataFile(P.FILENAME_OTSECHKI_SEC,
                    dateFormat, timeFormat, "Подтягивание",
                    "Подтягивание на перекладине", P.TYPE_TIMEMETER, 6);

            //добавляем запись в таблицу TabFile, используя данные DataFile и получаем id записи
            long file1_id = TabFile.addFile(db, file1);

            //создаём экземпляр класса DataSet в конструкторе
            DataSet set1 = new DataSet(2f, 1, 1);
            //добавляем запись в таблицу TabSet, используя данные DataSet
            TabSet.addSet(db, set1, file1_id);
            // повторяем для всех фрагментов подхода
            DataSet set2 = new DataSet(2.5f, 1, 2);
            TabSet.addSet(db, set2, file1_id);
            DataSet set3 = new DataSet(3f, 1, 3);
            TabSet.addSet(db, set3, file1_id);
            DataSet set4 = new DataSet(3.5f, 1, 4);
            TabSet.addSet(db, set4, file1_id);

            Log.d(TAG, "MyDatabaseHelper.createDefaultPersonIfNeed ... count = " +
                    this.getFilesCount());

            //создаём экземпляр класса DataFile в конструкторе
            DataFile file2 = new DataFile(P.FILENAME_OTSECHKI_TEMP,
                    dateFormat, timeFormat, "Полиатлон",
                    "Пистолетики", P.TYPE_TEMPOLEADER, 6);

            //добавляем запись в таблицу TabFile, используя данные DataFile и получаем id записи
            long file2_id = TabFile.addFile(db, file2);

            //создаём экземпляр класса DataSet в конструкторе
            DataSet set11 = new DataSet(2.2f, 3, 1);
            //добавляем запись в таблицу TabSet, используя данные DataSet
            TabSet.addSet(db, set11, file2_id);
            // повторяем для всех фрагментов подхода
            DataSet set22 = new DataSet(2.3f, 3, 2);
            TabSet.addSet(db, set22, file2_id);
            DataSet set33 = new DataSet(2.5f, 4, 3);
            TabSet.addSet(db, set33, file2_id);
            DataSet set44 = new DataSet(2.7f, 2, 4);
            TabSet.addSet(db, set44, file2_id);

            Log.d(TAG, "MyDatabaseHelper.createDefaultPersonIfNeed ... count = " +
                    this.getFilesCount());
        }
    }

    public String getDateString() {
        Calendar calendar = new GregorianCalendar();
        return String.format("%s-%s-%s",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String getTimeString() {
        Calendar calendar = new GregorianCalendar();
        return String.format("%s:%s:%s",
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }

    //получаем количество файлов (сохранённых подходов) в базе
    public int getFilesCount() {
        Log.i(TAG, "TempDBHelper.getFilesCount ... ");
        String countQuery = "SELECT  * FROM " + TabFile.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

        //***********************************

    //Удалить запись в таблице TabFile и все записи в таблице TabSet с id удалённой записи в TabFile
    public void deleteFileAndSets(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TabFile.TABLE_NAME, TabFile._ID + "=" + rowId, null);
        db.delete(TabSet.TABLE_NAME, TabSet.COLUMN_SET_FILE_ID + "=" + rowId, null);
        db.close();
    }

    //вывод в лог всех строк базы
    public void displayDatabaseInfo() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                TabSet._ID,
                TabSet.COLUMN_SET_FILE_ID,
                TabSet.COLUMN_SET_TIME,
                TabSet.COLUMN_SET_REPS,
                TabSet.COLUMN_SET_FRAG_NUMBER};

        // Делаем запрос
        Cursor cursor = db.query(
                TabSet.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        // Зададим условие для выборки - список столбцов
        String[] projectionFile = {
                TabFile._ID,
                TabFile.COLUMN_FILE_NAME,
                TabFile.COLUMN_FILE_NAME_DATE,
                TabFile.COLUMN_FILE_NAME_TIME,
                TabFile.COLUMN_KIND_OF_SPORT,
                TabFile.COLUMN_DESCRIPTION_OF_SPORT,
                TabFile.COLUMN_TYPE_FROM,
                TabFile.COLUMN_DELAY};

        // Делаем запрос
        Cursor cursorFile = db.query(
                TabFile.TABLE_NAME,   // таблица
                projectionFile,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        try {
            // Проходим через все ряды в таблице TabFile
            while (cursorFile.moveToNext()) {
                // Используем индекс для получения строки или числа
                int current_ID = cursorFile.getInt(
                        cursorFile.getColumnIndex(TabFile._ID));
                String current_nameFile = cursorFile.getString(
                        cursorFile.getColumnIndex(TabFile.COLUMN_FILE_NAME));
                String current_nameFileDate = cursorFile.getString(
                        cursorFile.getColumnIndex(TabFile.COLUMN_FILE_NAME_DATE));
                String current_nameFileTime = cursorFile.getString(
                        cursorFile.getColumnIndex(TabFile.COLUMN_FILE_NAME_TIME));
                String current_kindSport = cursorFile.getString(
                        cursorFile.getColumnIndex(TabFile.COLUMN_KIND_OF_SPORT));
                String current_descript = cursorFile.getString(
                        cursorFile.getColumnIndex(TabFile.COLUMN_DESCRIPTION_OF_SPORT));
                String current_typeFrom = cursorFile.getString(
                        cursorFile.getColumnIndex(TabFile.COLUMN_TYPE_FROM));
                int current_delay = cursorFile.getInt(
                        cursorFile.getColumnIndex(TabFile.COLUMN_DELAY));
                // Выводим построчно значения каждого столбца
                Log.d(TAG, "\n" + current_ID + " - " +
                        current_nameFile + " - " +
                        current_nameFileDate + " - " +
                        current_nameFileTime + " - " +
                        current_kindSport + " - " +
                        current_descript + " - " +
                        current_typeFrom + " - " +
                        current_delay);
            }

            // Проходим через все ряды в таблице TabSet
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(
                        cursor.getColumnIndex(TabSet._ID));
                String current_FILE_ID = cursor.getString(
                        cursor.getColumnIndex(TabSet.COLUMN_SET_FILE_ID));
                String current_SET_TIME = cursor.getString(
                        cursor.getColumnIndex(TabSet.COLUMN_SET_TIME));
                int current_SET_REPS = cursor.getInt(
                        cursor.getColumnIndex(TabSet.COLUMN_SET_REPS));
                int current_SET_FRAG_NUMBER = cursor.getInt(
                        cursor.getColumnIndex(TabSet.COLUMN_SET_FRAG_NUMBER));
                // Выводим построчно значения каждого столбца
                Log.d(TAG, "\n" + currentID + " - " +
                        current_FILE_ID + " - " +
                        current_SET_TIME + " - " +
                        current_SET_REPS + " - " +
                        current_SET_FRAG_NUMBER);
            }

        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
            cursorFile.close();
        }
    }






}