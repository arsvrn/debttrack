package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.model.Group;
import com.debttrack.platfrom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByAdmin(User admin);
}