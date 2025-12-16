package com.example.migration.controller;

import com.example.migration.domain.model.Client;
import java.util.List;

public class BulkMigrationResponse {
    private int successCount;
    private int failedCount;
    private List<Client> migratedClients;
    private List<String> errors;

    public BulkMigrationResponse() {
    }

    public BulkMigrationResponse(int successCount, int failedCount, List<Client> migratedClients, List<String> errors) {
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.migratedClients = migratedClients;
        this.errors = errors;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public List<Client> getMigratedClients() {
        return migratedClients;
    }

    public void setMigratedClients(List<Client> migratedClients) {
        this.migratedClients = migratedClients;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

