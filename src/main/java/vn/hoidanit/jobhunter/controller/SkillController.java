package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private SkillService skillService;

    private SkillRepository skillRepository;

    public SkillController(SkillService skillService, SkillRepository skillRepository) {
        this.skillService = skillService;
        this.skillRepository = skillRepository;
    }

    // Create a skill
    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> handleCreateSkill(@Valid @RequestBody Skill postmanSkill) 
            throws IdInvalidException {
        
        boolean skillName = this.skillService.checkExistSkillName(postmanSkill.getName());
        if (postmanSkill != null && skillName)
            throw new IdInvalidException("Skill đã tồn tại, nhập skill khác!");
        Skill skill = this.skillRepository.save(postmanSkill);
        return ResponseEntity.status(HttpStatus.CREATED).body(skill);
    }
    
    //Update a skill by id
    @PutMapping("/skills")
    public ResponseEntity<Skill> postMethodName(@Valid @RequestBody Skill updateSkill) 
            throws IdInvalidException {
        Skill skill = this.skillService.checkExistSkillId(updateSkill.getId());

        if (skill == null)
            throw new IdInvalidException("Id = " + updateSkill.getId() + " không tồn tại!");

        boolean skillName = this.skillService.checkExistSkillName(updateSkill.getName());
        if (updateSkill != null && skillName)
            throw new IdInvalidException("Skill đã tồn tại, nhập skill khác!");

        skill.setName(updateSkill.getName());
        return ResponseEntity.ok(this.skillService.handleUpdateSkill(skill));
    }
    
    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> handleGetSkills(
            @Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.ok(this.skillService.handleFetchAllSkills(spec, pageable));
    }
    
    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.checkExistSkillId(id);

        if (currentSkill == null)
            throw new IdInvalidException("Id = " + id + " không tồn tại!");
        this.skillService.deleteSkill(id);

        return ResponseEntity.ok(null);
        
    }

}
