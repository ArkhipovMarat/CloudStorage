package ru.netology.cloud_storage.exceptions;

public enum Errors {
    // storage errors
    COULD_NOT_INITIALIZE_STORAGE_LOCATION("Could not initialize storage location "),
    COULD_NOT_LOAD_FILE("Could not load file "),
    FAILED_TO_READ_STORED_FILES("Failed to read stored files "),
    FAILED_TO_RENAME_FILE("Failed to rename file "),
    FAILED_TO_DELETE_FILE("Failed to delete file "),
    FAILED_TO_STORE_FILE("Failed to store file "),
    FILE_NOT_READABLE (" file is not readable or exists"),

    // security errors
    BAD_CREDENTIALS("Wrong login and password "),
    UNAUTHORIZED_REQUEST("Unauthorized request "),

    // data init error
    COULD_NOT_CREATE_USER_DIRECTORIES("Could not create user directories ");

    private String description;

    Errors(String description) {
        this.description = description;
    }

    public String value() {
        return description;
    }
}
