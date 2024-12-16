package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.model.Group;
import com.debttrack.platfrom.model.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    List<GroupUser> findByGroup(Group group);
}