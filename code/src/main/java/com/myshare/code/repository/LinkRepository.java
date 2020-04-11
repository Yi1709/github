package com.myshare.code.repository;

import com.myshare.code.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LinkRepository extends JpaRepository<Link, Integer>, JpaSpecificationExecutor<Link> {
}
