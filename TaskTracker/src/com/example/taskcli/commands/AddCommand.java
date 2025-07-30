package com.example.taskcli.commands;

import com.example.taskcli.TaskRepository;


public class AddCommand implements TaskCommand {

    private final TaskRepository repository;

    public AddCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: add <description>");
            return;
        }
        String description = String.join(" ", args);
        repository.addTask(description);
    }
}
