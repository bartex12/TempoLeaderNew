package ru.barcats.tempo_leader_javanew.ui.tempoleader.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.editor.EditorViewModel;

/**
 * Created by Андрей on 30.04.2020.
 */
public class DialogChangeTemp extends DialogFragment {

    private static String TAG = "33333";
    private EditorViewModel editorViewModel;
    private boolean up =true;
    private String finishFileName; //имя файла, передаваемое в аргументах фрагмента

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //конструктор по умолчанию
    public DialogChangeTemp(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DialogChangeTempFragment: onCreate  ");
        if ((getArguments()) != null){
            //имя файла из аргументов
            finishFileName = getArguments().getString(P.NAME_OF_FILE);
            editorViewModel = new ViewModelProvider(
                    requireActivity()).get(EditorViewModel.class);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder bilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_chahge_temp, null);

        final EditText valueChangTemp = view.findViewById(R.id.editTextChangeTempValue);

        final RadioGroup radioGroup =view.findViewById(R.id.radioGroupTempUpDown);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonTempUp:
                        up = true;
                        break;
                    case R.id.radioButtonTempDown:
                        up = false;
                        break;
                }
            }
        });
        //так как в макете это уже есть, здесь не надо
        //value.requestFocus();
        //value.setInputType(InputType.TYPE_CLASS_NUMBER);
        int valueOfDelta = getArguments().getInt(P.ARG_VALUE_CHANGE_TEMP, 10);
        valueChangTemp.setText(String.valueOf(valueOfDelta));
        bilder.setView(view);
        bilder.setTitle("Введите величину изменения темпа, %");
        bilder.setIcon(R.drawable.ic_swap_vert_black_32dp);

        bilder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //вызываем ViewModel редактора и изменяем темп - если делаать свой
                //ViewModel в диалоге - отмена будет невозможна, а если делать через
                //контроллер возврат в редактор, ещё и копия файла не будет удаляться
                editorViewModel.changeTemp(finishFileName,
                        Integer.parseInt(valueChangTemp.getText().toString()), up);
                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                takeOnAndOffSoftInput();
            }
        });

        bilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                takeOnAndOffSoftInput();

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

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    private void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) requireActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
