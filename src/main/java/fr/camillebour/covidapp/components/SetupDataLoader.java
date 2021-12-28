package fr.camillebour.covidapp.components;

import fr.camillebour.covidapp.models.Privilege;
import fr.camillebour.covidapp.models.Role;
import fr.camillebour.covidapp.models.User;
import fr.camillebour.covidapp.repositories.PrivilegeRepository;
import fr.camillebour.covidapp.repositories.RoleRepository;
import fr.camillebour.covidapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege deletePrivilege
                = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege, deletePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", List.of(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");
        User adminUser = new User();
        adminUser.setFirstName("Admin");
        adminUser.setLastName("Istrator");
        adminUser.setPassword(passwordEncoder.encode("admin2022"));
        adminUser.setEmail("admin@test.com");
        adminUser.setRoles(List.of(adminRole));
        adminUser.setEnabled(true);

        User regularUser = new User();
        regularUser.setFirstName("John");
        regularUser.setLastName("Doe");
        regularUser.setPassword(passwordEncoder.encode("user2022"));
        regularUser.setEmail("john@test.com");
        regularUser.setRoles(List.of(userRole));
        regularUser.setEnabled(true);

        userRepository.save(adminUser);
        userRepository.save(regularUser);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
