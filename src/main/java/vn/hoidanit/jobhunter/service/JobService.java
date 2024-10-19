package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.response.job.ResJobCreatedDTO;
import vn.hoidanit.jobhunter.domain.dto.response.job.ResJobUpdatedDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private JobRepository jobRepository;
    private SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResJobCreatedDTO handleCreateJob(Job job) {
        // check skill
        if (job.getSkills() != null) {
            List<Long> skillIds = job.getSkills()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(skillIds);
            job.setSkills(dbSkills);
        }

        // create Job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResJobCreatedDTO dto = new ResJobCreatedDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setLocation(currentJob.getLocation());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLevel(currentJob.getLevel());
        dto.setDescription(currentJob.getDescription());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> listSkill = currentJob.getSkills()
                    .stream().map(x -> x.getName()).collect(Collectors.toList());
            dto.setSkills(listSkill);
        }
        return dto;
    }
    
    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public ResJobUpdatedDTO update(Job job) {
        // check skill
        if (job.getSkills() != null) {
            List<Long> skillIds = job.getSkills()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(skillIds);
            job.setSkills(dbSkills);
        }

        // create Job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResJobUpdatedDTO dto = new ResJobUpdatedDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setLocation(currentJob.getLocation());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLevel(currentJob.getLevel());
        dto.setDescription(currentJob.getDescription());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setUpdatedAt(currentJob.getUpdatedAt());
        dto.setUpdatedBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> listSkill = currentJob.getSkills()
                    .stream().map(x -> x.getName()).collect(Collectors.toList());
            dto.setSkills(listSkill);
        }
        return dto;
    }

    public void delete(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO getAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        rs.setMeta(mt);
        List<Job> listSkill = pageJob.getContent();
        rs.setResult(listSkill);
        return rs;
    }
}
    
