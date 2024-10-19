package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.response.ResSkillDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class SkillService {

    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean checkExistSkillName(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill checkExistSkillId(long id) {
        return this.skillRepository.findById(id).isPresent() ? this.skillRepository.findById(id).get() : null;
    }

    public Skill handleUpdateSkill(Skill updateSkill) {
        return this.skillRepository.save(updateSkill);
    }

    public ResultPaginationDTO handleFetchAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);
        List<ResSkillDTO> listSkill = pageSkill.getContent()
                .stream().map(item -> new ResSkillDTO(
                        item.getId(),
                        item.getName(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getCreatedBy(),
                        item.getUpdatedBy()))
                .collect(Collectors.toList());
        rs.setResult(listSkill);
        return rs;
    }

    public void deleteSkill(long id) {
        // delete job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        //delete skill
        this.skillRepository.delete(currentSkill);
    }
}
