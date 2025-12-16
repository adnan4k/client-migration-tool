package com.example.migration.application;

import com.example.migration.domain.model.Client;
import com.example.migration.domain.repository.LegacyClientRepository;
import com.example.migration.domain.repository.NewClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientMigrationService {

    private static final Logger log = LoggerFactory.getLogger(ClientMigrationService.class);

    private final LegacyClientRepository legacyClientRepository;
    private final NewClientRepository newClientRepository;

    public ClientMigrationService(LegacyClientRepository legacyClientRepository,
                                  NewClientRepository newClientRepository) {
        this.legacyClientRepository = legacyClientRepository;
        this.newClientRepository = newClientRepository;
    }

    public List<Client> getLegacyClients() {
        return legacyClientRepository.findAll();
    }

    public List<Client> getNewClients() {
        return newClientRepository.findAll();
    }

    public Client migrateClient(Long id) {
        Client client = legacyClientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client with id " + id + " not found"));

        if (client.isMigrated()) {
            throw new IllegalStateException("Client with id " + id + " is already migrated");
        }

        client.setMigrated(true);
        legacyClientRepository.save(client);
        newClientRepository.add(client);

        log.info("Migrated client {} successfully", id);

        return client;
    }

    public BulkMigrationResult migrateClientsBulk(List<Long> ids) {
        int successCount = 0;
        int failedCount = 0;
        List<Client> migratedClients = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (Long id : ids) {
            try {
                Client migrated = migrateClient(id);
                migratedClients.add(migrated);
                successCount++;
            } catch (Exception e) {
                failedCount++;
                errors.add("Client ID " + id + ": " + e.getMessage());
                log.warn("Failed to migrate client {}: {}", id, e.getMessage());
            }
        }

        log.info("Bulk migration completed: {} succeeded, {} failed", successCount, failedCount);

        return new BulkMigrationResult(successCount, failedCount, migratedClients, errors);
    }

    public static class BulkMigrationResult {
        private final int successCount;
        private final int failedCount;
        private final List<Client> migratedClients;
        private final List<String> errors;

        public BulkMigrationResult(int successCount, int failedCount, List<Client> migratedClients, List<String> errors) {
            this.successCount = successCount;
            this.failedCount = failedCount;
            this.migratedClients = migratedClients;
            this.errors = errors;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getFailedCount() {
            return failedCount;
        }

        public List<Client> getMigratedClients() {
            return migratedClients;
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}


