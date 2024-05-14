package com.ridesharing.authservice.config.bootstrap;

import com.ridesharing.authservice.model.Role;
import com.ridesharing.authservice.model.User;
import com.ridesharing.authservice.service.RoleService;
import com.ridesharing.authservice.service.UserService;
import com.ridesharing.core.model.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppBootStrap implements ApplicationListener<ContextRefreshedEvent> {

   private final UserService userService;
   private final RoleService roleService;
   private final PasswordEncoder pwdEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(roleService.findAll().isEmpty()){
            roleService.saveAll(List.of(
                    Role.builder().name("ADMIN").build(),
                    Role.builder().name("RIDER").build(),
                    Role.builder().name("DRIVER").build()
            ));
        }

        if(userService.findByEmail("admin@email.com").isEmpty()){

            Role adminRole = null;
            var role = roleService.findByName("ADMIN");

            if(role.isPresent()) adminRole = role.get();

            assert adminRole != null;
            var adminUser = User.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .email("admin@email.com")
                    .type(UserType.ADMIN)
                    .password(pwdEncoder.encode("123456"))
                    .roles(Set.of(adminRole))
                    .build();

            userService.save(adminUser);

        }

//        if(userService.findByEmail("rider1@email.com").isEmpty()){
//
//            Role riderRole = null;
//            var roleR = roleService.findByName("RIDER");
//
//            if(roleR.isPresent()) riderRole = roleR.get();
//
//            assert riderRole != null;
//            var riderUser = User.builder()
//                    .firstName("First")
//                    .lastName("Rider")
//                    .email("rider1@email.com")
//                    .type(UserType.RIDER)
//                    .password(pwdEncoder.encode("123456"))
//                    .roles(Set.of(riderRole))
//                    .build();
//
//            userService.save(riderUser);
//
//        }
//
//        if(userService.findByEmail("driver1@email.com").isEmpty()){
//
//            Role driverRole = null;
//            var roleD = roleService.findByName("DRIVER");
//
//            if(roleD.isPresent()) driverRole = roleD.get();
//
//            assert driverRole != null;
//            var driverUser = User.builder()
//                    .firstName("Driver")
//                    .lastName("1")
//                    .email("driver1@email.com")
//                    .type(UserType.DRIVER)
//                    .password(pwdEncoder.encode("123456"))
//                    .roles(Set.of(driverRole))
//                    .build();
//
//            userService.save(driverUser);
//        }
//
//        if(userService.findByEmail("driver2@email.com").isEmpty()){
//
//            Role driverRole = null;
//            var roleD = roleService.findByName("DRIVER");
//
//            if(roleD.isPresent()) driverRole = roleD.get();
//
//            assert driverRole != null;
//            var driverUser = User.builder()
//                    .firstName("Driver")
//                    .lastName("2")
//                    .email("driver2@email.com")
//                    .type(UserType.DRIVER)
//                    .password(pwdEncoder.encode("123456"))
//                    .roles(Set.of(driverRole))
//                    .build();
//
//            userService.save(driverUser);
//        }
    }
}
