package com.transitops.controller;

import com.transitops.dto.response.VehicleReportResponse;
import com.transitops.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleReportResponse>> getVehicleReports() {
        return ResponseEntity.ok(reportService.getVehicleReports());
    }

    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<VehicleReportResponse> getVehicleReport(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(reportService.getVehicleReport(vehicleId));
    }

    @GetMapping(value = "/vehicles/export", produces = "text/csv")
    @PreAuthorize("hasAnyRole('FLEET_MANAGER', 'FINANCIAL_ANALYST')")
    public ResponseEntity<byte[]> exportVehicleReportsCsv() {
        String csv = reportService.toCsv(reportService.getVehicleReports());
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"vehicle-report.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(bytes);
    }
}
