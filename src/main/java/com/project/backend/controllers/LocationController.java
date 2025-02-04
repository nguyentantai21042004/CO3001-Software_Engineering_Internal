package com.project.backend.controllers;

import com.project.backend.models.Location;
import com.project.backend.responses.ResponseObject;
import com.project.backend.services.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/locations")
@RequiredArgsConstructor
public class LocationController {

        private final LocationService locationService;

        // Get all locations
        @GetMapping("")
        @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'SPSO')")
        public ResponseEntity<ResponseObject> getAllLocations() {
            ResponseObject response = locationService.findAllLocations();
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        // Get location by id
        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'SPSO')")
        public ResponseEntity<ResponseObject> getLocationById(@PathVariable Integer id) {
            ResponseObject response = locationService.findById(id);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        // Add a new location
        @PostMapping
        @PreAuthorize("hasAnyRole('ADMIN')")
        public ResponseEntity<ResponseObject> addLocation(@RequestBody Location location) {
            ResponseObject response = locationService.saveLocation(location);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        // Delete location by id
        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN')")
        public ResponseEntity<ResponseObject> deleteLocation(@PathVariable Integer id) {
            ResponseObject response = locationService.deleteLocation(id);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        // Find by campus
        @GetMapping("/search-by-campus")
        @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'SPSO')")
        public ResponseEntity<ResponseObject> findByCampus(@RequestParam String campus) {
            ResponseObject response = locationService.findByCampus(campus);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        // Find by campus and department
        @GetMapping("/search-by-campus-name")
        @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'SPSO')")
        public ResponseEntity<ResponseObject> findByCampusAndDepartment(@RequestParam String campus,
                @RequestParam String name) {
            ResponseObject response = locationService.findByCampusAndName(campus, name);
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        // Find by campus, department, and floor
        @GetMapping("/search-by-campus-name-floor")
        @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'SPSO')")
        public ResponseEntity<ResponseObject> findByCampusAndDepartmentAndFloor(@RequestParam String campus,
                @RequestParam String name,
                @RequestParam Integer floor) {
            ResponseObject response = locationService.findByCampusAndNameAndFloor(campus, name, floor);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
}
