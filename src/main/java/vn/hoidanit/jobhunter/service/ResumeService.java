package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.response.resume.ResResumeCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.resume.ResResumeFetchDTO;
import vn.hoidanit.jobhunter.domain.dto.response.resume.ResResumeUpdateDTO;
import vn.hoidanit.jobhunter.repository.ResumeRepository;

@Service
public class ResumeService {

    private ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ResResumeCreateDTO handleCreateResume(Resume resume) {
        // save resume
        Resume newResume = this.resumeRepository.save(resume);

        // convert response
        ResResumeCreateDTO resResumeCreateDTO = new ResResumeCreateDTO(newResume.getId(), newResume.getCreatedAt(),
                newResume.getCreatedBy());
        return resResumeCreateDTO;
    }
    
    public Resume handleFetchResumeById(long id) {
        Optional<Resume> optionalResume = this.resumeRepository.findById(id);
        return optionalResume.isPresent() ? optionalResume.get() : null;
    }

    public ResResumeUpdateDTO handleUpdateResume(Resume updateResume) {
        Resume resume = this.handleFetchResumeById(updateResume.getId());
        if (resume != null) {
            resume.setStatus(updateResume.getStatus());
            resume = this.resumeRepository.save(resume);
        }

        // convert response
        ResResumeUpdateDTO resDTO = new ResResumeUpdateDTO(resume.getUpdatedAt(), resume.getUpdatedBy());
        return resDTO;
    }

    public ResResumeFetchDTO handleConvertFetchDTO(Resume resume) {
        ResResumeFetchDTO res = new ResResumeFetchDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedBy(resume.getUpdatedBy());

        ResResumeFetchDTO.UserResume userResume = new ResResumeFetchDTO.UserResume();
        userResume.setId(resume.getUser().getId());
        userResume.setName(resume.getUser().getName());

        ResResumeFetchDTO.JobResume job = new ResResumeFetchDTO.JobResume();
        job.setId(resume.getJob().getId());
        job.setName(resume.getJob().getName());

        res.setUser(userResume);
        res.setJob(job);
        return res;
    }

    public ResultPaginationDTO handleFetchAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> resumPage = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(resumPage.getTotalPages());
        mt.setTotal(resumPage.getTotalElements());
        rs.setMeta(mt);

        List<ResResumeFetchDTO> listResume = resumPage.getContent()
                .stream().map(item -> this.handleConvertFetchDTO(item))
                .collect(Collectors.toList());
        rs.setResult(listResume);
        return rs;
    }

    public void handleDeleteResumeById(long id) {
        this.resumeRepository.deleteById(id);
    }
}
