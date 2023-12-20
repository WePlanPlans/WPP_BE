package org.tenten.tentenbe.domain.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.test.entity.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
}
