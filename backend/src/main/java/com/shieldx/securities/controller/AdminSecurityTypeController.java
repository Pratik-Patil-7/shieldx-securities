package com.shieldx.securities.controller;

import com.shieldx.securities.dto.SecurityTypeRequest;
import com.shieldx.securities.dto.SecurityTypeResponse;
import com.shieldx.securities.service.SecurityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/security-types")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSecurityTypeController {

    @Autowired
    private SecurityTypeService securityTypeService;

    @PostMapping
    public ResponseEntity<SecurityTypeResponse> addSecurityType(@RequestBody SecurityTypeRequest request) {
        return ResponseEntity.ok(securityTypeService.addSecurityType(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SecurityTypeResponse> updateSecurityType(@PathVariable("id") Integer stId, 
                                                                  @RequestBody SecurityTypeRequest request) {
        return ResponseEntity.ok(securityTypeService.updateSecurityType(stId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSecurityType(@PathVariable("id") Integer stId) {
        securityTypeService.deleteSecurityType(stId);
        return ResponseEntity.ok("Security type deleted successfully");
    }
}