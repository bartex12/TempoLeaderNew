package ru.barcats.tempo_leader_javanew.ui.tempoleader.dialogs.dialogsave;

public interface SaveStorage {
    long saveAsFile(String oldFileName, String newFileName, long fileOldIdCopy);

}
