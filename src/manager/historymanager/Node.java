package manager.historymanager;
import tasktype.Task;

public class Node<E> {
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

    public void setData(E task) {
        this.data = task;
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

    public static void removeNode(Node<Task> task){
        if (task.getPrev() != null) {
            task.getPrev().setNext(task.getNext());
        } else if (task.getNext() != null){
            task.getNext().setPrev(null);
        }

        if (task.getNext() != null){
            task.getNext().setPrev(task.getPrev());
        } else if (task.getPrev() != null){
            task.getPrev().setNext(null);
        }
    }
}
