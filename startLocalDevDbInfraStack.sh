#!/bin/bash
SKIP_BUILD=0
POSITIONAL=()
while test $# -gt 0
do
    case "$1" in
        --skipBuild)
	    SKIP_BUILD=1
            ;;
        *) echo "argument $1"
	    POSITIONAL+=("$1")
            ;;
    esac
    shift
done
mkdir -p logs/
export DOCKER_DEFAULT_PLATFORM=linux/amd64
PLATFORM=""
if [[ $(uname) == "Darwin" ]]; then
	export PLATFORM="linux/arm64"
else
    export PLATFORM="linux/amd64"
fi
export LC_DEPLOY_ENV=""
export LC_VERSION=$(./mvnw org.apache.maven.plugins:maven-help-plugin:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)')$LC_DEPLOY_ENV
echo "Starting for version $LC_VERSION..."
if [ "$SKIP_BUILD" == 0 ] 
then
	./mvnw -Plh-dev,docker-local install ${POSITIONAL[@]}
	RETVAL="$?"
	if [ $RETVAL -ne 0 ]
       	then
	    echo "Maven build failed with code $RETVAL. Please fix issues before retrying!"
	    exit 1
	fi
else
	echo "Skipping maven build! Re-using previously built Docker images!"
fi
echo "Preparing LipidCompass Authentication stack..."
echo "Creating Keycloak secrets (do not use for production deployments!)..."
KEYCLOAK_USER_NAME="keycloak"
KEYCLOAK_USER_PASSWORD="keycloak"
KEYCLOAK_DB_ROOT_PASSWORD="keycloakdbroot"
KEYCLOAK_DB_USER_NAME="keycloakdb"
KEYCLOAK_DB_USER_PASSWORD="keycloakdb"

echo "keycloak_user_name=$KEYCLOAK_USER_NAME"
#printf $KEYCLOAK_USER_NAME | docker secret create keycloak_user_name -
printf $KEYCLOAK_USER_NAME > docker/.keycloak_user_name
echo "keycloak_user_password=$KEYCLOAK_USER_PASSWORD"
#printf $KEYCLOAK_USER_PASSWORD | docker secret create keycloak_user_password -
printf $KEYCLOAK_USER_PASSWORD > docker/.keycloak_user_password
echo "keycloak_user_password=$KEYCLOAK_DB_ROOT_PASSWORD"
#printf $KEYCLOAK_DB_ROOT_PASSWORD | docker secret create keycloak_db_root_password -
printf $KEYCLOAK_DB_ROOT_PASSWORD > docker/.keycloak_db_root_password
echo "keycloak_user_password=$KEYCLOAK_DB_USER_NAME"
#printf $KEYCLOAK_DB_USER_NAME | docker secret create keycloak_db_user_name -
printf $KEYCLOAK_DB_USER_NAME > docker/.keycloak_db_user_name
echo "keycloak_user_password=$KEYCLOAK_DB_USER_PASSWORD"
#printf $KEYCLOAK_DB_USER_PASSWORD | docker secret create keycloak_db_user_password -
printf $KEYCLOAK_DB_USER_PASSWORD > docker/.keycloak_db_user_password

echo "Creating Keycloak volumes..."
docker volume create lc-dev-mysql-volume
docker volume create keycloak-dev-volume
echo "Launching lipidcompass_dev_auth stack"!
cd docker
docker compose -f docker-compose-lipidcompass_dev_auth.yml up -d
#docker stack deploy --with-registry-auth --compose-file docker-compose-lipidcompass_dev_auth.yml lipidcompass_dev_auth
cd ../

echo "Preparing LipidCompass Infrastructure stack..."
ARANGO_PW="As78ACS0asK"
echo "Setting Arangodb password as secret"
#printf $ARANGO_PW | docker secret create lipidcompass_dev_infra_arangodb -
printf $ARANGO_PW > docker/.lipidcompass_dev_infra_arangodb
echo "Access ArangoDB console as root using $ARANGO_PW"

echo "Setting LipidCompass System User password as secret"
LIPIDCOMPASS_SYSTEM_USER_PASSWORD="testuser"
#printf "$LIPIDCOMPASS_SYSTEM_USER_PASSWORD" | docker secret create lipidcompass_dev_systemuser_password -
printf $LIPIDCOMPASS_SYSTEM_USER_PASSWORD > docker/.lipidcompass_dev_systemuser_password
echo "Setting LipidCompass System Admin password as secret"
LIPIDCOMPASS_SYSTEM_ADMIN_PASSWORD="testadmin"
#printf "$LIPIDCOMPASS_SYSTEM_USER_PASSWORD" | docker secret create lipidcompass_dev_systemadmin_password -
printf $LIPIDCOMPASS_SYSTEM_ADMIN_PASSWORD > docker/.lipidcompass_dev_systemadmin_password

echo "Setting Minio Root User Name as secret"
MINIO_ROOT_USER="minio"
#printf "$MINIO_ROOT_USER" | docker secret create lipidcompass_dev_infra_minio_user -
printf $MINIO_ROOT_USER > docker/.lipidcompass_dev_infra_minio_user
echo "Setting Minio Root User Name as secret"
MINIO_ROOT_PASSWORD="minio1234567"
echo "Access Minio as minio using $MINIO_ROOT_PASSWORD"
#printf "$MINIO_ROOT_PASSWORD" | docker secret create lipidcompass_dev_infra_minio_password -
printf $MINIO_ROOT_PASSWORD > docker/.lipidcompass_dev_infra_minio_password

docker network create -d overlay --attachable --subnet=10.100.10.0/24 --gateway=10.100.10.1 proxy
docker network create -d overlay --attachable --subnet=10.100.4.0/24 --gateway=10.100.4.1 lipidcompass_dev
# The volumes should be created manually for production systems
#docker volume create lc-dev-solr-1
#docker volume create lc-dev-solr-1-backup
#docker volume create lc-dev-solr-2
#docker volume create lc-dev-solr-2-backup
docker volume create arangodb-dev-data-volume
docker volume create arangodb-dev-apps-volume
docker volume create lc-dev-minio
echo "Setting arangodb recommended system settings"
if [[ $(uname) == "Darwin" ]]; then
	echo "Skipping setting arangodb recommended system settings on macOS (OSX)"
else
	echo "Setting arangodb recommended system settings"
	./set-arango-settings.sh
fi
cd docker
echo "Launching lipidcompass_dev_dbinfra stack!"
docker compose -f docker-compose-lipidcompass_dev_dbinfra.yml up -d
#docker stack deploy --with-registry-auth --compose-file docker-compose-lipidcompass_dev_dbinfra.yml lipidcompass_dev_dbinfra
SLEEP_TIME="30"
echo -e "Waiting $SLEEP_TIME seconds for applications to launch!"
sleep $SLEEP_TIME

# pretty print the application names, ip addresses and ports and login information
# do this for arangodb, minio, keycloak and lipidcompass
echo "Keycloak Services:"
docker ps -f "name=lipidcompass_dev_auth_*" --format "table {{.Names}}\t{{.Ports}}"
echo "Login to Keycloak at http://127.0.0.1:28080/auth/ using $KEYCLOAK_USER_NAME:$KEYCLOAK_USER_PASSWORD"
echo "DB Infra Services:"
docker ps -f "name=lipidcompass_dev_dbinfra*" --format "table {{.Names}}\t{{.Ports}}"
echo "Login to ArangoDB at http://127.0.0.1:28091 using root:$ARANGO_PW"
echo "Login to Minio at http://127.0.0.1:9001 using $MINIO_ROOT_USER:$MINIO_ROOT_PASSWORD"
#firefox http://127.0.0.1:28091 http://127.0.0.1:8983/solr/#/ http://127.0.0.1:9001
# check if we are on osx
if [[ $(uname) == "Darwin" ]]; then
    # Code to run on macOS (OSX)
    echo "Running on macOS (OSX)"
    open http://127.0.0.1:28091 http://127.0.0.1:28092 http://127.0.0.1:9001
else
    # Code to run on other operating systems
    echo "Not running on macOS (OSX)"
    firefox http://127.0.0.1:28091 http://127.0.0.1:28092 http://127.0.0.1:9001
fi
cd ../
