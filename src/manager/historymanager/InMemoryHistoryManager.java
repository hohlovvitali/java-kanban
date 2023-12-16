package manager.historymanager;

import tasktype.Task;

import java.util.List;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> taskHistory = new LinkedList<>();
    @Override
    public void add(Task task) {
        if(taskHistory.size() == 10) {
            taskHistory.removeFirst();
        }
        taskHistory.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}
