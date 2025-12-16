package com.example.migration.infrastructure.inmemory;

import com.example.migration.domain.model.Client;
import com.example.migration.domain.repository.NewClientRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class InMemoryNewClientRepository implements NewClientRepository {

    private final List<Client> newClients = new ArrayList<>();

    @Override
    public List<Client> findAll() {
        return Collections.unmodifiableList(newClients);
    }

    @Override
    public void add(Client client) {
        newClients.add(client);
    }
}


