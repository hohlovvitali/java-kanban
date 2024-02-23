package manager.taskmanager;
import manager.Managers;
import manager.historymanager.HistoryManager;
import manager.managerexception.ManagerSaveException;
import tasktype.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;


public class FileBackedTasksManager extends InMemoryTaskManager{
    private final File file;

    public static void main(String[] args) throws ManagerSaveException {
        File file = new File("resources\\taskManager.txt");
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
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

        Scanner scanner = new Scanner(System.in);
        while (true){
            printMenu();

            System.out.println("Выберите команду");
            int command = scanner.nextInt();

            switch (command){
                case 1:
                    // Ввод эпиков
                    manager.addEpic(epic1);
                    manager.addEpic(epic2);
                    System.out.println(manager.getAllEpics());
                    System.out.println("----------------------------------------------------------------");
                    break;
                case 2:
                    // Ввод task.Task
                    manager.addTask(task1);
                    manager.addTask(task2);
                    System.out.println(manager.getAllTasks());
                    System.out.println("----------------------------------------------------------------");
                    break;
                case 3:
                    //Ввод subtask
                    subtask1.setEpicID(epic1.getTaskID());
                    subtask2.setEpicID(epic1.getTaskID());
                    subtask3.setEpicID(epic1.getTaskID());
                    subtask4.setEpicID(epic2.getTaskID());
                    manager.addSubtask(subtask1);
                    manager.addSubtask(subtask2);
                    manager.addSubtask(subtask3);
                    manager.addSubtask(subtask4);
                    System.out.println(manager.getAllSubtasks());
                    System.out.println("----------------------------------------------------------------");
                    break;
                case 4:
                    System.out.println(manager.getEpicObjectByID(epic1.getTaskID()).toString(manager));
                    System.out.println(manager.getHistory());
                    System.out.println("----------------------------------------------------------------");
                    showHistoryID((ArrayList<Task>) manager.getHistory());
                    break;
                case 5:
                    System.out.println(manager.getEpicObjectByID(epic2.getTaskID()).toString(manager));
                    System.out.println(manager.getHistory());
                    System.out.println("----------------------------------------------------------------");
                    showHistoryID((ArrayList<Task>) manager.getHistory());
                    subtask4.setStatus(TaskStatus.DONE);
                    subtask1.setStatus(TaskStatus.IN_PROGRESS);
                    manager.updateSubtask(subtask1);
                    manager.updateSubtask(subtask4);
                    break;
                case 6:
                    System.out.println(manager.getHistory());
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Размер истории: " + manager.getHistory().size());
                    System.out.println("----------------------------------------------------------------");
                    showHistoryID((ArrayList<Task>) manager.getHistory());
                    System.out.println(manager.getEpicObjectByID(epic1.getTaskID()));
                    System.out.println(manager.getEpicObjectByID(epic2.getTaskID()));
                    break;
                case 7:
                    System.out.println(manager.getTaskObjectByID(task1.getTaskID()).toString());
                    System.out.println(manager.getTaskObjectByID(task2.getTaskID()).toString());
                    System.out.println(manager.getSubtaskObjectByID(subtask3.getTaskID()).toString());
                    System.out.println(manager.getSubtaskObjectByID(subtask2.getTaskID()).toString());
                    System.out.println(manager.getSubtaskObjectByID(subtask1.getTaskID()).toString());
                    System.out.println(manager.getHistory());
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Размер истории: " + manager.getHistory().size());
                    System.out.println("----------------------------------------------------------------");
                    showHistoryID((ArrayList<Task>) manager.getHistory());
                    break;
                case 8:
                    manager.deleteTaskById(task1.getTaskID());
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(manager.getHistory());
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Размер истории: " + manager.getHistory().size());
                    System.out.println("----------------------------------------------------------------");
                    showHistoryID((ArrayList<Task>) manager.getHistory());
                    break;
                case 9:
                    manager.deleteEpicById(epic1.getTaskID());
                    System.out.println(manager.getHistory());
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Размер истории: " + manager.getHistory().size());
                    System.out.println("----------------------------------------------------------------");
                    showHistoryID((ArrayList<Task>) manager.getHistory());
                    break;
                case 10:
                    FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(file);
                    System.out.println(newManager.getHistory());
                    System.out.println(newManager.getAllEpics());
                    break;
                case 0:
                    scanner.close();
                    return;
                default:
                    System.out.println("Такой команды пока нет\n");
            }
        }
    }

    public static void showHistoryID(ArrayList<Task> taskHistory){
        ArrayList<Integer> taskHistoryID = new ArrayList<>();
        for (Task task: taskHistory){
            taskHistoryID.add(task.getTaskID());
        }

        System.out.println("История ID задач:");
        System.out.println(taskHistoryID);
    }

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
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

    public void addTaskWithID(Task task){
        taskHashMap.put(task.getTaskID(), task);
        tasksIDList.add(task.getTaskID());
    }

    public void addEpicWithID(Epic epic) {
        epicHashMap.put(epic.getTaskID(), epic);
        tasksIDList.add(epic.getTaskID());
    }

    public void addSubtaskWithID(Subtask subtask) {
        subtaskHashMap.put(subtask.getTaskID(), subtask);
        epicHashMap.get(subtask.getEpicID()).setSubtaskIDList(subtask.getTaskID());
        epicHashMap.get(subtask.getEpicID()).checkEpicStatus(subtaskHashMap);
        tasksIDList.add(subtask.getTaskID());
    }

    public Task fromString(String value){
        String[] lineValues = value.split(",");
        if (lineValues[1].equals(TaskType.TASK.toString())){
            return new Task(Integer.parseInt(lineValues[0]), TaskType.TASK, lineValues[2],
                            TaskStatus.valueOf(lineValues[3]), lineValues[4]);
        } else if (lineValues[1].equals(TaskType.SUBTASK.toString())) {
            return new Subtask(Integer.parseInt(lineValues[0]), TaskType.SUBTASK, lineValues[2],
                            TaskStatus.valueOf(lineValues[3]), lineValues[4], Integer.parseInt(lineValues[5]));
        } else {
            return new Epic(Integer.parseInt(lineValues[0]), TaskType.EPIC, lineValues[2],
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

    public String toString(Task task){
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

    private static void printMenu() {
        System.out.println("Команды меню:");
        System.out.println("1 - Ввод task.Epic");
        System.out.println("2 - Ввод task.Task");
        System.out.println("3 - Ввод task.Subtask");
        System.out.println("4 - Проверка на вывод по идентификатору и вывод истории после epic1 с сохранением в файл");
        System.out.println("5 - Проверка на изменение статуса для Sabtask1 и Sabtask4 с изменением статуса Epic и сохранением в файл");
        System.out.println("6 - История просмотра задач");
        System.out.println("7 - Проверка на вывод по идентификатору и вывод истории после всех Task и Subtask");
        System.out.println("8 - Проверка на вывод истории после удаления task1 и изменение файла");
        System.out.println("9 - Проверка на вывод истории после удаления epic1 и изменение файла");
        System.out.println("10 - Восстановление истории");
        System.out.println("0 - Выход");
    }
}
