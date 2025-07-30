package com.example.taskcli.commands;

import com.example.taskcli.TaskRepository;

import java.util.Arrays;

public class UpdateCommand implements TaskCommand {

    private final TaskRepository repository;

    public UpdateCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: update <id> <new description>");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("ID must be a number.");
            return;
        }
        String newDescription = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        boolean success = repository.updateTask(id, newDescription);
        System.out.println(success ? "Task " + id + " updated successfully." : "Task with ID " + id + " not found.");
    }
}
