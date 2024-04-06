package ru.darek;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Stream<Task> tasks = getTasksStream();

        logger.info("-Получение списка по статусу (запланирован, не запланирован)---------------");
        List<Task> resultTasks = tasks.filter(task -> (task.getStatus() == Status.PLANNED || task.getStatus() == Status.UNPLANNED))
                                      .toList();
        resultTasks.stream()
                   .map(task -> task.getName())
                   .peek(it -> logger.info(it))
                   .toList();

        logger.info("-Проверка по id (найден)----------------------------------");
        tasks = getTasksStream();
        Task taskTrue = tasks.filter(tsk -> tsk.getId() == 3)
                         .peek(it -> logger.info(it))
                         .findFirst()
                         .get();

        logger.info("-Проверка по id (не найден)--------------------------------");
        tasks = getTasksStream();
        logger.info("найдено соответствий: " + tasks.filter(tsk -> tsk.getId() == 9).count());
        tasks = getTasksStream();
        logger.info("Задача с номером 9 найдена? " + (tasks.findFirst().orElseThrow().getId() == 9));

        logger.info("-Количество задач со статусом В РАБОТЕ---------------------");
        tasks = getTasksStream();
        logger.info("Количество задач в статусе 'в работе': " + tasks.filter(tsk->tsk.getStatus() == Status.ATWORK).count());
        logger.info("-Задачи отсортированные не запланированные, запланированные, в работе, выполенные, закрытые, истекшие------");
        tasks = getTasksStream();
        Comparator<Task> comparator = Comparator.comparing(Task::getStatus);
        tasks.sorted(comparator)
             .peek(tsk->logger.info(tsk.getId() + " " + tsk.getStatus()))
             .collect(Collectors.toList());
        // Для иного порядка - можно поменять порядок перечисления ENUM или..
        // написать свой класс:   public class MyComparator implements Comparator<Task>
        logger.info("-Сортировка по статусу в алфавитном порядке ---------------------");
        tasks = getTasksStream();
        tasks.sorted((task, otherTask) -> {
            String str1 = task.getStatus().name();
            String str2 = otherTask.getStatus().name();
            if (str1 == null || str2 == null || str1.compareTo(str2) == 0) return 0;
            return  str1.compareTo(str2);
        }).peek(tsk->logger.info(tsk.getId() + " " + tsk.getStatus()))
          .collect(Collectors.toList());
        logger.info("-Сортировка по статусу в алфавитном порядке через forEach ---------------------");
        tasks = getTasksStream();
        tasks.sorted((task, otherTask) -> {
                    String str1 = task.getStatus().name();
                    String str2 = otherTask.getStatus().name();
                    if (str1 == null || str2 == null || str1.compareTo(str2) == 0) return 0;
                    return  str1.compareTo(str2);
                }).forEach(task -> logger.info(task.getId() + " " + task.getStatus()));

    }

    public static Stream<Task> getTasksStream() {
        return Stream.of(
                Task.builder()
                        .id(1)
                        .name("сон")
                        .status(Status.CLOSED)
                        .build(),
                Task.builder()
                        .id(2)
                        .name("душ")
                        .status(Status.COMPLETED)
                        .build(),
                Task.builder()
                        .id(3)
                        .name("завтрак")
                        .status(Status.ATWORK)
                        .build(),
                Task.builder()
                        .id(4)
                        .name("Чтение")
                        .status(Status.ATWORK)
                        .build(),
                Task.builder()
                        .id(5)
                        .name("гимнастика")
                        .status(Status.EXPIRED)
                        .build(),
                Task.builder()
                        .id(6)
                        .name("уборка")
                        .status(Status.PLANNED)
                        .build(),
                Task.builder()
                        .id(7)
                        .name("готовка")
                        .status(Status.PLANNED)
                        .build(),
                Task.builder()
                        .id(8)
                        .name("домашка")
                        .status(Status.UNPLANNED)
                        .build()
        );
    }
}

