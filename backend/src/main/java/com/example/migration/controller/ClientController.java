package com.example.migration.controller;

import com.example.migration.application.ClientMigrationService;
import com.example.migration.domain.model.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientMigrationService clientMigrationService;

    public ClientController(ClientMigrationService clientMigrationService) {
        this.clientMigrationService = clientMigrationService;
    }

    @GetMapping("/legacy/clients")
    public List<Client> getLegacyClients() {
        return clientMigrationService.getLegacyClients();
    }

    @GetMapping("/new/clients")
    public List<Client> getNewClients() {
        return clientMigrationService.getNewClients();
    }

    @PostMapping("/migrate/{id}")
    public ResponseEntity<Client> migrateClient(@PathVariable Long id) {
        Client migrated = clientMigrationService.migrateClient(id);
        return ResponseEntity.status(HttpStatus.OK).body(migrated);
    }
}

