package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Skill;

import java.util.List;
import java.util.Optional;


@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill>{
    boolean existsByName(String name);

    Optional<Skill> findById(long id);

    List<Skill> findByIdIn(List<Long> listID);

}
