package com.yandex.taskmanager.sevice;

import com.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, DoublyLinkedList.Node> tasks;
    private final DoublyLinkedList<Task> doublyLinkedList = new DoublyLinkedList<>();

    public InMemoryHistoryManager() {
        tasks = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());

        doublyLinkedList.linkLast(task);
        tasks.put(task.getId(), doublyLinkedList.tail);
    }

    @Override
    public List<Task> getHistory() {
        return doublyLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        DoublyLinkedList.Node node = tasks.remove(id);
        doublyLinkedList.removeNode(node);
    }

    private static class DoublyLinkedList<T extends Task> {

        private static class Node<T extends Task> {
            public T data;
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
        int size = 0;

        public void linkLast(T task) {

            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail != null)
                oldTail.next = newNode;
            else
                head = newNode;
            size++;
        }

        public List<Task> getTasks() {
            List tasks = new ArrayList<Task>();
            Node linkedTask = head;
            for (int i = 0; i < size; i++) {
                tasks.add(linkedTask.data);
                linkedTask = linkedTask.next;
            }
            return tasks;
        }

        private void removeNode(Node node) {
            if (node == null) {
                return;
            }
            if (node != null) {
                Node nodePrev = node.prev;
                Node nodeNext = node.next;
                if (nodePrev == null && nodeNext != null) {
                    head = nodeNext;
                } else if (nodeNext == null && nodePrev != null) {
                    tail = nodePrev;
                } else if (nodeNext != null && nodePrev != null) {
                    nodePrev.next = nodeNext;
                    nodeNext.prev = nodePrev;
                } else if (nodeNext == null && nodePrev == null) {
                    head = null;
                    tail = null;
                }
                size--;
            }
        }
    }
}