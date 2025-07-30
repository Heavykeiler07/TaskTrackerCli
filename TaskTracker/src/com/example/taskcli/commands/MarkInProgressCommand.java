package com.example.taskcli.commands;

import com.example.taskcli.TaskRepository;
import com.example.taskcli.TaskStatus;

public class MarkInProgressCommand implements TaskCommand {

    private final TaskRepository repository;

    public MarkInProgressCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: mark-in-progress <id>");
            return;
        }
        try {
            int id = Integer.parseInt(args[0]);
            boolean success = repository.setTaskStatus(id, TaskStatus.IN_PROGRESS);
            System.out.println(success ? "Task " + id + " marked as in-progress." : "Task with ID " + id + " not found.");
        } catch (NumberFormatException e) {
            System.out.println("ID must be a number.");
        }
    }
}
