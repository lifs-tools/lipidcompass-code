#!/bin/bash
killProcAll() {
    trap '' INT TERM     # ignore INT and TERM while shutting down
    echo "**** Shutting down... ****"     # added double quotes
    echo "Killing ${BACKEND_PID}"
    kill -TERM ${BACKEND_PID-0}         # fixed order, send TERM not INT
    echo "Killing ${DATAIMPORTER_PID}"
    kill -TERM ${DATAIMPORTER_PID-0}         # fixed order, send TERM not INT
    echo "Killing ${FRONTEND_PID}"
    kill -TERM ${FRONTEND_PID-0}         # fixed order, send TERM not INT
    trap - SIGINT SIGTERM # clear the trap
    kill -- -$$ # Sends SIGTERM to child/sub processes
    echo DONE
}
SKIP_BUILD=0
while test $# -gt 0
do
    case "$1" in
        --skipBuild)
            SKIP_BUILD=1
            ;;
        *) echo "argument $1"
            ;;
    esac
    shift
done

#trap killProcAll SIGINT SIGTERM
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
	echo "You can use --skipBuild to skip maven rebuilds!"
	./mvnw -Plh-dev,docker-local install
        if [ $RETVAL -ne 0 ]
       	then
            echo "Maven build failed with code $RETVAL. Please fix issues before retrying!"
            exit 1
        fi
else
        echo "Skipping maven build! Re-using previously built Docker images!"
fi

docker stack ls | grep traefik
TRAEFIK_STACK_DEAD=$?
if [ $TRAEFIK_STACK_DEAD -eq 1 ]
then # launch traefik stack if not running
    echo "Launching traefik stack!"
    cd docker
    docker compose -f docker-compose-traefik.yml up -d
    #docker stack deploy --with-registry-auth --compose-file docker-compose-traefik.yml traefik
    cd ../
else
    echo "traefik stack is already deployed, skipping redeployment!"
fi	
docker stack ls | grep lipidcompass_dev_auth
AUTH_STACK_DEAD=$?
if [ $AUTH_STACK_DEAD -eq 1 ]
then # launch auth stack if not running
    echo "Launching lipidcompass_dev_auth stack!"
    cd docker
    docker compose -f docker-compose-lipidcompass_dev_auth.yml up -d
    #docker stack deploy --with-registry-auth --compose-file docker-compose-lipidcompass_dev_auth.yml lipidcompass_dev_auth
    cd ../
else
    echo "lipidcompass_dev_auth stack is already deployed, skipping redeployment!"
fi	

echo "Creating secrets (do not use for production deployments!)..."
BATCHDB_PASSWORD="batchdb"

echo "lipidcompass_dev_infra_batchdb=$BATCHDB_PASSWORD"
#printf $BATCHDB_PASSWORD | docker secret create lipidcompass_dev_infra_batchdb -
printf $BATCHDB_PASSWORD > docker/.lipidcompass_dev_infra_batchdb

echo "Creating volumes..."
docker volume create batch-db-volume

echo "Launching lipidcompass_dev_apps stack!"
cd docker
docker compose -f docker-compose-lipidcompass_dev_apps.yml up -d
#docker stack deploy --with-registry-auth --compose-file docker-compose-lipidcompass_dev_apps.yml lipidcompass_dev_apps
cd ../

sleep 10
# check if we are on osx
if [[ $(uname) == "Darwin" ]]; then
    # Code to run on macOS (OSX)
    echo "Running on macOS (OSX)"
    open http://127.0.0.1/auth http://127.0.0.1:28091 http://127.0.0.1:8080 http://127.0.0.1:28095
else
    # Code to run on other operating systems
    echo "Not running on macOS (OSX)"
    firefox http://127.0.0.1/auth http://127.0.0.1:28091 http://127.0.0.1:8080 http://127.0.0.1:28095
fi

