package manager.taskmanager;
import manager.historymanager.HistoryManager;
import manager.managerexception.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class FileBackedTasksManager extends InMemoryTaskManager{
    private final File file;

    public static void main(String[] args) throws ManagerSaveException {
        File file = new File("resources\\taskManager.txt");
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        // Заполнeние задачами manager
        fillManager(manager);

        // Заполнение истории просмотра задача
        fillHistoryManager(manager);

        // Вывод manager
        System.out.println("Начальный менеджер:\n" + manager.toString());


        // Создаем копию manager из файла
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(file);

        System.out.println("Восстановленный менеджер:\n" + newManager.toString());

        // Сравниваем менеджеры
        System.out.println("Сравнение менеджеров: " + manager.equals(newManager));
    }

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file){
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))){
            br.readLine();

            while (br.ready()){
                String line = br.readLine();
                if (line.isBlank()){
                    line = br.readLine();
                    if (!line.isBlank()){
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
            throw new RuntimeException(e);
        }

        return tasksManager;
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws ManagerSaveException {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
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
        String outString = "Path: " + this.file.getPath() + ",\n";
        for (Integer taskID: tasksIDList){
            Task task = this.getTask(taskID);
            outString = outString + this.toString(task) + "\n";
        }

        outString = outString + "\n";
        outString= outString + "История: " + FileBackedTasksManager.historyToString(this.taskMemory);

        return outString;
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

    public Task fromString(String value){
        String[] lineValues = value.split(",");
        if (lineValues[1].equals(TaskType.TASK.toString())){
            return new Task(Integer.parseInt(lineValues[0]), lineValues[2],
                            TaskStatus.valueOf(lineValues[3]), lineValues[4]);
        } else if (lineValues[1].equals(TaskType.SUBTASK.toString())) {
            return new Subtask(Integer.parseInt(lineValues[0]), lineValues[2],
                            TaskStatus.valueOf(lineValues[3]), lineValues[4], Integer.parseInt(lineValues[5]));
        } else {
            return new Epic(Integer.parseInt(lineValues[0]), lineValues[2],
                            TaskStatus.valueOf(lineValues[3]), lineValues[4]);
        }
    }

    public static String historyToString(HistoryManager manager) {
        ArrayList<String> tasksIdHistory= new ArrayList<>();
        for (Task task: manager.getTasks()){
            tasksIdHistory.add(Integer.toString(task.getTaskID()));
        }

        return String.join(",", tasksIdHistory);
    }

    public static List<Integer> historyFromString(String value){
        String[] historyArray = value.split(",");
        List<Integer> historyList= new ArrayList<>();
        for (String id: historyArray){
            historyList.add(Integer.parseInt(id));
        }

        return historyList;
    }


    private String toString(Task task){
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

    private void save() throws ManagerSaveException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))){
            bw.write("id,type,name,status,description,epic\n");

            for (Integer taskID: tasksIDList){
                Task task = this.getTask(taskID);
                bw.write(this.toString(task) + "\n");
            }

            bw.write("\n");
            bw.write(FileBackedTasksManager.historyToString(this.taskMemory));
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
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

    private static void fillManager(FileBackedTasksManager manager) throws ManagerSaveException {
        // Проверяем создание классов task.Task
        Task task1 = new Task("Подняться", "Первое задание");
        Task task2 = new Task("Проснуться", "Второе задание");

        // Проверяем создание классов task.Epic
        Epic epic1 = new Epic("Важный завтрак");
        Epic epic2 = new Epic("Важная тренировка");

        // Проверяем создание классов task.Subtask
        // Для epic1
        Subtask subtask1 = new Subtask("Яичница", "Первое", epic1.getTaskID());
        Subtask subtask2 = new Subtask("Бекон", "Второе", epic1.getTaskID());
        Subtask subtask3 = new Subtask("Кофе", "Третье", epic1.getTaskID());
        // Для epic2
        Subtask subtask4 = new Subtask("Отжимания", "4 подхода", epic2.getTaskID());

        // Ввод эпиков
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        // Ввод task.Task
        manager.addTask(task1);
        manager.addTask(task2);

        //Ввод subtask
        subtask1.setEpicID(epic1.getTaskID());
        subtask2.setEpicID(epic1.getTaskID());
        subtask3.setEpicID(epic1.getTaskID());
        subtask4.setEpicID(epic2.getTaskID());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);

        // Меняем статус subtask
        subtask4.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask4);
    }

    private static void fillHistoryManager(FileBackedTasksManager manager) throws ManagerSaveException {
        System.out.println(manager.getEpicObjectByID(1).toString(manager));
        System.out.println(manager.getEpicObjectByID(2).toString(manager));
        System.out.println(manager.getTaskObjectByID(3).toString());
        System.out.println(manager.getTaskObjectByID(4).toString());
        System.out.println(manager.getSubtaskObjectByID(7).toString());
        System.out.println(manager.getSubtaskObjectByID(6).toString());
        System.out.println(manager.getSubtaskObjectByID(5).toString());
        System.out.println(manager.getEpicObjectByID(1).toString(manager));
        System.out.println(manager.getEpicObjectByID(2).toString(manager));
    }
}
