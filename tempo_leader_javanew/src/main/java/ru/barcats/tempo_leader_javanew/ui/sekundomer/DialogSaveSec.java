package ru.barcats.tempo_leader_javanew.ui.sekundomer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.DialogFragment;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.P;

/**
 * Created by Андрей on 10.05.2018.
 */
public class DialogSaveSec extends DialogFragment {

    private static String TAG = "33333";
    private TempDBHelper mTempDBHelper;
    private SQLiteDatabase database;
    private SaveSecListener saveSectListener;

    public DialogSaveSec(){}

    //интерфейс для передачи данных из фрагмента в активность
    public interface SaveSecListener{
        void onNameAndGrafTransmit(String nameFile, boolean showGraf, boolean cancel);
    }

    public void setSaveSectListener(SaveSecListener saveSectListener) {
        this.saveSectListener = saveSectListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //saveSectListener = (SaveSecListener)context;
        mTempDBHelper = new TempDBHelper(context);
        database = new TempDBHelper(context).getWritableDatabase();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        database.close();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.save_data_timemeter, null);
        final EditText name = view.findViewById(R.id.etNameOfFileSec);
        final CheckBox dateCheckBox = view.findViewById(R.id.cbDateSec);
        name.requestFocus();
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(view);
        builder.setTitle("Сохранить как");
        builder.setIcon(R.drawable.ic_save_black_24dp);

        Button btnSaveYes = view.findViewById(R.id.buttonSaveYesSec);
        //действия при нажатии кнопки "Сохранить" в диалоге сохранения данных в базу
        btnSaveYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя файла в строке ввода
                String nameFile = name.getText().toString();

                if(dateCheckBox.isChecked()){
                    nameFile = nameFile + "_" + P.setDateString();
                    Log.d(TAG, "SaverFragmentSecundomer date.isChecked() Имя файла = " + nameFile);
                }

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long fileId = TabFile.getIdFromFileName(database, nameFile);
                Log.d(TAG, "nameFile = " + nameFile + "  fileId = " +fileId);

                //если имя - пустая строка
                if (nameFile.trim().isEmpty()){
                    Snackbar.make(view, "Введите непустое имя раскладки", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое имя раскладки ");
                    //если такое имя уже есть в базе
                }else if (fileId != -1) {
                    Snackbar.make(view, "Такое имя уже существует. Введите другое имя.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d(TAG, "Такое имя уже существует. Введите другое имя. fileId = " +fileId);
                    //если имя не повторяется, оно не пустое то
                }else {
                    Log.d(TAG, "Такое имя отсутствует fileId = " + fileId);

                    //TODO
                    //Вызываем метод интерфейса, имя файла в TimeMeterActivity
                    saveSectListener.onNameAndGrafTransmit(nameFile, false, false);

                    //принудительно прячем  клавиатуру - повторный вызов ее покажет
                    takeOnAndOffSoftInput();

                    //getActivity().finish(); //закрывает и диалог и активность
                    getDialog().dismiss();  //закрывает только диалог
                }
            }
        });

        Button btnSaveNo = view.findViewById(R.id.btnSaveNoSec);
        //действия при нажатии кнопки "Нет" в диалоге сохранения данных в базу
        btnSaveNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //здесь имя не будет использоваться дальше, поэтому так
                String nameFile = name.getText().toString();
                //TODO
                //Вызываем метод интерфейса, имя файла в TimeMeterActivity
                saveSectListener.onNameAndGrafTransmit(nameFile, false, true);
                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                takeOnAndOffSoftInput();

                //getActivity().finish(); //закрывает и диалог и активность
                getDialog().dismiss();  //закрывает только диалог
            }
        });

        Button btnSaveYesShow = view.findViewById(R.id.buttonSaveYesSecShow);
        //действия при нажатии кнопки "Сохранить и показать" в диалоге сохранения данных в базу
        btnSaveYesShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //читаем имя файла в строке ввода
                String nameFile = name.getText().toString();
                if(dateCheckBox.isChecked()) {
                    nameFile = nameFile + "_" + P.setDateString();
                    Log.d(TAG, "SaverFragmentSecundomer date.isChecked() Имя файла = " + nameFile);
                }
                    //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                    long fileId = TabFile.getIdFromFileName(database, nameFile);
                    Log.d(TAG, "nameFile = " + nameFile + "  fileId = " +fileId);

                    //если имя - пустая строка
                    if (nameFile.trim().isEmpty()){
                        Snackbar.make(view, "Введите непустое имя раскладки", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.d(TAG, "Введите непустое имя раскладки ");
                        //если такое имя уже есть в базе
                    }else if (fileId != -1) {
                        Snackbar.make(view, "Такое имя уже существует. Введите другое имя.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.d(TAG, "Такое имя уже существует. Введите другое имя. fileId = " +fileId);
                        //если имя не повторяется, оно не пустое то
                    }else {
                        Log.d(TAG, "Такое имя отсутствует fileId = " + fileId);

                        //TODO
                        //Вызываем метод интерфейса, имя файла в TimeMeterActivity
                        saveSectListener.onNameAndGrafTransmit(nameFile, true, false);

                        //принудительно прячем  клавиатуру - повторный вызов ее покажет
                        takeOnAndOffSoftInput();

                        //getActivity().finish(); //закрывает и диалог и активность
                        getDialog().dismiss();  //закрывает только диалог
                    }
                }
        });

        //если не делать запрет на закрытие окна при щелчке за пределами окна, то можно так
        //return bilder.create();
        //А если делать запрет, то так
        Dialog  dialog = builder.create();
        //запрет на закрытие окна при щелчке за пределами окна
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    private void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) requireActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
