package com.example.jpastudy.repository;

import com.example.jpastudy.doamin.Child;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface ChildRepository extends JpaRepository<Child,Long>{

    Page<Child> findByNameStartingWith(String name, Pageable pageable);

}
