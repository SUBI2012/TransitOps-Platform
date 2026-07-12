package com.transitops.controller;

import com.transitops.dto.request.CloseMaintenanceRequest;
import com.transitops.dto.request.MaintenanceRequest;
import com.transitops.entity.MaintenanceLog;
import com.transitops.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<List<MaintenanceLog>> getAll(@RequestParam(required = false) Boolean activeOnly) {
        return ResponseEntity.ok(Boolean.TRUE.equals(activeOnly) ? maintenanceService.getActive() : maintenanceService.getAll());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<MaintenanceLog>> getByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(maintenanceService.getByVehicle(vehicleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceLog> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<MaintenanceLog> create(@Valid @RequestBody MaintenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceService.create(request));
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<MaintenanceLog> close(@PathVariable Long id, @RequestBody(required = false) CloseMaintenanceRequest request) {
        return ResponseEntity.ok(maintenanceService.close(id, request != null ? request : new CloseMaintenanceRequest()));
    }
}
