package manager.historymanager;

import tasktype.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    // void remove(Task task);
    void remove(int id);
    List<Task> getTasks();
}
