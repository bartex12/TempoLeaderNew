package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.database.sqlite.SQLiteDatabase;

import ru.barcats.tempo_leader_javanew.database.TempDBHelper;

public interface DelayStorage {
  int  getDelayNew(String fileName);
  int updateDelayNew(int timeOfDelay, String fileName);

}
