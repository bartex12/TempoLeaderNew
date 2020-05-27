package ru.barcats.tempo_leader_javanew.ui.tempoleader.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.TempoleaderViewModel;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.editor.EditorViewModel;


/**
 * Created by Андрей on 27.05.2018.
 */
public class DialogSetDelay extends DialogFragment {

    private static final String TAG = "33333";
    private EditText editTextDelay;
    private TempoleaderViewModel dataSetViewModel;
    private  String fileName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSetViewModel = new ViewModelProvider(requireActivity()).get(TempoleaderViewModel.class);
        fileName = getArguments().getString(P.NAME_OF_FILE);
        Log.d(TAG, "// ~~~ // DialogSetDelay  fileName "+ fileName);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // в макете сделана установка фокуса, выделение цветом  и цифровая клавиатура
        //android:focusable="true"
        // android:selectAllOnFocus="true"
        // android:inputType="numberDecimal"

        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.set_delay, null);

        editTextDelay = view.findViewById(R.id.editTextDelay);
        editTextDelay.setText( getArguments().getString(P.ARG_DELAY)); //получаем из аргументов
        //так как в макете это уже есть, здесь не надо
        editTextDelay.requestFocus();
        //editTextDelay.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(view);
        builder.setTitle("Установите задержку старта в секундах");

        builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int  delay;
                boolean tempDelay = getDelay();
                if (tempDelay){
                    //читаем задержку в строке ввода
                    delay = Integer.parseInt(editTextDelay.getText().toString());
                    //dataSetViewModel.updateDelayNew(delay, fileName);
                    Log.d(TAG, "// ~~~ // DialogSetDelay  delay "+ delay);

                    //находим NavController в диалоге
                    NavController controller =
                            Navigation.findNavController(getParentFragment().getView());
                    Bundle bundle = new Bundle();
                    bundle.putString(P.NAME_OF_FILE, getArguments().getString(P.NAME_OF_FILE));
                    bundle.putInt(P.ARG_DELAY, delay);
                    bundle.putInt(P.FROM_ACTIVITY, P.DIALOG_DELAY);
                    controller.navigate(R.id.action_nav_set_delay_to_nav_tempoleader, bundle);

                    editTextDelay.clearFocus();

                    //принудительно прячем  клавиатуру - повторный вызов ее покажет
                    takeOnAndOffSoftInput();
                }
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                takeOnAndOffSoftInput();
            }
        });
        //если делать запрет на закрытие окна при щелчке за пределами окна, то можно так
        Dialog  dialog = builder.create();
        //getDialog().getWindow().
         //       setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    private void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) requireActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private boolean getDelay(){
        boolean d;
        if (editTextDelay.getText().toString().equals("")) {
            d = false;
            myToast ("Введите задержку\nот 0 до 60 секунд");
        }else {
            int i = Integer.parseInt(editTextDelay.getText().toString());
            if (i>=0 && i<=60) {
                d = true;
            }else {
                d = false;
                myToast ("Введите задержку\n от 0 до 60 секунд\"");
            }
        }
        return d;
    }
    private void myToast (String s){
        Toast mToast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }
}
