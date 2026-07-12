package com.transitops.controller;

import com.transitops.dto.request.FuelLogRequest;
import com.transitops.entity.FuelLog;
import com.transitops.service.FuelLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel-logs")
@RequiredArgsConstructor
public class FuelLogController {

    private final FuelLogService fuelLogService;

    @GetMapping
    public ResponseEntity<List<FuelLog>> getAll(@RequestParam(required = false) Long vehicleId) {
        return ResponseEntity.ok(vehicleId != null ? fuelLogService.getByVehicle(vehicleId) : fuelLogService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FLEET_MANAGER', 'DRIVER', 'FINANCIAL_ANALYST')")
    public ResponseEntity<FuelLog> create(@Valid @RequestBody FuelLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fuelLogService.create(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('FLEET_MANAGER', 'FINANCIAL_ANALYST')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fuelLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
