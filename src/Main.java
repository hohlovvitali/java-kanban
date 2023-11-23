import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        // Проверяем создание классов Task
        ArrayList<String> taskStrings = new ArrayList<>();
        taskStrings.add("Подняться");
        taskStrings.add("Проснуться");
        Task task1 = new Task("Первое задание", taskStrings);
        manager.addTask(task1);
        ArrayList<String> taskStrings2 = new ArrayList<>();
        taskStrings2.add("Поесть");
        Task task2 = new Task("Второе задание", taskStrings2);
        manager.addTask(task2);

        // Проверяем создание классов Epic
        Epic epic1 = new Epic("Важный завтрак");
        Epic epic2 = new Epic("Важная тренировка");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        // Проверяем создание классов Subtask
        // Для epic1
        ArrayList<String> subtaskStrings = new ArrayList<>();
        subtaskStrings.add("Яичница");
        subtaskStrings.add("Бекон");
        Subtask subtask1 = new Subtask("Первое", subtaskStrings, epic1.getTaskID());
        manager.addSubtask(subtask1);
        ArrayList<String> subtaskStrings2 = new ArrayList<>();
        subtaskStrings2.add("Дессерт");
        subtaskStrings2.add("Кофе");
        Subtask subtask2 = new Subtask("Второе", subtaskStrings2, epic1.getTaskID());
        manager.addSubtask(subtask2);

        // Для epic2
        ArrayList<String> subtaskStrings3 = new ArrayList<>();
        subtaskStrings3.add("Жим груди");
        subtaskStrings3.add("Жим ногами");
        subtaskStrings3.add("Становая тяга");
        Subtask subtask3 = new Subtask("Силовая тренировка", subtaskStrings3, epic2.getTaskID());
        manager.addSubtask(subtask3);

        ArrayList<String> statusList = new ArrayList<>();
        statusList.add("NEW");
        statusList.add("IN_PROGRESS");
        statusList.add("DONE");

        Scanner scanner = new Scanner(System.in);
        while (true){
            printMenu();

            System.out.println("Выберите команду");
            int command = scanner.nextInt();

            switch (command){
                case 1:
                    // Вывод эпиков
                    System.out.println(manager.epicHashMapToString());
                    System.out.println("----------------------------------------------------------------");
                    break;
                case 2:
                    // Вывод Task
                    System.out.println(manager.taskHashMapToString());
                    System.out.println("----------------------------------------------------------------");
                    break;
                case 3:
                    //Вывод subtask
                    System.out.println(manager.subtaskHashMapToString());
                    System.out.println("----------------------------------------------------------------");
                    break;
                case 4:
                    System.out.println(manager.getEpicObjectByID(epic1.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(manager.getTaskObjectByID(task1.getTaskID()));
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(manager.getSubtaskObjectByID(subtask1.getTaskID()));
                    System.out.println("----------------------------------------------------------------");

                    // Проверка на ключи, которых нет

                    System.out.println(manager.getEpicObjectByID(14));
                    System.out.println(manager.getEpicObjectByID(15));
                    System.out.println(manager.getEpicObjectByID(16));
                    break;
                case 5:
                    System.out.println(manager.getEpicObjectByID(epic1.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(manager.getEpicObjectByID(epic2.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    subtask1.setStatus(statusList.get(1));
                    subtask3.setStatus(statusList.get(2));
                    task1.setStatus(statusList.get(1));
                    manager.updateSubtask(subtask1);
                    manager.updateSubtask(subtask3);
                    manager.updateTask(task1);
                    System.out.println(manager.getEpicObjectByID(epic1.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(manager.getEpicObjectByID(epic2.getTaskID()).toString(manager.getSubtaskHashMap()));
                    break;
                case 6:
                    manager.deleteAllSubtasks();
                    System.out.println(manager.getEpicObjectByID(epic1.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(manager.getEpicObjectByID(epic2.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(manager.getAllSubtasks());
                    System.out.println(manager.subtaskHashMapToString());
                    break;
                case 7:
                    manager.deleteAllEpics();
                    manager.deleteAllTasks();
                    System.out.println(manager.getAllEpics());
                    System.out.println(manager.getAllTasks());
                    System.out.println(manager.epicHashMapToString());
                    System.out.println(manager.taskHashMapToString());
                    break;
                case 8:
                    System.out.println(manager.getEpicObjectByID(epic1.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(manager.getEpicObjectByID(epic2.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    manager.deleteSubtask(subtask1.getTaskID());
                    manager.deleteTask(task1.getTaskID());
                    manager.deleteSubtask(subtask3.getTaskID());
                    System.out.println(manager.getEpicObjectByID(epic1.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    System.out.println(manager.getEpicObjectByID(epic2.getTaskID()).toString(manager.getSubtaskHashMap()));
                    System.out.println("----------------------------------------------------------------");
                    break;
                case 9:
                    scanner.close();
                    return;
                default:
                    System.out.println("Такой команды пока нет\n");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Команды меню:");
        System.out.println("1 - Вывод Epic");
        System.out.println("2 - Вывод Task");
        System.out.println("3 - Вывод Subtask");
        System.out.println("4 - Проверка на вывод по идентификатору");
        System.out.println("5 - Обновление статусов");
        System.out.println("6 - Удаление всех Subtask");
        System.out.println("7 - Удаление всего");
        System.out.println("8 - Удаление по идентификатору");
        System.out.println("9 - Выход");
    }
}