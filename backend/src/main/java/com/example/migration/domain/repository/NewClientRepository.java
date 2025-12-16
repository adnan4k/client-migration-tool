package com.example.migration.domain.repository;

import com.example.migration.domain.model.Client;

import java.util.List;

public interface NewClientRepository {

    List<Client> findAll();

    void add(Client client);
}


