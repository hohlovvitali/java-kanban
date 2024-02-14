package manager.historymanager;

import tasktype.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node<Task>> taskHistory = new HashMap<>();

    private Node<Task> head;

    private Node<Task> tail;
    @Override
    public void add(Task task){
        remove(task.getTaskID());
        Node<Task> newNode = linklast(task);
        if (head == null){
            head = newNode;
        }
        taskHistory.put(task.getTaskID(), newNode);
    }

    private Node<Task> linklast(Task task){
        Node<Task> newTail = new Node<>(task, this.tail);
        if(this.tail != null){
            this.tail.setNext(newTail);
        }
        this.tail = newTail;
        return newTail;
    }
    @Override
    public void remove(int id){
        if (taskHistory.containsKey(id)){
            Node<Task> currentNode = taskHistory.get(id);
            if (currentNode.getPrev() == null){
                head = currentNode.getNext();
            }

            if (currentNode.getNext() == null){
                tail = currentNode.getPrev();
            }
            Node.removeNode(currentNode);
            taskHistory.remove(id);
        }
    }
    @Override
    public List<Task> getTasks() {
        List<Task> tasksList = new ArrayList<>();
        Node<Task> currentNode= head;
        while (currentNode != null){
            tasksList.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }

        return tasksList;
    }


    public Node<Task> getHead() {
        return head;
    }

    public Node<Task> getTail() {
        return tail;
    }
}
