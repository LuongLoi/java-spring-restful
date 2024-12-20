package vn.hoidanit.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> handleCreateCompany(@Valid @RequestBody Company postmanCompany) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(postmanCompany));
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> handleGetAllCompany(
        @Filter Specification<Company> spec, Pageable pageable

    ) {
        return ResponseEntity.ok(this.companyService.handleGetAllCompany(spec,pageable));
    }
    
    @PutMapping("/companies")
    public ResponseEntity<Company> handleUpdateCompany(@RequestBody Company updateCompany) {
        return ResponseEntity.ok(this.companyService.handleUpdateCompany(updateCompany));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> handleDeleteCompanyById(@PathVariable("id") long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("fetch company by id")
    public ResponseEntity<Company> handleGetCompanyById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Company> opCompany = this.companyService.findById(id);
        if (!opCompany.isPresent())
            throw new IdInvalidException("Company với id = " + id + " không tồn tại!");
        return ResponseEntity.ok(opCompany.get());
    }
    

}
