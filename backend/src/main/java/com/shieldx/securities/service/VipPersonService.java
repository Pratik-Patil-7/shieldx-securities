package com.shieldx.securities.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shieldx.securities.dto.VipPersonDto;
import com.shieldx.securities.model.User;
import com.shieldx.securities.model.VipPerson;
import com.shieldx.securities.repository.UserRepository;
import com.shieldx.securities.repository.VIPpersonRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VipPersonService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VIPpersonRepository vipPersonRepository;


	public List<VipPerson> getAllVipPersons() {
		return vipPersonRepository.findAll();
	}


	public VipPerson getVipPersonById(Integer id) {
		return vipPersonRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("VIP Person not found with id: " + id));
	}

	
	public VipPerson createVipPerson(VipPersonDto vipPersonDto) {
		User user = userRepository.findById(vipPersonDto.getUserId())
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + vipPersonDto.getUserId()));

		VipPerson vipPerson = new VipPerson();
		mapDtoToEntity(vipPersonDto, vipPerson);
		vipPerson.setUser(user);

		return vipPersonRepository.save(vipPerson);
	}


	public VipPerson updateVipPerson(Integer id, VipPersonDto vipPersonDto) {
		VipPerson existingVipPerson = getVipPersonById(id);
		User user = userRepository.findById(vipPersonDto.getUserId())
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + vipPersonDto.getUserId()));

		mapDtoToEntity(vipPersonDto, existingVipPerson);
		existingVipPerson.setUser(user);

		return vipPersonRepository.save(existingVipPerson);
	}



	public List<VipPerson> getVipPersonsByUserId(Integer userId) {
		return vipPersonRepository.findByUserId(userId);
	}

	private void mapDtoToEntity(VipPersonDto dto, VipPerson entity) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setMobile(dto.getMobile());
		entity.setGender(dto.getGender());
		entity.setDateOfBirth(dto.getDateOfBirth());
		entity.setAddress(dto.getAddress());
		entity.setProfession(dto.getProfession());
		entity.setReasonForSecurity(dto.getReasonForSecurity());
	}

}
