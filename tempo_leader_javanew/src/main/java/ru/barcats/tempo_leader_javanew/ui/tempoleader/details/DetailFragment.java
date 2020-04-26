package ru.barcats.tempo_leader_javanew.ui.tempoleader.details;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

public class DetailFragment extends Fragment {

    private EditText mTimeOfRepFrag;  //поле для времени между повторами
    private EditText mRepsFrag;  // поле для количества повторениийдля фрагмента подхода
    private EditText mNumberOfFrag;  //порядковый номер фрагмента подхода
    private Button mButtonOk;
    private Button mButtonCancel;


    private static final String TAG = "33333";
   // private  Bundle extras;
    private DataSet mDataSet;
    private int fragmentCount;  //количество фрагментов подхода
    private int fragmentNumber;  //номер фрагмента для Вставить до/после
    private long fileId; //id файла, в который добавляем новый фрагмент подхода
    private String finishFileName; //имя файла для обратной отправки
    private SQLiteDatabase database;

    //редактируемая запись появляется в списке, только если нажата кнопка Принять
    //кнопка Назад в панели инструментов, кнопка отмена и кнопка Обратно на телефоне - отменяют
    //редактирование строки


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // вызываем здесь, закрываем в onDestroy()
        database = new TempDBHelper(requireActivity()).getWritableDatabase();
        if (getArguments() != null) {
          int from =  getArguments().getInt(P.FROM_ACTIVITY);
          switch (from){
              case P.TO_ADD_LAST_SET:
                  finishFileName = getArguments().getString(P.NAME_OF_FILE);
//                  fragmentCount = TabSet.getSetFragmentsCount(database, fileId);
//                  mDataSet = new DataSet();
//                  mDataSet.setNumberOfFrag(fragmentCount + 1);
                  Log.d(TAG, "DetailFragment TO_ADD_LAST_SET " +
                          "onCreate Добавить с тулбара редактора  fileId= " +fileId);
                  break;
              case P.TO_INSERT_AFTER_FRAG:
                  finishFileName = getArguments().getString(P.NAME_OF_FILE);
//                  fragmentNumber =getArguments().getInt(P.INTENT_TO_DETILE_FILE_POSITION);
//                  mDataSet = new DataSet();
//                  mDataSet.setNumberOfFrag(fragmentNumber + 1);
                  break;
              case P.TO_INSERT_BEFORE_FRAG:
                  finishFileName = getArguments().getString(P.NAME_OF_FILE);
//                  fragmentNumber = getArguments().getInt(P.INTENT_TO_DETILE_FILE_POSITION);
//                  mDataSet = new DataSet();
//                  mDataSet.setNumberOfFrag(fragmentNumber);
                  break;
              case P.DETAIL_CHANGE_TEMP:
                 //TODO
                  break;

          }

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "/$$$/  DetailFragment onCreateView" );
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        setOkListener();
        setCancelListener();
        setTimeButtonlListener();
        setRepsButtonlListener();
        setNumberOfFrag();

        //Устанавливаем фокус ввода в поле mTimeOfRepFrag
        mTimeOfRepFrag.requestFocus();
        //Вызываем экранную клавиатуру -метод работает как в 4.0.3, так и в 6.0
        showSoftInputOn();
    }

    private void setNumberOfFrag() {
        //записываем в поле mNumberOfFrag порядковый номер фрагмента подхода +1 (на экране - с 1)
        //если Добавить с тулбара редактора +
        if(getArguments().getInt(P.FROM_ACTIVITY) == P.TO_ADD_LAST_SET){
            mNumberOfFrag.setText(String.valueOf(fragmentCount + 1));
            Log.d(TAG, " TO_ADD_LAST_SET mNumberOfFrag = " + (fragmentCount + 1));

            //если вставить после  из контекстного меню редактора
        }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_AFTER_FRAG){
            mNumberOfFrag.setText(String.valueOf(fragmentNumber + 1));
            Log.d(TAG, " TO_INSERT_AFTER_FRAG щелчок на фрагменте = " + fragmentCount);

            //если вставить до   из контекстного меню редактора
        }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_BEFORE_FRAG){
            mNumberOfFrag.setText(String.valueOf(fragmentNumber));
            Log.d(TAG, " TO_INSERT_BEFORE_FRAG щелчок на фрагменте = " + fragmentCount);

            //если Изменить из контекстного меню редактора
        }else if (getArguments().getInt(P.FROM_ACTIVITY) == P.DETAIL_CHANGE_TEMP){
            mNumberOfFrag.setText(String.valueOf(mDataSet.getNumberOfFrag()));
            Log.d(TAG, " DETAIL_CHANGE_TEMP_REQUEST_CODE mNumberOfFrag = " + mDataSet.getNumberOfFrag());
        }
    }

    private void setRepsButtonlListener() {
        //если Добавить с тулбара редактора  +
        if(getArguments().getInt(P.FROM_ACTIVITY) == P.TO_ADD_LAST_SET){
            mRepsFrag.setText("0");
            //если вставить после  из контекстного меню редактора
        }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_AFTER_FRAG){
            mRepsFrag.setText("0");
            //если вставить до   из контекстного меню редактора
        }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_BEFORE_FRAG){
            mRepsFrag.setText("0");
            //если Изменить из контекстного меню редактора
        } else if (getArguments().getInt(P.FROM_ACTIVITY) == P.DETAIL_CHANGE_TEMP) {
            mRepsFrag.setText(String.valueOf(mDataSet.getReps()));
        }
        mRepsFrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //если Изменить из контекстного меню редактора
                if (getArguments().getInt(P.FROM_ACTIVITY) == P.DETAIL_CHANGE_TEMP) {
                    //получаем int количество повторений для фрагмента из строчки mRepsFrag
                    int ir = getCountReps(mRepsFrag);
                    //и присваиваем его переменной mReps класса Set
                    mDataSet.setReps(ir);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep()!=0))&&((mDataSet.getReps()!=0)));
                    Log.d(TAG, "DETAIL_CHANGE_TEMP_REQUEST_CODE countReps = " + mDataSet.getReps());

                    //если Добавить с тулбара редактора +
                }else if (getArguments().getInt(P.FROM_ACTIVITY) == P.TO_ADD_LAST_SET) {
                    //получаем int количество повторений для фрагмента из строчки mRepsFrag
                    int ir = getCountReps(mRepsFrag);
                    //и присваиваем его переменной mReps класса Set
                    mDataSet.setReps(ir);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep() != 0)) && ((mDataSet.getReps() != 0)));
                    Log.d(TAG, " TO_ADD_SET countReps = " + mDataSet.getReps());

                    //если вставить после  из контекстного меню редактора
                }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_AFTER_FRAG){
                    //получаем int количество повторений для фрагмента из строчки mRepsFrag
                    int ir = getCountReps(mRepsFrag);
                    //и присваиваем его переменной mReps класса Set
                    mDataSet.setReps(ir);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep() != 0)) && ((mDataSet.getReps() != 0)));
                    Log.d(TAG, " TO_INSERT_AFTER_FRAG countReps = " + mDataSet.getReps());

                    //если вставить до   из контекстного меню редактора
                }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_BEFORE_FRAG){
                    //получаем int количество повторений для фрагмента из строчки mRepsFrag
                    int ir = getCountReps(mRepsFrag);
                    //и присваиваем его переменной mReps класса Set
                    mDataSet.setReps(ir);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep() != 0)) && ((mDataSet.getReps() != 0)));
                    Log.d(TAG, " TO_INSERT_BEFORE_FRAG countReps = " + mDataSet.getReps());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setTimeButtonlListener() {
        //если Добавить с тулбара редактора  +
        if(getArguments().getInt(P.FROM_ACTIVITY) == P.TO_ADD_LAST_SET) {
            mTimeOfRepFrag.setText("0");
            //если вставить после  из контекстного меню редактора
        }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_AFTER_FRAG){
            mTimeOfRepFrag.setText("0");
            //если вставить до   из контекстного меню редактора
        }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_BEFORE_FRAG){
            mTimeOfRepFrag.setText("0");
            //если Изменить из контекстного меню редактора
        }else if (getArguments().getInt(P.FROM_ACTIVITY) == P.DETAIL_CHANGE_TEMP){
            mTimeOfRepFrag.setText(String.valueOf(mDataSet.getTimeOfRep()));
        }
        mTimeOfRepFrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //если Изменить из контекстного меню редактора
                if (getArguments().getInt(P.FROM_ACTIVITY) == P.DETAIL_CHANGE_TEMP) {
                    //получаем float секунд для времени между повторами из строчки mTimeOfRepFrag
                    float ft = getCountSecond(mTimeOfRepFrag);
                    //и присваиваем его переменной mTimeOfRep класса DataSet
                    mDataSet.setTimeOfRep(ft);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep()!=0))&&((mDataSet.getReps()!=0)));
                    Log.d(TAG, " DETAIL_CHANGE_TEMP_REQUEST_CODE countSecond = " + mDataSet.getTimeOfRep() +
                            "countReps = " + mDataSet.getReps());

                    //если Добавить с тулбара редактора  +
                }else if (getArguments().getInt(P.FROM_ACTIVITY) == P.TO_ADD_LAST_SET){
                    //получаем float секунд для времени между повторами из строчки mTimeOfRepFrag
                    float ft = getCountSecond(mTimeOfRepFrag);
                    //и присваиваем его переменной mTimeOfRep класса Set
                    mDataSet.setTimeOfRep(ft);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep()!=0))&&((mDataSet.getReps()!=0)));
                    Log.d(TAG, " TO_ADD_LAST_SET countSecond = " + mDataSet.getTimeOfRep() +
                            "countReps = " + mDataSet.getReps());

                    //если вставить после  из контекстного меню редактора
                }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_AFTER_FRAG){
                    //получаем float секунд для времени между повторами из строчки mTimeOfRepFrag
                    float ft = getCountSecond(mTimeOfRepFrag);
                    //и присваиваем его переменной mTimeOfRep класса Set
                    mDataSet.setTimeOfRep(ft);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep()!=0))&&((mDataSet.getReps()!=0)));
                    Log.d(TAG, " TO_INSERT_AFTER_FRAG countSecond = " + mDataSet.getTimeOfRep() +
                            "countReps = " + mDataSet.getReps());

                    //если вставить до   из контекстного меню редактора
                }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_BEFORE_FRAG){
                    //получаем float секунд для времени между повторами из строчки mTimeOfRepFrag
                    float ft = getCountSecond(mTimeOfRepFrag);
                    //и присваиваем его переменной mTimeOfRep класса Set
                    mDataSet.setTimeOfRep(ft);
                    //доступность кнопки Ok, если оба значения ненулевые
                    mButtonOk.setEnabled(((mDataSet.getTimeOfRep()!=0))&&((mDataSet.getReps()!=0)));
                    Log.d(TAG, " TO_INSERT_BEFORE_FRAG countSecond = " + mDataSet.getTimeOfRep() +
                            "countReps = " + mDataSet.getReps());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setCancelListener() {
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //прячем экранную клавиатуру
                takeOffSoftInput();
                //завершаем
                //finish();
            }
        });
    }

    private void setOkListener() {
        //доступность кнопки Ok в момент появления экрана редактирования (если изменить - доступна)
        if ((Float.parseFloat( mTimeOfRepFrag.getText().toString())==0) &&
                ((Integer.parseInt( mRepsFrag.getText().toString())==0))){
            mButtonOk.setEnabled(false);
        }

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //если Добавить с тулбара редактора  +
                if (getArguments().getInt(P.FROM_ACTIVITY) == P.TO_ADD_LAST_SET) {
                    //Добавляем фрагмент подхода
                    TabSet.addSet(database, mDataSet, fileId);
                    Log.d(TAG, "mButtonOk (P.FROM_ACTIVITY) == P.TO_ADD_LAST_SET ");


                    //если вставить после  из контекстного меню редактора
                }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_AFTER_FRAG){
                    //вставляем фрагмент подхода после позиции, на которой сделан щелчок
                    TabSet.addSetAfter(database, mDataSet, fileId, fragmentNumber);
                    Log.d(TAG, "mButtonOk (P.FROM_ACTIVITY) == P.TO_INSERT_AFTER_FRAG ");


                    //если вставить до  из контекстного меню редактора
                }else if (getArguments().getInt(P.FROM_ACTIVITY) ==P.TO_INSERT_BEFORE_FRAG){
                    //вставляем фрагмент подхода после позиции, на которой сделан щелчок
                    TabSet.addSetBefore(database, mDataSet, fileId, fragmentNumber);
                    Log.d(TAG, "mButtonOk (P.FROM_ACTIVITY) == P.TO_INSERT_BEFORE_FRAG ");


                    //если Изменить из контекстного меню редактора
                }else if(getArguments().getInt(P.FROM_ACTIVITY) == P.DETAIL_CHANGE_TEMP){
                    //обновляем фрагмент подхода в базе данных
                    TabSet.updateSetFragment(database, mDataSet);
                    Log.d(TAG, "mButtonOk (P.DETAIL_CHANGE_REQUEST) == P.DETAIL_CHANGE_TEMP_REQUEST_CODE");

                }
                //прячем экранную клавиатуру
                takeOffSoftInput();

            }
        });
    }

    private void initViews(@NonNull View view) {
        mButtonOk = view.findViewById(R.id.buttonOk);
        mButtonCancel = view.findViewById(R.id.buttonCancel);
        mTimeOfRepFrag = view.findViewById(R.id.time_item_set_editText);
        mRepsFrag = view.findViewById(R.id.reps_item_set_editText);
        mNumberOfFrag = view.findViewById(R.id.mark_item_set_editText);
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //перевоим текст в миллисекунды для времени между повторениями одного фрагмента
    private float getCountSecond(EditText time) {
        String s = time.getText().toString();
        if ((s.equals("")) ||(s.equals("."))) {
            return 0;
        } else return Float.parseFloat(time.getText().toString());
    }
    //getCountMilliSecond
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
