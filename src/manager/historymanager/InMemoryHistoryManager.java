package manager.historymanager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node<Task>> taskHistory = new HashMap<>();

    private Node<Task> head;

    private Node<Task> tail;

    @Override
    public void add(Task task) {
        remove(task.getTaskID());
        Node<Task> newNode = linkLast(task);
        if (head == null) {
            head = newNode;
        }
        taskHistory.put(task.getTaskID(), newNode);
    }

    @Override
    public void remove(int id) {
        if (taskHistory.containsKey(id)) {
            Node<Task> currentNode = taskHistory.get(id);
            if (currentNode.getPrev() == null) {
                head = currentNode.getNext();
            }

            if (currentNode.getNext() == null) {
                tail = currentNode.getPrev();
            }
            Node.removeNode(currentNode);
            taskHistory.remove(id);
        }
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasksList = new ArrayList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasksList.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }

        return tasksList;
    }

    private Node<Task> linkLast(Task task) {
        Node<Task> newTail = new Node<>(task, this.tail);
        if (this.tail != null) {
            this.tail.setNext(newTail);
        }
        this.tail = newTail;
        return newTail;
    }

    public static class Node<E> {
        private E data;
        private Node<E> prev;
        private Node<E> next;

        public Node(E task, Node<E> prev) {
            this.data = task;
            this.prev = prev;
            this.next = null;
        }

        public E getData() {
            return data;
        }

        public Node<E> getPrev() {
            return prev;
        }

        public void setPrev(Node<E> prev) {
            this.prev = prev;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }

        public static void removeNode(Node<Task> task) {
            if (task.getPrev() != null) {
                task.getPrev().setNext(task.getNext());
            } else if (task.getNext() != null) {
                task.getNext().setPrev(null);
            }

            if (task.getNext() != null) {
                task.getNext().setPrev(task.getPrev());
            } else if (task.getPrev() != null) {
                task.getPrev().setNext(null);
            }
        }
    }
}
