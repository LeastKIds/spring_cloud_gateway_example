package com.example.catalogservice.service;

import com.example.catalogservice.jpa.CatalogEntity;

public interface CatalogSerivce {
    Iterable<CatalogEntity> getAllCatalogs();
}
