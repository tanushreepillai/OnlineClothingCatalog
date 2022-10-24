package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ClothingRecord;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ClothingRepository extends CrudRepository<ClothingRecord, String> {
}
