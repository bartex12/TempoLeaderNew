package ru.barcats.tempo_leader_javanew.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.P;

public class DialogChangeFileName extends DialogFragment {
    private static final String TAG = "33333";
    private Dialog dialog;
    private SQLiteDatabase database;
    private Context context;
    private View view;

    public static DialogChangeFileName newInstance(String fileName){
        Bundle args = new Bundle();
        args.putString(P.NAME_OF_FILE, fileName);
        DialogChangeFileName fragment = new DialogChangeFileName();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG, " **** DialogChangeFileName onAttachFragment = ");
        context = childFragment.getContext();
//        database = new TempDBHelper(context).getWritableDatabase();
//        Log.d(TAG, " **** DialogChangeFileName database = " + database);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, " **** DialogChangeFileName onCreateView");

          view = inflater.inflate(R.layout.fragment_dialog_chahge_name, null);

//        database = new TempDBHelper(container.getContext()).getWritableDatabase();
//        Log.d(TAG, " **** DialogChangeFileName database = " + database);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        final String fileNameOld = getArguments().getString(P.NAME_OF_FILE);
//        Log.d(TAG, "**** DialogChangeFileName fileNameOld = " + fileNameOld);
//        //получаем id по имени
//        final long fileIdOld = TabFile.getIdFromFileName(database,fileNameOld);
//        //получаем объект с данными строки с id = acmi.id из  таблицы TabFile
//        final DataFile dataFile = TabFile.getAllFilesData(database, fileIdOld);
//        //принудительно вызываем клавиатуру - повторный вызов ее скроет
//        // takeOnAndOffSoftInput();

        final AlertDialog.Builder changeDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //final View view = inflater.inflate(R.layout.fragment_dialog_chahge_name, null);
        changeDialog.setView(view);
        changeDialog.setTitle("Изменить имя");
        changeDialog.setIcon(R.drawable.ic_wrap_text_black_24dp);
//
//        final EditText name = view.findViewById(R.id.editTextNameOfFile);
//        name.requestFocus();
//        name.setInputType(InputType.TYPE_CLASS_TEXT);
//        //пишем имя файла
//        name.setText(fileNameOld);
//
//        String min = dataFile.getFileNameDate() +
//                getResources().getString(R.string.LowMinus) + dataFile.getFileNameTime();
//        final EditText dateAndTime = view.findViewById(R.id.editTextDateAndTime);
//        dateAndTime.setText(min);
//        dateAndTime.setEnabled(false);
//
//        final CheckBox date = view.findViewById(R.id.checkBoxDate);
//
//        changeDialog.setView(view);
//        changeDialog.setTitle("Изменить имя");
//        changeDialog.setIcon(R.drawable.ic_wrap_text_black_24dp);
//
//        Button saveButYes = view.findViewById(R.id.buttonSaveYesChangeName);
//        saveButYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String  newfileName = fileNameOld;
//                if(date.isChecked()){
//                   newfileName = fileNameOld + "_" + P.setDateString();
//                    Log.d(TAG, "**** DialogChangeFileName date.isChecked() Имя файла = " + newfileName);
//                }
//                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
//                long fileIdNew = TabFile.getIdFromFileName(database, newfileName);
//                Log.d(TAG, "newfileName = " + newfileName + "  fileIdNew = " + fileIdNew);
//
//                //если имя - пустая строка
//                if (newfileName.trim().isEmpty()) {
//                    Snackbar.make(view, "Введите непустое имя раскладки", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                    Log.d(TAG, "Введите непустое имя раскладки ");
//                    //если такое имя уже есть в базе
//                } else if (fileIdNew != -1) {
//                    Snackbar.make(view, "Такое имя уже существует. Введите другое имя.", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                    Log.d(TAG, "Такое имя уже существует. Введите другое имя. fileIdNew = " + fileIdNew);
//                    //если имя не повторяется, оно не пустое и не системный файл то
//                } else {
//                    Log.d(TAG, "Такое имя отсутствует fileIdNew = " + fileIdNew);
//
//                    //изменяем имя файла
//                    TabFile.updateFileName(database, newfileName, fileIdOld);
//
//                    //принудительно прячем  клавиатуру - повторный вызов ее покажет
//                    takeOnAndOffSoftInput();
//
//                    NavController controller = Navigation.findNavController(view);
//                    controller.navigate(R.id.action_dialogChangeFileName_to_nav_rascladki);
//                    //dialog.dismiss();  //закрывает только диалог
//                }
//            }
//        });
//
//        Button saveButNo = view.findViewById(R.id.buttonSaveNoChangeName);
//        saveButNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //принудительно прячем  клавиатуру - повторный вызов ее покажет
//                takeOnAndOffSoftInput();
//                //getActivity().finish(); //закрывает и диалог и активность
//                dialog.dismiss();  //закрывает только диалог
//            }
//        });
        //если делать запрет на закрытие окна при щелчке за пределами окна,
        // то сначала билдер создаёт диалог
        dialog = changeDialog.create();
        //запрет на закрытие окна при щелчке за пределами окна
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    private void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    }

