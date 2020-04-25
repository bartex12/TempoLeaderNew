package ru.barcats.tempo_leader_javanew.ui.tempoleader.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

import ru.barcats.tempo_leader_javanew.ui.tempoleader.editor.EditorViewModel;

public class NewSetDialog extends DialogFragment {

    private static final String TAG = "33333";
    private EditText mTimeOfRepFrag;  //поле для времени между повторами
    private EditText mRepsFrag;  // поле для количества повторениийдля фрагмента подхода
    private EditText mNumberOfFrag;  //порядковый номер фрагмента подхода
    private Button mButtonOk;
    private Button mButtonCancel;
    private String finishFileName; //имя файла, передаваемое в аргументах фрагмента
    private  TempDBHelper mTempDBHelper;
    private SQLiteDatabase database;
    private EditorViewModel editorViewModel;
    private DataSet mDataSet;
    private int from;
    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTempDBHelper = new TempDBHelper(context);
        database = new TempDBHelper(context).getWritableDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "NewSetDialog: onDestroy  ");
        database.close();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "NewSetDialog: onCreate  ");

        if ((getArguments()) != null){
            //имя файла из аргументов
            finishFileName = getArguments().getString(P.NAME_OF_FILE);
            from = getArguments().getInt(P.FROM_ACTIVITY);

            editorViewModel = new ViewModelProvider(requireActivity()).get(EditorViewModel.class);
            mDataSet = editorViewModel.getDataSetNew(finishFileName);

            Log.d(TAG, "NewSetDialog: onCreate  numberFrag = " + mDataSet.getNumberOfFrag());
        }else finishFileName = "";
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater =requireActivity().getLayoutInflater();
        rootView = inflater.inflate(R.layout.fragment_dialog_new_set, null);

        builder.setView(rootView);
        builder.setTitle("Создать");
        builder.setIcon(R.drawable.ic_fiber_new_black_24dp);

        initViews(rootView);

        setOkListener();
        setCancelListener();
        setTimeButtonlListener();
        setRepsButtonlListener();

        AlertDialog  dialog = builder.create();
        //запрет на закрытие окна при щелчке за пределами окна
        dialog.setCanceledOnTouchOutside(false);
        //Вызываем экранную клавиатуру -метод работает как в 4.0.3, так и в 6.0
        showSoftInputOn();
        return dialog;
    }

    private void setOkListener() {
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //если Добавить с тулбара редактора  +
                if (getArguments().getInt(P.FROM_ACTIVITY) == P.TO_ADD_FRAG) {
                    editorViewModel.addSet(mDataSet, finishFileName);
                    Log.d(TAG, "NewSetDialog mButtonOk (P.FROM_ACTIVITY) == P.TO_ADD_FRAG ");
                }
                //прячем экранную клавиатуру
                takeOffSoftInput();
                getDialog().dismiss();  //закрывает только диалог
            }
        });
    }

    private void setCancelListener() {
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();  //закрывает только диалог
            }
        });
    }

    private void initViews(View view) {

        mTimeOfRepFrag = view.findViewById(R.id.time_item_newset);
        if(from == P.TO_ADD_FRAG) {
            mTimeOfRepFrag.setText(String.valueOf(mDataSet.getTimeOfRep()));
        }

        mRepsFrag = view.findViewById(R.id.reps_item_newset);
        //если Добавить с тулбара редактора  +
        if(from == P.TO_ADD_FRAG){
            mRepsFrag.setText(String.valueOf(mDataSet.getReps()));
        }

        mNumberOfFrag = view.findViewById(R.id.mark_item_newset);
        if(from == P.TO_ADD_FRAG){
            mNumberOfFrag.setText(String.valueOf(mDataSet.getNumberOfFrag()));
        }

        mButtonOk = view.findViewById(R.id.buttonOk_newset);
        //доступность кнопки Ok в момент появления экрана редактирования (если изменить - доступна)
        if ((Float.parseFloat( mTimeOfRepFrag.getText().toString())==0) &&
                ((Integer.parseInt( mRepsFrag.getText().toString())==0))){
            mButtonOk.setEnabled(false);
        }

        mButtonCancel = view.findViewById(R.id.buttonCancel_newset);
    }

    private void setRepsButtonlListener() {

        mRepsFrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //если Изменить из контекстного меню редактора
                if (getArguments().getInt(P.FROM_ACTIVITY) == P.TO_ADD_FRAG) {
                    //получаем int количество повторений для фрагмента из строчки mRepsFrag
                    int ir = getCountReps(mRepsFrag);
                    //и присваиваем его переменной mReps класса Set
                    mDataSet.setReps(ir);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep() != 0)) && ((mDataSet.getReps() != 0)));
                    Log.d(TAG, " TO_ADD_SET countReps = " + mDataSet.getReps());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setTimeButtonlListener() {

        mTimeOfRepFrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //если Добавить с тулбара редактора  +
                if (getArguments().getInt(P.FROM_ACTIVITY) == P.TO_ADD_FRAG){
                    //получаем float секунд для времени между повторами из строчки mTimeOfRepFrag
                    float ft = getCountSecond(mTimeOfRepFrag);
                    //и присваиваем его переменной mTimeOfRep класса Set
                    mDataSet.setTimeOfRep(ft);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep()!=0))&&((mDataSet.getReps()!=0)));
                    Log.d(TAG, " TO_ADD_FRAG countSecond = " + mDataSet.getTimeOfRep() +
                            " countReps = " + mDataSet.getReps());
                                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //перевоим текст в миллисекунды для времени между повторениями одного фрагмента
    private float getCountSecond(EditText time) {
        String s = time.getText().toString();
        if ((s.equals("")) ||(s.equals("."))) {
            return 0;
        } else return Float.parseFloat(time.getText().toString());
    }

    //переводим текст в цифру для количества повторений одного фрагмента
    private int getCountReps(EditText reps){
        String s = reps.getText().toString();
        if ((s.equals("")) ||(s.equals("."))) {
            return 0;
        }else return Integer.parseInt(reps.getText().toString());
    }

    private void showSoftInputOn() {
        InputMethodManager imm = (InputMethodManager)requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mTimeOfRepFrag, 0);
    }

    private void takeOffSoftInput(){
        //прячем экранную клавиатуру
        InputMethodManager imm = (InputMethodManager)requireActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }

}
