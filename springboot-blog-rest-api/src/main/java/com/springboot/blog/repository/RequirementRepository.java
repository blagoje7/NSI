package com.springboot.blog.repository;

import com.springboot.blog.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    List<Requirement> findByCategoryId(Long categoryId);

}
