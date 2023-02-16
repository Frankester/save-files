package com.frankester.savefiles.repositories;

import com.frankester.savefiles.models.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<File,String> {
}
