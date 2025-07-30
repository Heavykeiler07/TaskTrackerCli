package com.example.taskcli;

public enum TaskStatus {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static TaskStatus fromString(String s) {
        switch (s.toLowerCase()) {
            case "todo": return TODO;
            case "in-progress": return IN_PROGRESS;
            case "done": return DONE;
            default: throw new IllegalArgumentException("Ungültiger Status: " + s);
        }
    }
}
