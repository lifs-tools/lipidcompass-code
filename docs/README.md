# LipidCompass Development Documentation

LipidCompass uses the JAVA programming language and the Spring Boot framework. The project is structured as a set of microservices, which are deployed in a Docker environment. The main services are the backend service, which provides the REST API for data access, and the gateway service, which provides access to the other services and the Angular UI.

Please note that you may need access to the LIFS Tools Docker registry to pull some of the necessary Docker images. Please contact us for more information.

## Building the Project

In order to build LipidCompass for a **development** environment, run:

```bash
./mvnw -Plh-dev install
```

Additionally, if you want to build the docker containers, add the 'docker' profile:

```bash
./mvnw -Plh-dev,docker install
```

For a **production** deployment, use the *lh-prod* profile:

```bash
./mvnw -Plh-prod,docker install
```

After any changes to the domain objects or REST endpoints, use the *generate-openapi-frontend-services* profile with the verify goal:

```bash
  ./mvnw -Pgenerate-openapi-frontend-services verify
```

This may then generate updated typescript angular code that can be used by the frontend. Please add the changed files and commit them.

## Running the Project

Make sure to configure your local Docker settings.xml file (~/.m2/settings.xml) as follows:

```bash
    <profiles>
        ...
        <profile>
            <id>lh-dev</id>
            <properties>
                <KEYCLOAK_CLIENT_SECRET>REPLACE_WITH_KEYCLOAK_CLIENT_SECRET</KEYCLOAK_CLIENT_SECRET>
                <ARANGODB_PASSWORD>REPLACE_WITH_ARANGODB_PASSWORD</ARANGODB_PASSWORD>
                <LIPIDCOMPASS_SYSTEM_USER_PASSWORD>REPLACE_WITH_LIPIDCOMPASS_SYSTEM_USER_PASSWORD</LIPIDCOMPASS_SYSTEM_USER_PASSWORD> 
                <LIPIDCOMPASS_SYSTEM_ADMIN_PASSWORD>REPLACE_WITH_LIPIDCOMPASS_SYSTEM_ADMIN_PASSWORD</LIPIDCOMPASS_SYSTEM_ADMIN_PASSWORD>
                <BATCH_DATABASE_PASSWORD>REPLACE_WITH_SPRING_BATCH_DB_PASSWORD</BATCH_DATABASE_PASSWORD>
            </properties>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>
            lh-dev
        </activeProfile>
    </activeProfiles>
```

In order to run the project for a **development** environment, run:

```bash
./startLocalDevInfraStack.sh
```

This builds the project with the *lh-dev* and *docker* profiles and subsequently deploys the infrastructure docker containers,
e.g. for the ArangoDB database, the SOLR search server, the microservice registry, and the configuration service.

**Take a look at this script to see the Docker secrets that need to be created in order to launch the complete stack. These are also 
referenced in the `docker/*.yml` files**

The lh-dev configuration allows you to run individual services, e.g. the backend or the data-importer from within your IDE 
against the deployed infrastructure services. Just remember to activate the Maven profile *lh-dev*.

You can also skip the Maven and Docker build phase, by running the script as follows:

```bash
./startLocalDevInfraStack.sh --skipBuild
```

### Production System

The setup of the production system is handled by ansible scripts and specialized docker swarm stack files. Please inquire for more information. Operation of the production system requires additional configuration and setup steps, e.g. for the Keycloak OIDC server, the ArangoDB database, the LipidSpace service and the dataset import.

## Module Structure
### Object Models

- model - common object models
- mztab-dbmodel - object models for mzTab files

### Common Components

- core - common components
- service-core - common service components
- backend-core - common backend components

### (Micro-)Services

- backend - main service for data access, exposes REST API
- data-importer - imports data from mzTab files into the database
- gateway (provides access to other services & Angular UI)

### Infrastructure Services

- config - configuration management for Microservices
- registry - registry / lookup of Microservices
- Keycloak (external) OIDC Authentication Server
- ArangoDB Graph Database
- LipidSpace lipidome comparison service

### Frontend

- ui - Angular frontend code

## Other topics
### Exporting the realm with users from keycloak

The following command exports the keycloak realm with all users to a file. This is helpful for backup purposes or for transferring the realm to another keycloak instance and for testing purposes.

Make sure that /tmp/keycloak volume is attached to the container.

```bash
docker exec -it -e JDBC_PARAMS="?useSSL=false" -e DB_ADDR=lcmysql -e DB_VENDOR=mysql -e DB_PORT=3306 -e DB_DATABASE=keycloak -e DB_USER=<REPLACE_WITH_DB_USER> -e DB_PASSWORD=<REPLACE_WITH_DB_PASSWORD> <REPLACE_WITH_KC_CONTAINER_ID> /opt/jboss/keycloak/bin/standalone.sh -Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.realmName=lifs -Dkeycloak.migration.usersExportStrategy=REALM_FILE -Dkeycloak.migration.file=/tmp/keycloak/lifs-realm-all.json
```