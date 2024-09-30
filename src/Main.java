public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        // Ниже оставил необходимый минимум для проверки функциональности кода, который нужен по условию

        Task run = new Task("Протренироваться", "Выйти на пробежку", Status.IN_PROGRESS);
        Task swim = new Task("Поплавать", "Пойти в бассейн", Status.NEW);
        taskManager.addTask(run);
        taskManager.addTask(swim);
        System.out.println("------------------------------");
        System.out.println(taskManager.getTasks());
        System.out.println("------------------------------");
        Epic learnJava = new Epic("Освоить Java", "Разобраться в JavaCore");
        taskManager.addEpic(learnJava);
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");
        SubTask readTheory = new SubTask("Прочитать теорию", "Написать конспект", Status.DONE);
        SubTask practicum = new SubTask("Практика", "Написать код", Status.IN_PROGRESS);
        taskManager.addSubTask(3, readTheory);
        taskManager.addSubTask(3, practicum);
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");
        System.out.println(taskManager.getSubsByEpicId(3));
        System.out.println("------------------------------");
        Epic checkCode = new Epic("Проверить код", "Проверить все методы классов");
        taskManager.addEpic(checkCode);
        System.out.println(taskManager.getEpicById(6));
        System.out.println("------------------------------");
        SubTask useDebug = new SubTask("Использовать дебаг", "Пройтись дебагом", Status.DONE);
        taskManager.addSubTask(6, useDebug);
        System.out.println(taskManager.getEpicById(6));
        System.out.println("------------------------------");
        System.out.println(taskManager.getSubsByEpicId(6));
        System.out.println("------------------------------");
        Task newRun = new Task("Протренироваться", "Выйти на пробежку", Status.DONE);
        taskManager.updateTask(1, newRun);
        System.out.println(taskManager.getTasks());
        System.out.println("------------------------------");
        SubTask newPracticum = new SubTask("Практика", "Написать код", Status.DONE);
        taskManager.updateSubTask(5, newPracticum);
        System.out.println(taskManager.getEpicById(3));
        System.out.println("------------------------------");
        System.out.println(taskManager.getSubsByEpicId(3));
        System.out.println("------------------------------");
        taskManager.delTaskById(1);
        taskManager.delEpicById(3);
        System.out.println(taskManager.getTasks());
        System.out.println("------------------------------");
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");
        taskManager.dellAllTasks();
        taskManager.dellAllSubs();
        System.out.println(taskManager.getTasks());
        System.out.println("------------------------------");
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");
        taskManager.dellAllEpics();
        System.out.println(taskManager.getEpics());
        System.out.println("------------------------------");
    }
}
