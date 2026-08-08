package com.example.controlej2e.controller;

import com.example.controlej2e.dto.ComputerMapper;
import com.example.controlej2e.dto.ComputerRequest;
import com.example.controlej2e.dto.ComputerResponse;
import com.example.controlej2e.entities.Computer;
import com.example.controlej2e.service.ComputerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/computers")
@Tag(name = "Computers", description = "Gestion du parc d'ordinateurs")
public class ComputerController {

    @Autowired
    private ComputerService computerService;

    @Operation(summary = "Créer un ordinateur")
    @PostMapping
    public ResponseEntity<ComputerResponse> create(@Valid @RequestBody ComputerRequest request) {
        Computer created = computerService.addComputer(ComputerMapper.toEntity(request));
        ComputerResponse body = ComputerMapper.toResponse(created);
        return ResponseEntity.created(URI.create("/api/computers/" + body.getIdPc())).body(body);
    }

    @Operation(summary = "Lister tous les ordinateurs (paginé)")
    @GetMapping
    public ResponseEntity<Page<ComputerResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<ComputerResponse> page = computerService.getAllComputers(pageable).map(ComputerMapper::toResponse);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Récupérer un ordinateur par son id")
    @GetMapping("/{id}")
    public ResponseEntity<ComputerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ComputerMapper.toResponse(computerService.getComputerById(id)));
    }

    @Operation(summary = "Mettre à jour un ordinateur existant")
    @PutMapping("/{id}")
    public ResponseEntity<ComputerResponse> update(@PathVariable Long id, @Valid @RequestBody ComputerRequest request) {
        Computer updated = computerService.updateComputer(id, ComputerMapper.toEntity(request));
        return ResponseEntity.ok(ComputerMapper.toResponse(updated));
    }

    @Operation(summary = "Supprimer un ordinateur")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        computerService.deleteComputer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lister les ordinateurs dont le prix est inférieur au seuil donné")
    @GetMapping("/price/{maxPrice}")
    public ResponseEntity<List<ComputerResponse>> getByPrice(@PathVariable double maxPrice) {
        List<ComputerResponse> result = computerService.getComputersByPrice(maxPrice).stream()
                .map(ComputerMapper::toResponse)
                .toList();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Lister les ordinateurs par type de processeur")
    @GetMapping("/processor/{proce}")
    public ResponseEntity<List<ComputerResponse>> getByProcessor(@PathVariable String proce) {
        List<ComputerResponse> result = computerService.getComputersByProce(proce).stream()
                .map(ComputerMapper::toResponse)
                .toList();
        return ResponseEntity.ok(result);
    }
}
