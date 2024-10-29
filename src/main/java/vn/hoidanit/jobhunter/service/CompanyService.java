package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    private UserRepository userRepository;
    
    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO handleGetAllCompany(Specification<Company> spec ,Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
    }

    public Company handleUpdateCompany(Company company) {
        Optional<Company> com = this.companyRepository.findById(company.getId());
        if (com.isPresent()) {
            Company c = com.get();
            c.setAddress(company.getAddress());
            c.setDescription(company.getDescription());
            c.setLogo(company.getLogo());
            c.setName(company.getName());
            return this.companyRepository.save(c);
        }
        return null;
    }

    public Optional<Company> findById(long id) {
        return this.companyRepository.findById(id);
    }

    public void handleDeleteCompany(long id) {
        Optional<Company> company = this.findById(id);
        if (company != null) {
            Company com = company.get();
            //fetch all user belong to company
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }
        this.companyRepository.deleteById(id);
    }

}
