package com.legicycle.backend.daos.awsrds;

import com.legicycle.backend.models.awsrds.TestModel;
import org.springframework.data.repository.CrudRepository;

public interface TestModelDao extends CrudRepository<TestModel, Long> {
}
