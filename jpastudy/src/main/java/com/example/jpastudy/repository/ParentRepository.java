package com.example.jpastudy.repository;

import com.example.jpastudy.doamin.Child;
import com.example.jpastudy.doamin.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent,Long> {
}
