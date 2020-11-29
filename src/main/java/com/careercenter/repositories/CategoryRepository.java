package com.careercenter.repositories;

import com.careercenter.model.ApplicantLanguage;
import com.careercenter.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoryByApplicantId(Long applicantId);
}
