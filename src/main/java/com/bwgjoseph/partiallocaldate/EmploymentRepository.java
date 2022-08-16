package com.bwgjoseph.partiallocaldate;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentRepository extends MongoRepository<EmploymentDO, String> {

}
