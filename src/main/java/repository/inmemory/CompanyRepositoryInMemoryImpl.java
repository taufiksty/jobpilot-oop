package repository.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import exception.HandleException;
import model.Company;
import repository.CompanyRepository;

public class CompanyRepositoryInMemoryImpl implements CompanyRepository {

    private final List<Company> companies = new ArrayList<>();

    @Override
    public Optional<Company> findById(String id) {
        return companies.stream().filter(company -> company.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Company> findByName(String name) {
        return companies.stream().filter(company -> company.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public Company save(Company company) {
        if (company.getId() == null) {
            company.setId(UUID.randomUUID().toString());
            companies.add(company);
            return company;
        } else {
            // Update existing company
            Optional<Company> existingCompanyOpt = findById(company.getId());
            if (existingCompanyOpt.isPresent()) {
                Company existingCompany = existingCompanyOpt.get();
                existingCompany.setName(company.getName());
                existingCompany.setUpdatedAt(company.getUpdatedAt());
                existingCompany.setDeletedAt(company.getDeletedAt());
                existingCompany.setCreatedBy(company.getCreatedBy());
                existingCompany.setUpdatedBy(company.getUpdatedBy());
                existingCompany.setDeletedBy(company.getDeletedBy());
                return existingCompany;
            } else {
                throw new HandleException("Company with id " + company.getId() + " does not exist.");
            }
        }
    }

}
