package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.response.resume.ResResumeCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.resume.ResResumeFetchDTO;
import vn.hoidanit.jobhunter.domain.dto.response.resume.ResResumeUpdateDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private ResumeService resumeService;

    private UserService userService;

    private JobService jobService;

    public ResumeController(ResumeService resumeService,
            UserService userService, JobService jobService) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.jobService = jobService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create new resumes")
    public ResponseEntity<ResResumeCreateDTO> createResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        //TODO: process POST request
        User user = this.userService.handleGetUserById(resume.getUser().getId());
        Optional<Job> job = this.jobService.fetchJobById(resume.getJob().getId());
        if (user == null || !job.isPresent())
            throw new IdInvalidException("User id/Job id không tồn tại!");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleCreateResume(resume));
    }
    
    @PutMapping("/resumes")
    @ApiMessage("Update resumes")
    public ResponseEntity<ResResumeUpdateDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        //TODO: process PUT request
        Resume r = this.resumeService.handleFetchResumeById(resume.getId());
        if (r == null)
            throw new IdInvalidException("Resume với id = " + resume.getId() + " không tồn tại!");
        return ResponseEntity.ok(this.resumeService.handleUpdateResume(resume));
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResResumeFetchDTO> fetchById(@PathVariable("id") long id) 
            throws IdInvalidException {
        Resume resume = this.resumeService.handleFetchResumeById(id);
        if (resume == null)
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại!");
        return ResponseEntity.ok(this.resumeService.handleConvertFetchDTO(resume));
    }
    
    @GetMapping("/resumes")
    public ResponseEntity<ResultPaginationDTO> fetchAllResume( @Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.ok(this.resumeService.handleFetchAllResume(spec, pageable));
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void> deleteResumeById(@PathVariable("id") long id) throws IdInvalidException {
        Resume resume = this.resumeService.handleFetchResumeById(id);
        if (resume == null) 
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại!");
        this.resumeService.handleDeleteResumeById(id);
        return ResponseEntity.ok(null);
    }
    
}
