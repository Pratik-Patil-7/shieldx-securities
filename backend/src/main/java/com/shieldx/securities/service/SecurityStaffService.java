package com.shieldx.securities.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shieldx.securities.dto.BouncerRequest;
import com.shieldx.securities.dto.JobApplicationResponse;
import com.shieldx.securities.model.Bouncer;
import com.shieldx.securities.model.JobApplication;
import com.shieldx.securities.repository.BouncerRepository;
import com.shieldx.securities.repository.JobApplicationRepository;

@Service
public class SecurityStaffService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private BouncerRepository bouncerRepository;

    public List<JobApplicationResponse> getAllApplications() {
        return jobApplicationRepository.findAll().stream()
                .map(app -> new JobApplicationResponse(
                    app.getApplicationId(), app.getName(), app.getEmail(), app.getMobile(),
                    app.getDob(), app.getGender(), app.getAddress(), app.getQualification(),
                    app.getExperience(), app.getResumeUrl(), app.getPhotoUrl(), app.getStatus()))
                .collect(Collectors.toList());
    }

    public void approveApplication(Integer applicationId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus("approved");
        jobApplicationRepository.save(application);

        Bouncer bouncer = new Bouncer();
        bouncer.setName(application.getName());
        bouncer.setEmail(application.getEmail());
        bouncer.setMobile(application.getMobile());
        bouncer.setGender(application.getGender());
        bouncer.setAge(String.valueOf(LocalDate.now().getYear() - application.getDob().getYear()));
        bouncer.setAddress(application.getAddress());
        bouncer.setQualification(application.getQualification());
        bouncer.setExperience(application.getExperience());
        bouncer.setResumeUrl(application.getResumeUrl());
        bouncer.setPhotoUrl(application.getPhotoUrl());
        bouncer.setStatus("active");
        bouncerRepository.save(bouncer);
    }

    public void rejectApplication(Integer applicationId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus("rejected");
        jobApplicationRepository.save(application);
    }

    public void addBouncer(BouncerRequest request) {
        Bouncer bouncer = new Bouncer();
        bouncer.setName(request.getName());
        bouncer.setGender(request.getGender());
        bouncer.setAge(request.getAge());
        bouncer.setEmail(request.getEmail());
        bouncer.setMobile(request.getMobile());
        bouncer.setAddress(request.getAddress());
        bouncer.setQualification(request.getQualification());
        bouncer.setExperience(request.getExperience());
        bouncer.setIsArmed(request.getIsArmed());
        bouncer.setStatus("active");
        bouncerRepository.save(bouncer);
    }
}