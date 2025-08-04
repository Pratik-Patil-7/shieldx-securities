package com.shieldx.securities.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.VipPersonDto;
import com.shieldx.securities.model.VipPerson;
import com.shieldx.securities.service.VipPersonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vip")
@CrossOrigin(origins = "*")
public class VIPPersonController {

    private final VipPersonService vipService;

    public VIPPersonController(VipPersonService vipService) {
        this.vipService = vipService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VipPersonDto>> getAllVipPersons(Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        System.out.println("Fetching all VIP persons for user ID: " + userId);
        return ResponseEntity.ok(vipService.getVipPersonsByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> createVipPerson(@Valid @RequestBody VipPersonDto vipPersonDto,
            BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Invalid VIP person data: " + bindingResult.getFieldError().getDefaultMessage()));
        }

        Integer userId = Integer.parseInt(authentication.getName());
        System.out.println("Creating VIP person for user ID: " + userId);
        VipPerson createdVip = vipService.createVipPerson(vipPersonDto);
        return ResponseEntity.ok(Collections.singletonMap("message", "VIP person created successfully with ID: " + createdVip.getVipId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VipPerson> getVipPersonById(@PathVariable Integer id, Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        System.out.println("Fetching VIP person ID: " + id + " for user ID: " + userId);
        return ResponseEntity.ok(vipService.getVipPersonById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> updateVipPerson(@PathVariable Integer id,
            @Valid @RequestBody VipPersonDto vipPersonDto, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Invalid VIP person data: " + bindingResult.getFieldError().getDefaultMessage()));
        }

        Integer userId = Integer.parseInt(authentication.getName());
        System.out.println("Updating VIP person ID: " + id + " for user ID: " + userId);
        VipPerson updatedVip = vipService.updateVipPerson(id, vipPersonDto);
        return ResponseEntity.ok(Collections.singletonMap("message", "VIP person updated successfully with ID: " + updatedVip.getVipId()));
    }

  
}