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

     public static class DoublyLinkedList<T extends Task> {

         private static class Node<T extends Task> {
             public final T data;
             public Node<T> next;
             public Node<T> prev;

             public Node(Node<T> prev, T data, Node<T> next) {
                 this.data = data;
                 this.next = next;
                 this.prev = prev;
             }

         }

         private Node<T> head;
         private Node<T> tail;
        private int size = 0;

         public void linkLast(T task) {

             final Node<T> oldTail = tail;
             final Node<T> newNode =  new Node<>(oldTail, task, null);
             tail = newNode;
             if (oldTail == null)
                 oldTail.next = newNode;
             else
                 head = newNode;
             size++;
         }

     }
 }