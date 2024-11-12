/* 
 * Copyright 2021 The LipidCompass Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lipidcompass.backend.bootstrap;

import com.arangodb.springframework.core.ArangoOperations;
import org.lipidcompass.backend.repository.AuthorityRepository;
import org.lipidcompass.backend.repository.HasAuthorityRepository;
import org.lipidcompass.backend.repository.UserRepository;
import org.lipidcompass.config.LipidCompassProperties;
import org.lipidcompass.data.model.Authority;
import org.lipidcompass.data.model.HasAuthority;
import org.lipidcompass.data.model.User;
import org.lipidcompass.data.model.Visibility;
import org.lipidcompass.data.model.roles.SystemRole;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nils.hoffmann
 */
@Slf4j
@Component
public class AuthorityAndUserBootstrap {

    @Autowired
    private ArangoOperations operations;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private HasAuthorityRepository hasAuthorityRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
    @Autowired
    private LipidCompassProperties lipidCompassProperties;

    @Autowired
    private BootstrapUserProvider bootstrapUserProvider;

    @Transactional
    public void init() throws DataAccessException {
        log.info("Bootstrapping authorities and users.");
        bootstrapUserProvider.setSecurityContextUser(lipidCompassProperties.getSystemAdminName(), new SimpleGrantedAuthority(SystemRole.ADMIN.toString()));
//        org.springframework.security.core.userdetails.User tokenUser = new org.springframework.security.core.userdetails.User(
//                adminName,
//                passwordEncoder.encode(adminPw),
//                Arrays.asList(new SimpleGrantedAuthority(SystemRole.ADMIN.toString()))
//        );
//        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(tokenUser, tokenUser.getPassword(), tokenUser.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(token);

//        Authentication auth = new UsernamePasswordAuthenticationToken(SystemUsers.SYSTEM_ADMIN, SystemUsers.SYSTEM_ADMIN, Arrays.asList(new SimpleGrantedAuthority(SystemRole.ADMIN.toString())));
//        SecurityContextHolder.getContext().setAuthentication(auth);

//        String userPassword = DockerSecrets.read();
//        String adminPassword = DockerSecrets.read("lipidcompass.systemAdminPassword", lipidCompassProperties.getSystemAdminPassword());

        try {
            String transactionUuid = UUID.randomUUID().toString();
            Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
            log.info("Authentication: {}", contextAuth);
            long authorities = operations.collection(Authority.class).count();
            List<Authority> adminAuthority = authorityRepository.findByName(SystemRole.ADMIN.toString());
            Authority adminAuth = null;
            if (adminAuthority.isEmpty()) {
                adminAuth = new Authority(
                    SystemRole.ADMIN.toString(),
                    "Authority for administrators",
                    transactionUuid,
                    Visibility.PUBLIC
                );
                adminAuth = authorityRepository.save(adminAuth);
            } else {
                adminAuth = adminAuthority.get(0);
            }
            List<Authority> userAuthority = authorityRepository.findByName(SystemRole.USER.toString());
            Authority userAuth = null;
            if (userAuthority.isEmpty()) {
                userAuth = new Authority(
                    SystemRole.USER.toString(),
                    "Authority for users",
                    transactionUuid,
                    Visibility.PUBLIC
                );
                userAuth = authorityRepository.save(userAuth);
            } else {
                userAuth = userAuthority.get(0);
            }
            operations.collection(User.class).count();
            Optional<User> users = userRepository.findByUserName(lipidCompassProperties.getSystemUserName());
            if (users.isEmpty()) {
                User user = userRepository.save(User.builder().
                        transactionUuid(transactionUuid).
                        userName(lipidCompassProperties.getSystemUserName()).
                        password(passwordEncoder.encode(lipidCompassProperties.getSystemUserPassword())).
                        firstName(lipidCompassProperties.getSystemUserName()).
                        familyName(lipidCompassProperties.getSystemUserName()).
                        emailAddress(lipidCompassProperties.getSystemUserEmail()).
                        accountNonExpired(Boolean.TRUE).
                        accountNonLocked(Boolean.TRUE).
                        credentialsNonExpired(Boolean.TRUE).
                        enabled(Boolean.TRUE).
                        visibility(Visibility.PRIVATE).
//                        createdBy(AuthorityAndUserBootstrap.class.getSimpleName()).
//                        dateCreated(new Date()).
                        authorities(Arrays.asList(userAuth)).build());
                hasAuthorityRepository.save(HasAuthority.builder().from(user).to(userAuth).build());
            }
            Optional<User> admins = userRepository.findByUserName(lipidCompassProperties.getSystemAdminName());
            if (admins.isEmpty()) {
                User admin = userRepository.save(User.builder().
                        transactionUuid(transactionUuid).
                        userName(lipidCompassProperties.getSystemAdminName()).
                        password(passwordEncoder.encode(lipidCompassProperties.getSystemAdminPassword())).
                        firstName(lipidCompassProperties.getSystemAdminName()).
                        familyName(lipidCompassProperties.getSystemAdminName()).
                        emailAddress(lipidCompassProperties.getSystemAdminEmail()).
                        accountNonExpired(Boolean.TRUE).
                        accountNonLocked(Boolean.TRUE).
                        credentialsNonExpired(Boolean.TRUE).
                        enabled(Boolean.TRUE).
                        visibility(Visibility.PRIVATE).
//                        createdBy(AuthorityAndUserBootstrap.class.getSimpleName()).
//                        dateCreated(new Date()).
                        authorities(Arrays.asList(adminAuth)).build());
                hasAuthorityRepository.save(HasAuthority.builder().transactionUuid(transactionUuid).from(admin).to(adminAuth).build());
                Optional<User> adminUser = userRepository.findById(admin.getId());
                if (adminUser.isPresent()) {
                    hasAuthorityRepository.save(HasAuthority.builder().transactionUuid(transactionUuid).from(adminUser.get()).to(userAuth).build());
                }
            }
        } finally {
            bootstrapUserProvider.clearSecurityContextUser();
        }
    }

}
