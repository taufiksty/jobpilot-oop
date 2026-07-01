package repository;

import java.util.Optional;

import model.Company;

public interface CompanyRepository {

    Optional<Company> findById(int id);

    Optional<Company> findByName(String name);

    Company save(Company company);
}
