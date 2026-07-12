package com.transitops.controller;

import com.transitops.dto.request.DriverRequest;
import com.transitops.entity.Driver;
import com.transitops.enums.DriverStatus;
import com.transitops.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<List<Driver>> getAll(@RequestParam(required = false) DriverStatus status) {
        return ResponseEntity.ok(status != null ? driverService.getByStatus(status) : driverService.getAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Driver>> getAvailable() {
        return ResponseEntity.ok(driverService.getAvailableForDispatch());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('FLEET_MANAGER', 'SAFETY_OFFICER')")
    public ResponseEntity<Driver> create(@Valid @RequestBody DriverRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('FLEET_MANAGER', 'SAFETY_OFFICER')")
    public ResponseEntity<Driver> update(@PathVariable Long id, @Valid @RequestBody DriverRequest request) {
        return ResponseEntity.ok(driverService.update(id, request));
    }

    @PostMapping("/{id}/suspend")
    @PreAuthorize("hasAnyRole('FLEET_MANAGER', 'SAFETY_OFFICER')")
    public ResponseEntity<Driver> suspend(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.suspend(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
