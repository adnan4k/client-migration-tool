package com.example.migration.controller;

import java.util.List;

public class BulkMigrationRequest {
    private List<Long> ids;

    public BulkMigrationRequest() {
    }

    public BulkMigrationRequest(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}

