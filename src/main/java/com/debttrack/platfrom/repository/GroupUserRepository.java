package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.model.Group;
import com.debttrack.platfrom.model.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    List<GroupUser> findByGroup(Group group);
}