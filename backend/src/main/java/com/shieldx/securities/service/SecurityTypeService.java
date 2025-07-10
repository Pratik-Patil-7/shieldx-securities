package com.shieldx.securities.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shieldx.securities.dto.SecurityTypeRequest;
import com.shieldx.securities.dto.SecurityTypeResponse;
import com.shieldx.securities.model.SecurityType;
import com.shieldx.securities.repository.SecurityTypeRepository;

@Service
public class SecurityTypeService {

	@Autowired
	private SecurityTypeRepository securityTypeRepo;

	public List<String> getSecurityType() {
		return securityTypeRepo.findAll().stream().map(SecurityType::getLevelName).distinct().toList();
	}

	public List<String> getSecurityLevel() {
		return securityTypeRepo.findAll().stream().map(SecurityType::getLevelName).distinct().toList();
	}

	public List<SecurityType> getAllPricing() {
		return securityTypeRepo.findAll();
	}
	
	 public List<SecurityType> getAllSecurityTypes() {
	        return securityTypeRepo.findAll();
	    }
	 
	 
	 public List<SecurityTypeResponse> getAllSecurityType() {
	        return securityTypeRepo.findAll().stream()
	                .map(st -> new SecurityTypeResponse(st.getStId(), st.getLevelName(), 
	                                                   st.getDescription(), st.getIsArmed(), st.getPricePerDay()))
	                .collect(Collectors.toList());
	    }

	    public List<String> getAllSecurityLevels() {
	        return securityTypeRepo.findAll().stream()
	                .map(SecurityType::getLevelName)
	                .collect(Collectors.toList());
	    }

	    public List<SecurityTypeResponse> getPricingDetails() {
	        return getAllSecurityType();
	    }

	    public SecurityTypeResponse addSecurityType(SecurityTypeRequest request) {
	        SecurityType securityType = new SecurityType();
	        securityType.setLevelName(request.getLevelName());
	        securityType.setDescription(request.getDescription());
	        securityType.setIsArmed(request.getIsArmed());
	        securityType.setPricePerDay(request.getPricePerDay());
	        securityTypeRepo.save(securityType);
	        return new SecurityTypeResponse(securityType.getStId(), securityType.getLevelName(), 
	                                       securityType.getDescription(), securityType.getIsArmed(), 
	                                       securityType.getPricePerDay());
	    }

	    public SecurityTypeResponse updateSecurityType(Integer stId, SecurityTypeRequest request) {
	        SecurityType securityType = securityTypeRepo.findById(stId)
	                .orElseThrow(() -> new RuntimeException("Security type not found"));
	        securityType.setLevelName(request.getLevelName());
	        securityType.setDescription(request.getDescription());
	        securityType.setIsArmed(request.getIsArmed());
	        securityType.setPricePerDay(request.getPricePerDay());
	        securityTypeRepo.save(securityType);
	        return new SecurityTypeResponse(securityType.getStId(), securityType.getLevelName(), 
	                                       securityType.getDescription(), securityType.getIsArmed(), 
	                                       securityType.getPricePerDay());
	    }

	    public void deleteSecurityType(Integer stId) {
	        SecurityType securityType = securityTypeRepo.findById(stId)
	                .orElseThrow(() -> new RuntimeException("Security type not found"));
	        securityTypeRepo.delete(securityType);
	    }

}
