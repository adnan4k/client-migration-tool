package com.example.migration.infrastructure.inmemory;

import com.example.migration.domain.model.Client;
import com.example.migration.domain.repository.LegacyClientRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryLegacyClientRepository implements LegacyClientRepository {

    private final List<Client> legacyClients = new ArrayList<>();

    public InMemoryLegacyClientRepository() {
        // Seed some in-memory legacy clients
        legacyClients.add(new Client(1L, "Alice Johnson", "alice@example.com", false));
        legacyClients.add(new Client(2L, "Bob Smith", "bob@example.com", false));
        legacyClients.add(new Client(3L, "Charlie Brown", "charlie@example.com", false));
    }

    @Override
    public List<Client> findAll() {
        return Collections.unmodifiableList(legacyClients);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return legacyClients.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void save(Client client) {
        // In this simple in-memory example, the client is already in the list,
        // so we do not need to add it again. This method is here to express intent.
    }
}


