package com.myshare.code.repository;

import com.myshare.code.entity.ArcType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ArcTypeRepository extends JpaRepository<ArcType, Integer>, JpaSpecificationExecutor<ArcType> {
}
