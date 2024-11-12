# LipidCompass Gateway

This application uses the Spring Cloud Gateway implementation to relay external REST API calls to
internal services. This also serves as a resource server in the OAUTH2 sense.

Routes to services are automatically discovered via Spring Cloud Registry (Eureka). 
For standard operations, the `backend` service must be up and running. Additionally, the
data-importer may also run. The gateway does not impose additional access restrictions on exposed 
resources. Each service must manage those separately via the mapped OAUTH2 roles (see
`service-core` for servlet and reactive implementations to map Keycloak `resource_access` `roles`
JWT grants to spring security roles).

To reduce the risk of accidentally exposing sensitive endpoints to the public, we advise to use the 
"@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)"
annotation on any Security configuration class and to add the corresponding annotations on the
controller methods (e.g.: ```@Secured("ROLE_ADMIN")``` to restrict access to users with `ADMIN`
privileges).

To run the **complete** Gateway application in spring boot development mode (with reloading of
changed java classes):

1. Run `./run-dev.sh`

To have a quicker turnaround time for frontend development, try the following:

To run the live development server (requires the gateway to run) for the angular application:

1. Go to `src/main/angular/ui`
2. Run `./run-dev.sh`

This will automatically recomplile and reload the angular application.
