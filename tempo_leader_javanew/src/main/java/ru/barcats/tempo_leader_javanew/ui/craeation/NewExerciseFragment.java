package ru.barcats.tempo_leader_javanew.ui.craeation;

import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

public class NewExerciseFragment extends Fragment {

    public static final String TAG ="33333";

    private EditText fileName, delay;
    private EditText time1, time2, time3, time4, time5, reps1, reps2, reps3, reps4, reps5 ;
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frafment_new_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tempDBHelper = new TempDBHelper(getActivity());
        database = tempDBHelper.getWritableDatabase();

        //разрешить только портретную ориентацию экрана
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fileName = view.findViewById(R.id.etFileName);
        delay = view.findViewById(R.id.etDelay);

        time1 = view.findViewById(R.id.time1);
        time2 = view.findViewById(R.id.time2);
        time3 = view.findViewById(R.id.time3);
        time4 = view.findViewById(R.id.time4);
        time5 = view.findViewById(R.id.time5);

        reps1 = view.findViewById(R.id.reps1);
        reps2 = view.findViewById(R.id.reps2);
        reps3 = view.findViewById(R.id.reps3);
        reps4 = view.findViewById(R.id.reps4);
        reps5 = view.findViewById(R.id.reps5);

        Button create = view.findViewById(R.id.buttonCreate);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (delay.getText().toString().equals("")){
                    delay.setText("0");
                }
                int delayInt = Integer.parseInt(delay.getText().toString());

                String[] time = {time1.getText().toString(),time2.getText().toString(),
                        time3.getText().toString(),time4.getText().toString(),time5.getText().toString()};

                String[] reps = {reps1.getText().toString(),reps2.getText().toString(),
                        reps3.getText().toString(),reps4.getText().toString(),reps5.getText().toString()};
                int size =  time.length;

                float[] timeArray = new float[size];
                int[] repsArray = new int[size];

                for (int i = 0; i< size; i++) {
                    if (time[i].equals("")) {
                        time[i] = "0";
                    }
                    if (reps[i].equals("")) {
                        reps[i] = "0";
                    }

                    timeArray[i]= Float.parseFloat(time[i]);
                    repsArray[i] = Integer.parseInt(reps[i]);
                }

                double resalt = 0;
                for (int i = 0; i< timeArray.length; i++) {
                    resalt += timeArray[i] * repsArray[i];
                }
                Log.d(TAG, "  *****  resalt  *****  = " +resalt);

                String fileNameStr = fileName.getText().toString();
                long fileId = TabFile.getIdFromFileName(database, fileNameStr);
                Log.d(TAG, "fileNameStr = " +fileNameStr + "  fileId = " +fileId);

                //если имя - пустая строка
                if (fileNameStr.trim().isEmpty()){
                    Snackbar.make(v, R.string.InputNameOfSchelule, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое имя раскладки ");
                    //если такое имя уже есть в базе
                }else if (fileId != -1) {
                    Snackbar.make(v, R.string.InputAnotherName, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d(TAG, "Такое имя уже существует. Введите другое имя. fileId = " +fileId);
                    //если во всех фрагментах есть нулевые поля
                }else  if (resalt == 0) {
                    Snackbar.make(v, "Заполните хотя бы один фрагмент подхода.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.d(TAG, "Заполните хотя бы один фрагмент подхода  resalt = " + resalt);
                    //если имя не повторяется, оно не пустое и заполнен хотя бы один фрагмент
                }else {
                    Log.d(TAG, "Такое имя отсутствует fileId = " + fileId);
                    //получаем дату и время в нужном для базы данных формате
                    String dateFormat = tempDBHelper.getDateString();
                    String timeFormat = tempDBHelper.getTimeString();

                    //создаём экземпляр класса DataFile в конструкторе
                    DataFile file = new DataFile(fileNameStr,
                            dateFormat, timeFormat, null,
                            null, P.TYPE_TEMPOLEADER, delayInt);
                    //добавляем запись в таблицу TabFile, используя данные DataFile и получаем id записи
                    long file1_id = TabFile.addFile(database, file);
                    Log.d(TAG, "Добавили   fileNameStr = " + fileNameStr + " file1_id = " + file1_id);

                    int j = 1;
                    for (int i = 0; i<timeArray.length; i++){
                        if ((timeArray[i]!=0)&&(repsArray[i]!=0)){
                            DataSet set = new DataSet(timeArray[i],repsArray[i], j);
                            TabSet.addSet(database, set, file1_id);
                            j++;
                        }
                    }
                    TabSet.rerangeSetFragments(database, file1_id);
                    Log.d(TAG, "NewExerciseFragment count = " +
                            TabSet.getSetFragmentsCount(database, file1_id));

                    //переходим в темполидер с созданным файлом
                    NavController controller = Navigation.findNavController(view);
                    Bundle bundle = new Bundle();
                    bundle.putString(P.NAME_OF_FILE, fileNameStr);
                    bundle.putInt(P.FROM_ACTIVITY, P.NEW_EXERCISE_ACTIVITY);  //555 - NewExerciseFragment
                    controller.navigate(R.id.action_new_exercise_to_nav_tempoleader, bundle);
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //return super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.pacemaker,menu);
//        Log.d(TAG, "onCreateOptionsMenu");
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case android.R.id.home:
//                Log.d(TAG, "Домой");
//                Intent intentHome = new Intent(this, MainActivity.class);
//                startActivity(intentHome);
//                finish();
//                return true;
//
//            case R.id.action_settings:
//                //вызываем ListOfFilesActivity
//                Intent intentPref = new Intent(getBaseContext(), PrefActivity.class);
//                startActivity(intentPref);
//                //finish();  //не нужно
//                return true;
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
