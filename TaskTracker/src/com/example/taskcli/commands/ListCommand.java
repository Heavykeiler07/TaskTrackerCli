package com.example.taskcli.commands;

import com.example.taskcli.TaskRepository;
import com.example.taskcli.TaskStatus;

import javax.json.JsonObject;
import java.util.List;

public class ListCommand implements TaskCommand {

    private final TaskRepository repository;

    public ListCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        TaskStatus filterStatus = null;
        if (args.length == 1) {
            try {
                filterStatus = TaskStatus.fromString(args[0]);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status filter. Supported: todo, in-progress, done");
                return;
            }
        } else if (args.length > 1) {
            System.out.println("Usage: list [status]");
            return;
        }

        List<JsonObject> tasks = repository.getTasksByStatus(filterStatus);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        for (JsonObject task : tasks) {
            System.out.printf(
                    "ID: %d\nDescription: %s\nStatus: %s\nCreatedAt: %s\nUpdatedAt: %s\n---\n",
                    task.getInt("id"),
                    task.getString("description"),
                    task.getString("status"),
                    task.getString("createdAt"),
                    task.getString("updatedAt")
            );
        }
    }
}
