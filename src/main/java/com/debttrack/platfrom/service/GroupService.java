package com.debttrack.platfrom.service;

import com.debttrack.platfrom.model.Group;
import com.debttrack.platfrom.model.GroupUser;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.repository.GroupRepository;
import com.debttrack.platfrom.repository.GroupUserRepository;
import com.debttrack.platfrom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final UserRepository userRepository;

    public Group createGroup(String name, Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        Group group = new Group();
        group.setName(name);
        group.setAdmin(admin);
        return groupRepository.save(group);
    }

    public void addUserToGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (groupUserRepository.findByGroup(group).stream().anyMatch(gu -> gu.getUser().equals(user))) {
            throw new RuntimeException("User already in group");
        }

        GroupUser groupUser = new GroupUser();
        groupUser.setGroup(group);
        groupUser.setUser(user);
        groupUserRepository.save(groupUser);
    }

    public void removeUserFromGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupUser groupUser = groupUserRepository.findByGroup(group).stream()
                .filter(gu -> gu.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not in group"));

        groupUserRepository.delete(groupUser);
    }

    public List<User> getGroupMembers(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return groupUserRepository.findByGroup(group).stream()
                .map(GroupUser::getUser)
                .toList();
    }
}