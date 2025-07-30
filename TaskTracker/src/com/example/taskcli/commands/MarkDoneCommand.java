package com.example.taskcli.commands;

import com.example.taskcli.TaskRepository;
import com.example.taskcli.TaskStatus;

public class MarkDoneCommand implements TaskCommand {

    private final TaskRepository repository;

    public MarkDoneCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: mark-done <id>");
            return;
        }
        try {
            int id = Integer.parseInt(args[0]);
            boolean success = repository.setTaskStatus(id, TaskStatus.DONE);
            System.out.println(success ? "Task " + id + " marked as done." : "Task with ID " + id + " not found.");
        } catch (NumberFormatException e) {
            System.out.println("ID must be a number.");
        }
    }
}
