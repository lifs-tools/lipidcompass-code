# Docker Stacks

Lipid Compass requires a number of infrastructural services, that may or may not be present in your environment.
Please note that all stacks in this directory are only meant for development purposes. They may need additional hardening and deployment specifications for a stable, resilient and safe Docker Swarm deployment.

## Authentication Infrastructure
Lipid Compass uses a two-step authentication process, using Keycloak as the main identity provider which allows federated OIDC providers to be configured, such as [Orcid](https://orcid.org/) or [LifeScience Login](https://lifescience-ri.eu/ls-login.html).
However, the local role assignments (authorization) of users are assigned in keycloak locally within the lipidcompass realm. Keycloak currently uses a MySQL database backend.
Additionally, the graph database backend ArangoDB maintains a mapping of user uid and group assignments to objects in the different vertex and edge collections, to establish 
ownership / responsibility and auditing capabilities.

## Routing Infrastructure
Lipid Compass uses [traefik](https://traefik.io) as its Microservice router. Services can register to be exposed by traefik using deployment labels.
Generally, only services that should be available from outside of the swarm should be exposed via traefik, e.g. the microservice 
gateway (frontend). Traefik has lots of possibilities to restrict routing based on host names, paths, etc. Please refer to the traefik documentation for more information.

