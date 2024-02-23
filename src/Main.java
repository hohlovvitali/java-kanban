import manager.Managers;
import manager.managerexception.ManagerSaveException;
import manager.taskmanager.InMemoryTaskManager;
import tasktype.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();
        // Проверяем создание классов task.Task
        Task task1 = new Task("Подняться", "Первое задание");
        manager.addTask(task1);
        Task task2 = new Task("Проснуться", "Второе задание");
        manager.addTask(task2);

        // Проверяем создание классов task.Epic
        Epic epic1 = new Epic("Важный завтрак");
        Epic epic2 = new Epic("Важная тренировка");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        // Проверяем создание классов task.Subtask
        // Для epic1
        Subtask subtask1 = new Subtask("Яичница", "Первое", epic1.getTaskID());
        Subtask subtask2 = new Subtask("Бекон", "Второе", epic1.getTaskID());
        Subtask subtask3 = new Subtask("Кофе", "Третье", epic1.getTaskID());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);


        Scanner scanner = new Scanner(System.in);
        while (true){
            printMenu();

            System.out.println("Выберите команду");
            int command = scanner.nextInt();

            switch (command){
                case 1:
                    // Вывод эпиков
                    System.out.println(manager.getAllEpics());
                    System.out.println("----------------------------------------------------------------");
                    break;
                case 2:
                    // Вывод task.Task
                    System.out.println(manager.getAllTasks());
                    System.out.println("----------------------------------------------------------------");
                    break;
                case 3:
                    //Вывод subtask
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
                    break;
                case 6:
                    System.out.println(manager.getHistory());
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Размер истории: " + manager.getHistory().size());
                    System.out.println("----------------------------------------------------------------");
                    showHistoryID((ArrayList<Task>) manager.getHistory());
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
                    manager.deleteEpicById(epic2.getTaskID());
                    System.out.println(manager.getHistory());
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Размер истории: " + manager.getHistory().size());
                    System.out.println("----------------------------------------------------------------");
                    showHistoryID((ArrayList<Task>) manager.getHistory());
                    break;
                case 10:
                    manager.deleteEpicById(epic1.getTaskID());
                    System.out.println(manager.getHistory());
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("Размер истории: " + manager.getHistory().size());
                    System.out.println("----------------------------------------------------------------");
                    showHistoryID((ArrayList<Task>) manager.getHistory());
                    break;
                case 0:
                    scanner.close();
                    return;
                default:
                    System.out.println("Такой команды пока нет\n");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Команды меню:");
        System.out.println("1 - Вывод task.Epic");
        System.out.println("2 - Вывод task.Task");
        System.out.println("3 - Вывод task.Subtask");
        System.out.println("4 - Проверка на вывод по идентификатору и вывод истории после epic1");
        System.out.println("5 - Проверка на вывод по идентификатору и вывод истории после epic2");
        System.out.println("6 - История просмотра задач");
        System.out.println("7 - Проверка на вывод по идентификатору и вывод истории после всех Task и Subtask");
        System.out.println("8 - Проверка на вывод истории после удаления task1");
        System.out.println("9 - Проверка на вывод истории после удаления epic2");
        System.out.println("10 - Проверка на вывод истории после удаления epic1 и его подзадач");
        System.out.println("0 - Выход");
    }

    public static void showHistoryID(ArrayList<Task> taskHistory){
        ArrayList<Integer> taskHistoryID = new ArrayList<>();
        for (Task task: taskHistory){
            taskHistoryID.add(task.getTaskID());
        }

        System.out.println("История ID задач:");
        System.out.println(taskHistoryID);
    }
}