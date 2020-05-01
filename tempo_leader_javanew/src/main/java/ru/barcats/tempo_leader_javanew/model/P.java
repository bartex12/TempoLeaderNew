package ru.barcats.tempo_leader_javanew.model;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class P {

    public static final String TAG ="33333";

    //идентификатор интента : Откуда пришёл?
    public static final String FROM_ACTIVITY = "ru.bartex.p010_train_from_activity";
    //пришёл от TimeGrafactivity
    public static final int TIME_GRAF_ACTIVITY = 222;
    //пришёл от TabBarActivity
    public static final int TAB_BAR_ACTIVITY = 333;
    //пришёл от DetailActivity
    public static final int NEW_EXERCISE_ACTIVITY = 555;
    // MainActivity =111   TIME_GRAF_ACTIVITY = 222    TabBarActivity = 333 DetailActivity =444
    //NewExerciseActivity = 555
    //пришёл от DialogSetDelay
    public static final int DIALOG_DELAY = 777;

    //плюсик в тулбаре редактора
    public final static int TO_ADD_LAST_SET = 5555;
    //контекстное меню в редакторе -вставить после
    public final static int TO_INSERT_AFTER_FRAG = 7777;
    //контекстное меню в редакторе -вставить до
    public final static int TO_INSERT_BEFORE_FRAG = 8888;
    //контекстное меню в редакторе - изменить сет
    public final static int TO_CHANGE_SET = 9999;

    //имя файла для записи раскладки секундомера по умолчанию - когда имя - пустая строка
     public static final String FILENAME_OTSECHKI_SEC ="автосохранение_секундомера";
    //public static final String FILENAME_OTSECHKI_SEC ="автосохранение";

    public static final String TYPE_TIMEMETER ="type_timemeter";
    public static final String TYPE_TEMPOLEADER ="type_tempoleader";
    public static final String TYPE_LIKE ="type_like";

    //имя файла из строки в TabBarActivity
    public static final String NAME_OF_FILE = "ru.bartex.p010_train.name_of_file";
    public static final String FINISH_FILE_NAME = "Раскладка без имени";
    public static final String FINISH_FILE_ID = "ru.bartex.p010_train_date_fileId";
    //позиция в списке редактора
    public static final String POSITION_OF_LIST = "ru.bartex.p010_train.name_position_of_list";
    // ключ для имени последнего сохранённого файла
    public static final String LAST_FILE = "ru.bartex.p010_train.last_file";

    public static final String KEY_DELAY = "DELAY";
    public static final String KEY_FILENAME = "FILENAME";

    public static final String ARG_NAME_OF_FILE = "ru.bartex.p010_train.NameOfFile";
    public static final String ARG_NUMBER_ITEM = "ru.bartex.p010_train.NumberItem";
    public static final String ARG_DELAY = "ru.bartex.p010_train.delay";
    public static final String ARG_VALUE_CHANGE_TEMP = "ru.bartex.p010_train.value";


    public final static int DETAIL_CHANGE_TEMP = 20;

    public static final int DELETE_ACTION_SEC = 1;
    public static final int CHANGE_ACTION_SEC = 2;
    public static final int CANCEL_ACTION_SEC = 3;
    public static final int MOVE_LIKE_ACTION_SEC = 5;
    public static final int MOVE_TEMP_ACTION_SEC = 7;

    public static final int DELETE_ACTION_TEMP = 11;
    public static final int CHANGE_ACTION_TEMP = 12;
    public static final int CANCEL_ACTION_TEMP = 13;
    public static final int MOVE_LIKE_ACTION_TEMP = 15;
    public static final int MOVE_SEC_ACTION_TEMP = 16;


    public static final int DELETE_ACTION_LIKE = 21;
    public static final int CHANGE_ACTION_LIKE = 22;
    public static final int CANCEL_ACTION_LIKE = 23;
    public static final int MOVE_SEC_ACTION_LIKE = 26;
    public static final int MOVE_TEMP_ACTION_LIKE = 27;


    public static final int DELETE_CHANGETEMP = 31;
    public static final int CHANGE_CHANGETEMP = 32;
    public static final int CANCEL_CHANGETEMP = 33;

    public static final int INSERT_BEFORE_CHANGETEMP = 37;
    public static final int INSERT_AFTER_CHANGETEMP = 38;


    //====================================================================//

    public static String setDateString() {
        Calendar calendar = new GregorianCalendar();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        return  String.format(Locale.ENGLISH,"%02d.%02d.%04d_%02d:%02d:%02d",
                date, month+1, year, hour, min, sec);
    }

    public static String getTimeString1 (Long timeInMillis){

        //формируем формат строки показа времени
        int minut = (int)((timeInMillis/60000)%60);
        int second = (int)((timeInMillis/1000)%60);
        int decim = Math.round((timeInMillis%1000)/100);
        String time;

        if(minut<1) {
            time = String.format(Locale.ENGLISH,"%d.%01d", second, decim);
        }else if (minut<60){
            time = String.format(Locale.ENGLISH,"%d:%02d.%01d",minut,second,decim);
        }else {
            int hour = (int)((timeInMillis/3600000)%24);
            time = String.format(Locale.ENGLISH,"%d:%02d:%02d.%01d",hour,minut,second,decim);
        }
        return time;
    }

    public static String getTimeString2 (Long timeInMillis){

        //формируем формат строки показа времени
        int minut = (int)((timeInMillis/60000)%60);
        int second = (int)((timeInMillis/1000)%60);
        int decim = Math.round((timeInMillis%1000)/10);
        String time;

        if(minut<1) {
            time = String.format(Locale.ENGLISH,"%d.%02d", second, decim);
        }else if (minut<60){
            time = String.format(Locale.ENGLISH,"%d:%02d.%02d",minut,second,decim);
        }else {
            int hour = (int)((timeInMillis/3600000)%24);
            time = String.format(Locale.ENGLISH,"%d:%02d:%02d.%02d",hour,minut,second,decim);
        }
        return time;
    }

    public static String getTimeString3 (Long timeInMillis){

        //формируем формат строки показа времени
        int minut = (int)((timeInMillis/60000)%60);
        int second = (int)((timeInMillis/1000)%60);
        int decim = (int)(timeInMillis%1000);
        String time;

        if(minut<1) {
            time = String.format(Locale.ENGLISH,"%d.%03d", second, decim);
        }else if (minut<60){
            time = String.format(Locale.ENGLISH,"%d:%02d.%03d",minut,second,decim);
        }else {
            int hour = (int)((timeInMillis/3600000)%24);
            time = String.format(Locale.ENGLISH,"%d:%02d:%02d.%03d",hour,minut,second,decim);
        }
        return time;
    }

//    //Заполнение списка адаптера данными курсора
//    //потом заменить на CursorAdaptor
//    public static ArrayList<Map<String, Object>>
//    getDataFromCursor(Cursor cursor, ArrayList<Map<String, Object>> data, int accurancy){
//        //проходим по курсору и берём данные
//        if (cursor.moveToFirst()) {
//            // Узнаем индекс каждого столбца
//            int idColumnIndex = cursor.getColumnIndex(TabSet.COLUMN_SET_TIME);
//            do {
//                long time_total = 0;
//                // Используем индекс для получения строки или числа и переводим в милисекунды
//                //чтобы использовать ранее написанные функции getTimeString1
//                long time_now = (long) (cursor.getFloat(idColumnIndex) * 1000);
//                time_total += time_now;
//
//                Log.d(TAG, "TimeGrafActivity cursor.getPosition()+1 = " +
//                        (cursor.getPosition() + 1) + "  time_now = " + time_now +
//                        "  time_total = " + time_total);
//
//                //Делаем данные для адаптера
//                String s_item = Integer.toString(cursor.getPosition() + 1);
//                String s_time;
//                String s_delta;
//
//                switch (accurancy) {
//                    case 1:
//                        s_time = getTimeString1(time_total);
//                        s_delta = getTimeString1(time_now);
//                        break;
//                    case 2:
//                        s_time = getTimeString2(time_total);
//                        s_delta = getTimeString2(time_now);
//                        break;
//                    case 3:
//                        s_time = getTimeString3(time_total);
//                        s_delta = getTimeString3(time_now);
//                        break;
//                    default:
//                        s_time = getTimeString1(time_total);
//                        s_delta = getTimeString1(time_now);
//                }
//                //заводим данные для одной строки списка
//                Map<String, Object> m;
//                m = new HashMap<>();
//                m.put(ATTR_ITEM_GRAF, s_item);
//                m.put(ATTR_TIME_GRAF, s_time);
//                m.put(ATTR_DELTA_GRAF, s_delta);
//                //добавляем данные в конец ArrayList
//                data.add(m);
//            } while (cursor.moveToNext());
//        }
//                return  data;
//    }

    public static String getTimeString1_Float (float timeFloat){

        float timeInMillis = timeFloat*1000;

        //формируем формат строки показа времени
        int minut = (int)((timeInMillis/60000)%60);
        int second = (int)((timeInMillis/1000)%60);
        int decim = Math.round((timeInMillis%1000)/100);
        String time;

        if(minut<1) {
            time = String.format(Locale.ENGLISH,"%d.%01d", second, decim);
        }else if (minut<60){
            time = String.format(Locale.ENGLISH,"%d:%02d.%01d",minut,second,decim);
        }else {
            int hour = (int)((timeInMillis/3600000)%24);
            time = String.format(Locale.ENGLISH,"%d:%02d:%02d.%01d",hour,minut,second,decim);
        }
        return time;
    }

    public static String getTimeString2_Float (float timeFloat){

        float timeInMillis = timeFloat*1000;

        //формируем формат строки показа времени
        int minut = (int)((timeInMillis/60000)%60);
        int second = (int)((timeInMillis/1000)%60);
        int decim = Math.round((timeInMillis%1000)/10);
        String time;

        if(minut<1) {
            time = String.format(Locale.ENGLISH,"%d.%02d", second, decim);
        }else if (minut<60){
            time = String.format(Locale.ENGLISH,"%d:%02d.%02d",minut,second,decim);
        }else {
            int hour = (int)((timeInMillis/3600000)%24);
            time = String.format(Locale.ENGLISH,"%d:%02d:%02d.%02d",hour,minut,second,decim);
        }
        return time;
    }

    public static String getTimeString3_Float (float timeFloat){

        float timeInMillis = timeFloat*1000;

        //формируем формат строки показа времени
        int minut = (int)((timeInMillis/60000)%60);
        int second = (int)((timeInMillis/1000)%60);
        int decim = (int)(timeInMillis%1000);
        String time;

        if(minut<1) {
            time = String.format(Locale.ENGLISH,"%d.%03d", second, decim);
        }else if (minut<60){
            time = String.format(Locale.ENGLISH,"%d:%02d.%03d",minut,second,decim);
        }else {
            int hour = (int)((timeInMillis/3600000)%24);
            time = String.format(Locale.ENGLISH,"%d:%02d:%02d.%03d",hour,minut,second,decim);
        }
        return time;
    }


}
