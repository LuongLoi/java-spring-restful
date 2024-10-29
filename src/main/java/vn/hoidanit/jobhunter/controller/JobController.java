package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.response.job.ResJobCreatedDTO;
import vn.hoidanit.jobhunter.domain.dto.response.job.ResJobUpdatedDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResJobCreatedDTO> createJobs(@Valid @RequestBody Job postmanJob) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleCreateJob(postmanJob));
    }
    
    @PutMapping("/jobs")
    @ApiMessage("Update a job") 
    public ResponseEntity<ResJobUpdatedDTO> updateJobs(@Valid @RequestBody Job updateJob)
            throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(updateJob.getId());
        if (!currentJob.isPresent())
            throw new IdInvalidException("Job với id = " + updateJob.getId() + " không tồn tại!");
        return ResponseEntity.ok(this.jobService.update(updateJob, currentJob.get()));
    }
    
    @DeleteMapping("/delete/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (!currentJob.isPresent())
            throw new IdInvalidException("Job not found");
        this.jobService.delete(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (!currentJob.isPresent())
            throw new IdInvalidException("Job not found");
        return ResponseEntity.ok(currentJob.get());
    }
    
    @GetMapping("/jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJob(
        @Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.ok(this.jobService.getAllJob(spec, pageable));
    }
    
}
