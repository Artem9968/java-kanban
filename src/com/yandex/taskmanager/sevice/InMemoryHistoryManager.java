package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

 class InMemoryHistoryManager implements HistoryManager {
     private final static int historySize = 10;   // сделал константой
     private List<Task> tasks;              // сделал приватным

     public InMemoryHistoryManager() {
         tasks = new ArrayList<>(historySize);
     }

     @Override
     public void add(Task task) {
         if (tasks.size() == historySize) {
             tasks.remove(0);
         }
         tasks.add(task);
     }

     @Override
     public List<Task> getHistory() {
         return List.copyOf(tasks);    // передаю копию
     }
 }
