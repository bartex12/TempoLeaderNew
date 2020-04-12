package ru.barcats.tempo_leader_javanew.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

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
    public void createDefaultSetIfNeed(SQLiteDatabase database) {

        int count = TabFile.getFilesCount(database);
        Log.d(TAG, "MyDatabaseHelper.createDefaultPersonIfNeed ... count = " + count);

        if (count <= 0) {
            DataFile file;
            long file_id;
            //получаем дату и время в нужном для базы данных формате
            String dateFormat = getDateString();
            String timeFormat = getTimeString();

                file = new DataFile(P.FILENAME_OTSECHKI_SEC,
                        dateFormat, timeFormat, "Подтягивание",
                        "Подтягивание на перекладине", P.TYPE_TIMEMETER, 6);

                file_id = TabFile.addFile(database, file);

                //создаём экземпляр класса DataSet в конструкторе
                DataSet set1 = new DataSet(2f, 1, 1);
                //добавляем запись в таблицу TabSet, используя данные DataSet
                TabSet.addSet(database, set1, file_id);
                // повторяем для всех фрагментов подхода
                DataSet set2 = new DataSet(2.5f, 1, 2);
                TabSet.addSet(database, set2, file_id);
                DataSet set3 = new DataSet(3f, 1, 3);
                TabSet.addSet(database, set3, file_id);
                DataSet set4 = new DataSet(3.5f, 1, 4);
                TabSet.addSet(database, set4, file_id);
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