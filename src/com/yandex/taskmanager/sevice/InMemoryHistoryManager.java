package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryHistoryManager implements HistoryManager {
    private final static int HISTORY_SIZE = 10;
    private final List<Task> tasks;
    public Map<Integer, Node<Task>> historyTasks = new HashMap<>();


    public InMemoryHistoryManager() {
        tasks = new ArrayList<>(HISTORY_SIZE);
    }

    @Override
    public void add(Task task) {
//        if (historyTasks.containsKey(task.getId())) {
//            remove(task.getId());
//        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(historyTasks.get(id));
    }


    private static class Node<T extends Task> {
        public  T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

    }

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    public void linkLast(Task task) {

        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail != null)
            oldTail.next = newNode;
        else
            head = newNode;
        size++;
        historyTasks.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Map.Entry<Integer, Node<Task>> listTask : historyTasks.entrySet()) {
            tasks.add(listTask.getValue().data);
        }
        return tasks;
    }

    private void removeNode(Node task) {
        if (size == 0) {
            return;
        }
        if (size == 1) {
            head = null;
            tail = null;
            task.data = null;
            size = 0;
            return;
        } else if (task == tail) {
            task.prev.next = null;
            task.prev = null;
            task.data = null;
        } else if (task == head) {
            task.next.prev = null;
            task.next = null;
            task.data = null;
        } else {
            task.prev = task.next.next;
            task.next = task.prev.prev;
            task.data = null;
        }
        size--;
    }


}