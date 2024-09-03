package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;
    
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> handleGetAllCompany() {
        return this.companyRepository.findAll();
    }

    public Company handleUpdateCompany(Company company) {
        Company c = this.companyRepository.findById(company.getId());
        if (c != null) {
            c.setAddress(company.getAddress());
            c.setDescription(company.getDescription());
            c.setLogo(company.getLogo());
            c.setName(company.getName());
            return this.companyRepository.save(c);
        }
        return null;
    }

    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }

}
