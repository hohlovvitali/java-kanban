package manager.taskmanager;
import manager.historymanager.HistoryManager;
import manager.managerexception.ManagerSaveException;
import manager.managerexception.ManagerValidateException;
import tasks.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.nio.charset.StandardCharsets;


public class FileBackedTasksManager extends InMemoryTaskManager{
    private final File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))){
            br.readLine();

            while (br.ready()){
                String line = br.readLine();
                if (line.isBlank()){
                    line = br.readLine();
                    if (line!= null && !line.isBlank()){
                        List<Integer> taskHistoryID = FileBackedTasksManager.historyFromString(line);

                        for (Integer taskID: taskHistoryID){
                            tasksManager.taskMemory.add(tasksManager.getTask(taskID));
                        }
                    }
                } else {
                    Task task = tasksManager.fromString(line);
                    if (task.getTaskType() == TaskType.TASK){
                        tasksManager.addTaskWithID(task);
                    } else if (task.getTaskType() == TaskType.SUBTASK) {
                        tasksManager.addSubtaskWithID((Subtask) task);
                    } else {
                        tasksManager.addEpicWithID((Epic) task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }

        return tasksManager;
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))){
            bw.write("id,type,name,status,description,epic\n");

            for (Integer taskID: tasksIDList){
                Task task = this.getTask(taskID);
                bw.write(this.taskObjectToString(task) + "\n");
            }

            bw.write("\n");
            bw.write(FileBackedTasksManager.historyToString(this.taskMemory));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException, ManagerValidateException {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws ManagerSaveException, ManagerValidateException {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException, ManagerValidateException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException, ManagerValidateException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int taskID) throws ManagerSaveException {
        super.deleteTaskById(taskID);
        save();
    }

    @Override
    public void deleteSubtaskById(int taskID) throws ManagerSaveException {
        super.deleteSubtaskById(taskID);
        save();
    }

    @Override
    public void deleteEpicById(int taskID) throws ManagerSaveException {
        super.deleteEpicById(taskID);
        save();
    }

    @Override
    public Epic getEpicObjectByID(int epicID) throws ManagerSaveException {
        Epic epic = super.getEpicObjectByID(epicID);
        save();
        return epic;
    }

    @Override
    public Task getTaskObjectByID(int taskID) throws ManagerSaveException {
        Task task = super.getTaskObjectByID(taskID);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskObjectByID(int subtaskID) throws ManagerSaveException {
        Subtask subtask = super.getSubtaskObjectByID(subtaskID);
        save();
        return subtask;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        FileBackedTasksManager tasksManager = (FileBackedTasksManager) o;
        return this.file.equals(tasksManager.file) && this.epicHashMap.equals(tasksManager.epicHashMap) &&
                this.taskHashMap.equals(tasksManager.taskHashMap) &&
                this.subtaskHashMap.equals(tasksManager.subtaskHashMap) &&
                FileBackedTasksManager.historyToString(this.taskMemory).equals(
                        FileBackedTasksManager.historyToString(tasksManager.taskMemory));
    }

    @Override
    public String toString(){
        String outString = "Path: " + this.file.getPath() + "\n";
        for (Integer taskID: tasksIDList){
            Task task = this.getTask(taskID);
            outString = outString + this.taskObjectToString(task) + "\n";
        }

        outString = outString + "\n";
        outString= outString + "History: " + FileBackedTasksManager.historyToString(this.taskMemory);

        return outString;
    }

    private void addTaskWithID(Task task){
        taskHashMap.put(task.getTaskID(), task);
        tasksIDList.add(task.getTaskID());
    }

    private void addEpicWithID(Epic epic) {
        epicHashMap.put(epic.getTaskID(), epic);
        tasksIDList.add(epic.getTaskID());
    }

    private void addSubtaskWithID(Subtask subtask) {
        subtaskHashMap.put(subtask.getTaskID(), subtask);
        epicHashMap.get(subtask.getEpicID()).setSubtaskIDList(subtask.getTaskID());
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
        tasksIDList.add(subtask.getTaskID());
    }

    private Task fromString(String value){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

        String[] lineValues = value.split(",");
        if(lineValues.length >= 7){
            if (lineValues[1].equals(TaskType.TASK.toString())){
                return new Task(Integer.parseInt(lineValues[0]), lineValues[2], TaskStatus.valueOf(lineValues[3]),
                        lineValues[4], LocalDateTime.parse(lineValues[5], formatter).toInstant(ZoneOffset.UTC),
                        LocalDateTime.parse(lineValues[6], formatter).toInstant(ZoneOffset.UTC));
            } else if (lineValues[1].equals(TaskType.SUBTASK.toString())) {
                return new Subtask(Integer.parseInt(lineValues[0]), lineValues[2], TaskStatus.valueOf(lineValues[3]),
                        lineValues[4], LocalDateTime.parse(lineValues[5], formatter).toInstant(ZoneOffset.UTC),
                        LocalDateTime.parse(lineValues[6], formatter).toInstant(ZoneOffset.UTC),
                        Integer.parseInt(lineValues[7]));
            } else {
                return new Epic(Integer.parseInt(lineValues[0]), lineValues[2], TaskStatus.valueOf(lineValues[3]),
                        lineValues[4], LocalDateTime.parse(lineValues[5], formatter).toInstant(ZoneOffset.UTC),
                        LocalDateTime.parse(lineValues[6], formatter).toInstant(ZoneOffset.UTC));
            }
        }
        else {
            if (lineValues[1].equals(TaskType.TASK.toString())){
                return new Task(Integer.parseInt(lineValues[0]), lineValues[2], TaskStatus.valueOf(lineValues[3]),
                        lineValues[4]);
            } else if (lineValues[1].equals(TaskType.SUBTASK.toString())) {
                return new Subtask(Integer.parseInt(lineValues[0]), lineValues[2], TaskStatus.valueOf(lineValues[3]),
                        lineValues[4], Integer.parseInt(lineValues[5]));
            } else {
                return new Epic(Integer.parseInt(lineValues[0]), lineValues[2], TaskStatus.valueOf(lineValues[3]),
                        lineValues[4]);
            }
        }
    }

    private static String historyToString(HistoryManager manager) {
        ArrayList<String> tasksIdHistory= new ArrayList<>();
        for (Task task: manager.getTasks()){
            tasksIdHistory.add(Integer.toString(task.getTaskID()));
        }

        return String.join(",", tasksIdHistory);
    }

    private static List<Integer> historyFromString(String value){
        String[] historyArray = value.split(",");
        List<Integer> historyList= new ArrayList<>();
        for (String id: historyArray){
            historyList.add(Integer.parseInt(id));
        }

        return historyList;
    }


    private String taskObjectToString(Task task){
        if (task.getTaskType() == TaskType.TASK){
            return task.toString();
        } else if (task.getTaskType() == TaskType.EPIC) {
            Epic epic = (Epic) task;
            return epic.toString();
        } else {
            Subtask subtask = (Subtask) task;
            return subtask.toString();
        }
    }
}
