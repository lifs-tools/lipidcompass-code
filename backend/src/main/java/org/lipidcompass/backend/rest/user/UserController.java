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
package org.lipidcompass.backend.rest.user;

import org.lipidcompass.backend.repository.AuthorityRepository;
import org.lipidcompass.backend.repository.HasAuthorityRepository;
import org.lipidcompass.backend.repository.UserRepository;
import org.lipidcompass.backend.rest.ArangoEntityRepresentationModelAssembler;
import org.lipidcompass.data.model.Authority;
import org.lipidcompass.data.model.HasAuthority;
import org.lipidcompass.data.model.User;
import org.lipidcompass.data.model.Visibility;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import static org.lipidcompass.data.model.Roles.ROLE_ADMIN;
import static org.lipidcompass.data.model.Roles.ROLE_USER;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nils.hoffmann
 */
@RestController
@ExposesResourceFor(User.class)
@RequestMapping(value = "/user",
        produces = "application/hal+json")
@Slf4j
public class UserController {

    private final UserRepository repository;
    private final AuthorityRepository authorityRepository;
    private final HasAuthorityRepository hasAuthorityRepository;
    private final EntityLinks entityLinks;
    private final ArangoEntityRepresentationModelAssembler<User> assembler;

    @Autowired
    public UserController(UserRepository repository, AuthorityRepository authorityRepository, HasAuthorityRepository hasAuthorityRepository,
            EntityLinks entityLinks) {
        this.repository = repository;
        this.authorityRepository = authorityRepository;
        this.hasAuthorityRepository = hasAuthorityRepository;
        this.entityLinks = entityLinks;
        this.assembler = new ArangoEntityRepresentationModelAssembler<>(entityLinks);
    }

    public UserController(UserRepository repository,
            AuthorityRepository authorityRepository,
            HasAuthorityRepository hasAuthorityRepository,
            EntityLinks entityLinks, ArangoEntityRepresentationModelAssembler<User> assembler) {
        this.repository = repository;
        this.authorityRepository = authorityRepository;
        this.hasAuthorityRepository = hasAuthorityRepository;
        this.entityLinks = entityLinks;
        this.assembler = assembler;
    }

    @Secured({ROLE_USER})
    @GetMapping({"", "/"})
    public RepresentationModel index(Principal principal) {
        RepresentationModel index = new RepresentationModel();
        index.add(linkTo(UserController.class).withRel("me"));
        return index;
    }

    @PageableAsQueryParam
    @Secured({ROLE_ADMIN})
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<PagedModel<EntityModel<User>>> get(@ParameterObject Pageable p, Authentication authentication) throws Exception {
        Page<User> page = repository.findAll(p);
        CollectionModel<User> cm = CollectionModel.of(
                page,
                WebMvcLinkBuilder.linkTo(this.getClass()).withSelfRel()
        );
        PagedModel.PageMetadata meta = new PagedModel.PageMetadata(page.getNumberOfElements(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        PagedModel<EntityModel<User>> pagedResources = PagedModel.wrap(cm, meta);
        return ResponseEntity.ok(pagedResources);
    }

    @Secured({ROLE_USER})
    @GetMapping("/me")
    @Transactional
    public ResponseEntity<EntityModel<User>> user(Principal principal) {
        if (principal != null) {
            log.debug("Accessing /me endpoint for principal {} with name {}", principal, principal.getName());
            log.info("Principal class: {}", principal.getClass().getSimpleName());
            if (principal instanceof JwtAuthenticationToken) {
                log.debug("Trying to find user from name.");
                JwtAuthenticationToken ouser = (JwtAuthenticationToken) principal;
                String userName = principal.getName();
                log.debug("Using the following user name: {}", userName);
                Optional<User> oauthUsers = repository.findByUserName(userName);
                String transactionUuid = UUID.randomUUID().toString();
                if (oauthUsers.isEmpty()) {
                    Collection<Authority> authorities = findOrCreateAuthorities(ouser, transactionUuid);
                    final User u = repository.save(
                            User.builder().
                                    transactionUuid(transactionUuid).
                                    userName(userName).
                                    password("KEYCLOAK").
                                    firstName((String) ouser.getTokenAttributes().get("given_name")).
                                    familyName((String) ouser.getTokenAttributes().get("family_name")).
                                    emailAddress((String) ouser.getTokenAttributes().get("email")).
                                    accountNonExpired(Boolean.TRUE).
                                    accountNonLocked(Boolean.TRUE).
                                    credentialsNonExpired(Boolean.TRUE).
                                    enabled(Boolean.TRUE).
                                    visibility(Visibility.PUBLIC).
//                                    createdBy(userName).
//                                    updatedBy(userName).
                                    //                                    authorities(authorities).
                                    build()
                    );
                    for (Authority a : authorities) {
                        findOrCreateHasAuthority(HasAuthority.builder().from(u).to(a).build());
                    }
                    return loadUpdatedUser(u);
                } else {
                    User u = oauthUsers.get();
                    log.debug("Updating user {} in database with latest from OAuth2!", u.getUserName());
                    u.setFirstName((String) ouser.getTokenAttributes().get("given_name"));
                    u.setFamilyName((String) ouser.getTokenAttributes().get("family_name"));
                    u.setEmailAddress((String) ouser.getTokenAttributes().get("email"));
                    Collection<Authority> authorities = findOrCreateAuthorities(ouser, transactionUuid);
                    //                    u.setAuthorities(authorities);
                    final User updatedUser = repository.save(u);
                    for (Authority a : authorities) {
                        findOrCreateHasAuthority(HasAuthority.builder().from(updatedUser).to(a).build());
                    }
                    return loadUpdatedUser(updatedUser);
                }
            }
            return ResponseEntity.notFound().
                    build();
        }
        return ResponseEntity.notFound().build();
    }

    private HasAuthority findOrCreateHasAuthority(HasAuthority ha) {
        List<HasAuthority> activeHa = hasAuthorityRepository.findByToNameAndFromUserName(ha.getTo().getName(), ha.getFrom().getUserName());
        if (activeHa.isEmpty()) {
            log.debug("Saving HasAuthority: {}", ha);
            return hasAuthorityRepository.save(ha);
        }
        log.debug("Returning HasAuthority: {}", activeHa.get(0));
        return activeHa.get(0);
    }

    private Collection<Authority> findOrCreateAuthorities(JwtAuthenticationToken ouser, String transactionUuid) {
        Collection<Authority> authorities = ouser.getAuthorities().stream().filter((ga) -> {
            return ga.getAuthority().startsWith("ROLE_");
        }).map((ga) -> {
            String authority = ga.getAuthority().replaceAll("ROLE_", "");
            List<Authority> auths = authorityRepository.findByName(authority);
            if (auths.isEmpty()) {
                log.debug("Adding missing authority: {}", authority);
                return authorityRepository.save(
                    new Authority(
                        authority, 
                        "Authority for " + authority.toLowerCase() + "s",
                        transactionUuid,
                        Visibility.PUBLIC
                    )
                );
            }
            return auths.get(0);
        }).collect(Collectors.toList());
        log.debug("Found or created the following authorities: {}", authorities);
        return authorities;
    }

    private ResponseEntity<EntityModel<User>> loadUpdatedUser(User u) {
        Optional<User> updatedUser = repository.findById(u.getId());
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(assembler.toModel(updatedUser.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
