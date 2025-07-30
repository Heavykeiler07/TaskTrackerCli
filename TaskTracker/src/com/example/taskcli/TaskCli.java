package com.example.taskcli;

import com.example.taskcli.commands.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TaskCli {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        TaskRepository repository = new TaskRepository("tasks.json");

        Map<String, TaskCommand> commands = new HashMap<>();
        commands.put("add", new AddCommand(repository));
        commands.put("update", new UpdateCommand(repository));
        commands.put("delete", new DeleteCommand(repository));
        commands.put("mark-in-progress", new MarkInProgressCommand(repository));
        commands.put("mark-done", new MarkDoneCommand(repository));
        commands.put("list", new ListCommand(repository));

        String cmdName = args[0].toLowerCase();

        TaskCommand command = commands.get(cmdName);
        if (command == null) {
            System.out.println("Unknown command: " + cmdName);
            printUsage();
            System.exit(1);
        }

        String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
        command.execute(cmdArgs);
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  add <description>");
        System.out.println("  update <id> <new description>");
        System.out.println("  delete <id>");
        System.out.println("  mark-in-progress <id>");
        System.out.println("  mark-done <id>");
        System.out.println("  list [status]");
        System.out.println("\nStatus values for list: todo, in-progress, done");
    }
}
