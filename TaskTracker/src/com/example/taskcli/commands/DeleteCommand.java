package com.example.taskcli.commands;

import com.example.taskcli.TaskRepository;

public class DeleteCommand implements TaskCommand {

    private final TaskRepository repository;

    public DeleteCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: delete <id>");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("ID must be a number.");
            return;
        }
        boolean success = repository.deleteTask(id);
        System.out.println(success ? "Task " + id + " deleted successfully." : "Task with ID " + id + " not found.");
    }
}
