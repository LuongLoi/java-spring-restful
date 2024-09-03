package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.service.CompanyService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
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
    public ResponseEntity<List<Company>> handleGetAllCompany() {
        return ResponseEntity.ok(this.companyService.handleGetAllCompany());
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

}
