package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.model.Group;
import com.debttrack.platfrom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByAdmin(User admin);
}