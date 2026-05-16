package com.example.databaseMeneger;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class TestDB {

    private static final String TAG = "DB_TEST";
    private DataBaseController dbController;

    public TestDB(Context context) {
        dbController = new DataBaseController(context);
    }

    public void runTests() {

        Log.d(TAG, "--- ROZPOCZĘCIE TESTÓW ---");

        Log.d(TAG, "KROK 1: Dodawanie rekordów...");
        dbController.insertTask("Zadanie testowe A");
        dbController.insertTask("Zadanie testowe B");
        dbController.insertTask("Zadanie testowe C");
        displayCurrentState();

        Log.d(TAG, "KROK 2: Modyfikacja pierwszego dostępnego rekordu...");
        List<Task> tasksForUpdate = dbController.getTaskList();

        if (!tasksForUpdate.isEmpty()) {

            // Bezpośredni dostęp do publicznego pola 'id' zamiast metody getId()
            int idToUpdate = tasksForUpdate.get(0).id;
            dbController.updateAtID(idToUpdate, "Zadanie testowe A - ZAKTUALIZOWANE", 1);
        }

        displayCurrentState();

        Log.d(TAG, "KROK 3: Usuwanie drugiego dostępnego rekordu...");
        List<Task> tasksForDeletion = dbController.getTaskList();

        if (tasksForDeletion.size() > 1) {

            // Bezpośredni dostęp do publicznego pola 'id'
            int idToDelete = tasksForDeletion.get(1).id;
            dbController.deleteAtID(idToDelete);
        }

        displayCurrentState();

        Log.d(TAG, "--- ZAKOŃCZENIE TESTÓW ---");
    }

    private void displayCurrentState() {

        List<Task> tasks = dbController.getTaskList();
        Log.d(TAG, "STAN BAZY DANYCH (Ilość: " + tasks.size() + "):");

        for (Task task : tasks) {

            // Odwołanie do publicznych pól klasy Task
            Log.d(TAG, "ID: " + task.id + " | Nazwa: " + task.taskName + " | Status: " + task.isDone);
        }

        Log.d(TAG, "--------------------------------------------------");
    }
}