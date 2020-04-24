package ru.barcats.tempo_leader_javanew.ui.tempoleader.dialogs.dialogsave;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.P;


/**
 * Created by Андрей on 06.05.2018.
 */
public class DialogSaveFragment extends DialogFragment {

    private static String TAG = "33333";
    private String finishFileName; //имя файла, передаваемое в аргументах фрагмента

    private  TempDBHelper mTempDBHelper;
    private SQLiteDatabase database;
    private SaveViewModel saveViewModel;
    private long fileIdCopy;

    public DialogSaveFragment(){}

    public static DialogSaveFragment newInstance(String nameOfFile){
        Bundle args = new Bundle();
        args.putString(P.ARG_NAME_OF_FILE,nameOfFile);
        DialogSaveFragment fragment = new DialogSaveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTempDBHelper = new TempDBHelper(context);
        database = new TempDBHelper(context).getWritableDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DialogSaveTempFragment: onDestroy  ");
        database.close();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DialogSaveTempFragment: onCreate  ");

        if ((getArguments()) != null){
            //имя файла из аргументов
            finishFileName = getArguments().getString(P.NAME_OF_FILE);
            fileIdCopy = getArguments().getLong(P.FINISH_FILE_ID);

            saveViewModel = new ViewModelProvider(requireActivity()).get(SaveViewModel.class);

        }else finishFileName = "";
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //принудительно вызываем клавиатуру - повторный вызов ее скроет
       // takeOnAndOffSoftInput();

        AlertDialog.Builder bilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.save_data_in_file_new, null);

        final EditText name = view.findViewById(R.id.editTextNameOfFile_new);
        name.setText(finishFileName);
        name.requestFocus();
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        bilder.setView(view);
        bilder.setTitle("Сохранить как");
        bilder.setIcon(R.drawable.ic_save_black_24dp);

        final CheckBox date = view.findViewById(R.id.checkBoxDate_new);


        date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String dayTimeFile = "";
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String nameFile = name.getText().toString();;
                if(isChecked){
                    dayTimeFile ="_" + P.setDateString();
                    nameFile = nameFile  + dayTimeFile;
                    name.setText(nameFile);
                    Log.d(TAG, "dayTimeFile = " + dayTimeFile + "  nameFile = " + nameFile);
                    name.setEnabled(false);
                }else {
                    String oldName = nameFile.replace(dayTimeFile,"");
                    name.setText(oldName);
                    Log.d(TAG, "nameFile = " + nameFile + "  oldName = " + oldName);
                    name.setEnabled(true);
                }
            }
        });


        Button btnSaveYes = view.findViewById(R.id.buttonSaveYes_new);
        btnSaveYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameFile = name.getText().toString();

                if(date.isChecked()){
                    nameFile = nameFile + "_" + P.setDateString();
                    Log.d(TAG, "SaverFragment date.isChecked() Имя файла = " + nameFile);
                }
                //если имя - пустая строка
                if (nameFile.trim().isEmpty()) {
                    Snackbar.make(view, "Введите непустое имя раскладки", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое имя раскладки ");
                }else {
                    //если новое имя совпадает со старым, перезаписываем старый файл с новым именем
                    //если имена не совпадают, делаем копию с новым именем
                   saveViewModel.saveAsFile(finishFileName, nameFile, fileIdCopy);

                   //переходим в темполидер
                    NavController controller = Navigation.findNavController(getParentFragment().getView());
                    Bundle bundle = new Bundle();
                    bundle.putString(P.NAME_OF_FILE,nameFile);
                    controller.navigate(R.id.action_dialogSaveTempFragment_to_nav_tempoleader, bundle);
                    //принудительно прячем  клавиатуру - повторный вызов ее покажет
                   // takeOnAndOffSoftInput();
                    getDialog().dismiss();  //закрывает только диалог
                }
            }
        });

        Button btnSaveNo = view.findViewById(R.id.buttonSaveNo_new);
        btnSaveNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                //takeOnAndOffSoftInput();
                getDialog().dismiss();  //закрывает только диалог
            }
        });
        //если не делать запрет на закрытие окна при щелчке за пределами окна, то можно так
        //return bilder.create();
        //А если делать запрет, то так
        AlertDialog  dialog = bilder.create();
        //запрет на закрытие окна при щелчке за пределами окна
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

//    //принудительно вызываем клавиатуру - повторный вызов ее скроет
//    private void takeOnAndOffSoftInput(){
//        InputMethodManager imm = (InputMethodManager) requireActivity().
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//    }



}
