package com.example.catalogservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.service.CatalogSerivce;
import com.example.catalogservice.vo.ResponseCatalog;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/catalog-service")
@AllArgsConstructor
public class CatalogController {

    Environment env;
    CatalogSerivce catalogSerivce;

    @GetMapping("/heath_check")
    public String status() {
        return String.format("It's Working in User Service on PORT %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        Iterable<CatalogEntity> catalogList = catalogSerivce.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        catalogList.forEach(v -> {
            result.add(modelMapper.map(v, ResponseCatalog.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
