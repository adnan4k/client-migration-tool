package com.example.migration.application;

import com.example.migration.domain.model.Client;
import com.example.migration.domain.repository.LegacyClientRepository;
import com.example.migration.domain.repository.NewClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
}


