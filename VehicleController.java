package com.transitops.controller;

import com.transitops.dto.request.VehicleRequest;
import com.transitops.entity.Vehicle;
import com.transitops.enums.VehicleStatus;
import com.transitops.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAll(@RequestParam(required = false) VehicleStatus status) {
        return ResponseEntity.ok(status != null ? vehicleService.getByStatus(status) : vehicleService.getAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Vehicle>> getAvailable() {
        return ResponseEntity.ok(vehicleService.getAvailableForDispatch());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<Vehicle> create(@Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<Vehicle> update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.update(id, request));
    }

    @PostMapping("/{id}/retire")
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<Void> retire(@PathVariable Long id) {
        vehicleService.retire(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
