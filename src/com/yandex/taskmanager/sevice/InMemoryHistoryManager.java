package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

 class InMemoryHistoryManager implements HistoryManager {
     private final static int HISTORY_SIZE = 10;
     private final List<Task> tasks;

     public InMemoryHistoryManager() {
         tasks = new ArrayList<>(HISTORY_SIZE);
     }

     @Override
     public void add(Task task) {
         if (task != null) {          // добавил условие
             if (tasks.size() == HISTORY_SIZE) {
                 tasks.removeFirst();       // исправил
             }
             tasks.add(task);
         }
     }

     @Override
     public List<Task> getHistory() {
         return List.copyOf(tasks);    // передаю копию
     }
 }