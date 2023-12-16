package manager.historymanager;

import tasktype.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}
