package repository;

import java.util.Optional;

import model.Company;

public interface CompanyRepository {

    Optional<Company> findById(String id);

    Optional<Company> findByName(String name);

    Company save(Company company);
}
