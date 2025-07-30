package com.example.taskcli;

import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

public class TaskRepository {

    private final String filename;
    private JsonObject rootJson;

    public TaskRepository(String filename) {
        this.filename = filename;
        load();
    }

    private void load() {
        File file = new File(filename);
        if (!file.exists()) {
            rootJson = Json.createObjectBuilder()
                    .add("tasks", Json.createArrayBuilder().build())
                    .build();
            save();
        } else {
            try (InputStream is = Files.newInputStream(Paths.get(filename));
                 JsonReader reader = Json.createReader(is)) {
                rootJson = reader.readObject();
                if (!rootJson.containsKey("tasks")) {
                    rootJson = Json.createObjectBuilder()
                            .add("tasks", Json.createArrayBuilder().build())
                            .build();
                }
            } catch (IOException e) {
                throw new RuntimeException("Fehler beim Laden der Datei", e);
            }
        }
    }

    public List<JsonObject> getAllTasks() {
        JsonArray tasksArray = rootJson.getJsonArray("tasks");
        List<JsonObject> list = new ArrayList<>();
        for (JsonValue val : tasksArray) {
            list.add(val.asJsonObject());
        }
        return list;
    }

    public Optional<JsonObject> getTaskById(int id) {
        return getAllTasks().stream()
                .filter(t -> t.getInt("id") == id)
                .findFirst();
    }

    public void addTask(String description) {
        List<JsonObject> tasks = getAllTasks();
        int newId = tasks.stream()
                .mapToInt(t -> t.getInt("id"))
                .max()
                .orElse(0) + 1;

        String now = Instant.now().toString();

        JsonObject newTask = Json.createObjectBuilder()
                .add("id", newId)
                .add("description", description)
                .add("status", TaskStatus.TODO.toString())
                .add("createdAt", now)
                .add("updatedAt", now)
                .build();

        tasks.add(newTask);
        saveJson(tasks);
        System.out.println("Task added successfully (ID: " + newId + ")");
    }

    public boolean updateTask(int id, String newDescription) {
        List<JsonObject> tasks = getAllTasks();
        boolean found = false;
        List<JsonObject> updatedTasks = new ArrayList<>();
        String now = Instant.now().toString();

        for (JsonObject t : tasks) {
            if (t.getInt("id") == id) {
                JsonObject updated = Json.createObjectBuilder(t)
                        .add("description", newDescription)
                        .add("updatedAt", now)
                        .build();
                updatedTasks.add(updated);
                found = true;
            } else {
                updatedTasks.add(t);
            }
        }
        if (found) saveJson(updatedTasks);
        return found;
    }

    public boolean deleteTask(int id) {
        List<JsonObject> tasks = getAllTasks();
        boolean found = false;
        List<JsonObject> updatedTasks = new ArrayList<>();

        for (JsonObject t : tasks) {
            if (t.getInt("id") == id) {
                found = true;
            } else {
                updatedTasks.add(t);
            }
        }
        if (found) saveJson(updatedTasks);
        return found;
    }

    public boolean setTaskStatus(int id, TaskStatus newStatus) {
        List<JsonObject> tasks = getAllTasks();
        boolean found = false;
        List<JsonObject> updatedTasks = new ArrayList<>();
        String now = Instant.now().toString();

        for (JsonObject t : tasks) {
            if (t.getInt("id") == id) {
                JsonObject updated = Json.createObjectBuilder(t)
                        .add("status", newStatus.toString())
                        .add("updatedAt", now)
                        .build();
                updatedTasks.add(updated);
                found = true;
            } else {
                updatedTasks.add(t);
            }
        }
        if (found) saveJson(updatedTasks);
        return found;
    }

    public List<JsonObject> getTasksByStatus(TaskStatus status) {
        if (status == null) return getAllTasks();
        List<JsonObject> filtered = new ArrayList<>();
        for (JsonObject t : getAllTasks()) {
            if (t.getString("status", "").equalsIgnoreCase(status.toString())) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    private void saveJson(List<JsonObject> tasks) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        tasks.forEach(arrayBuilder::add);

        rootJson = Json.createObjectBuilder()
                .add("tasks", arrayBuilder.build())
                .build();

        save();
    }

    private void save() {
        try (OutputStream os = Files.newOutputStream(Paths.get(filename));
             JsonWriter writer = Json.createWriter(os)) {
            writer.writeObject(rootJson);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Speichern der Datei", e);
        }
    }
}
