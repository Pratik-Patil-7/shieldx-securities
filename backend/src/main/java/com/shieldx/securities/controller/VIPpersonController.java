package com.shieldx.securities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shieldx.securities.dto.AddVIPpersonDTO;
import com.shieldx.securities.dto.VIPpersonProfileDTO;
import com.shieldx.securities.model.VipPerson;
import com.shieldx.securities.service.VIPpersonService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/vipperson")
public class VIPpersonController {

	@Autowired
	private VIPpersonService vipPersoneService;
	
	@PostMapping("/add-vipperson")
	public ResponseEntity<VipPerson> addVipPersone(@Valid @RequestBody AddVIPpersonDTO dto) {
		return ResponseEntity.ok(vipPersoneService.addVipPerson(dto));
	}
	@PutMapping("/update-vipperson/{email}")
	public ResponseEntity<VipPerson>updateVipPersonProfile(@PathVariable String email, @RequestBody  VIPpersonProfileDTO dto){
		return ResponseEntity.ok(vipPersoneService.updateVipPerson(email,dto));
	}
	@DeleteMapping("/delete-vipperson/{id}")
	public ResponseEntity<VipPerson>deleteVipPersonProfile(@PathVariable int id){
		return ResponseEntity.ok(vipPersoneService.deleteVipPerson(id));
	}
	@GetMapping("/get-all-vipperson")
	public ResponseEntity <List<VipPerson>> getAllVipperson() {
		return ResponseEntity.ok(vipPersoneService.getAllVipPerson());
	}
	@GetMapping("/{id}")
	public ResponseEntity<VipPerson> getVIPperson(@PathVariable int id) {
		return ResponseEntity.ok(vipPersoneService.getVIPperson(id));
	}
}
