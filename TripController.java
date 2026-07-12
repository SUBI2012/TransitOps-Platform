package com.transitops.controller;

import com.transitops.dto.request.CompleteTripRequest;
import com.transitops.dto.request.CreateTripRequest;
import com.transitops.entity.Trip;
import com.transitops.enums.TripStatus;
import com.transitops.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping
    public ResponseEntity<List<Trip>> getAll(@RequestParam(required = false) TripStatus status) {
        return ResponseEntity.ok(status != null ? tripService.getByStatus(status) : tripService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<Trip> create(@Valid @RequestBody CreateTripRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.create(request));
    }

    @PostMapping("/{id}/dispatch")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<Trip> dispatch(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.dispatch(id));
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<Trip> complete(@PathVariable Long id, @Valid @RequestBody CompleteTripRequest request) {
        return ResponseEntity.ok(tripService.complete(id, request));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('DRIVER', 'FLEET_MANAGER')")
    public ResponseEntity<Trip> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.cancel(id));
    }
}
