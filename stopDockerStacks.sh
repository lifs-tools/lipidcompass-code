#!/bin/bash
STOP_ALL_STACKS=false
STOP_AUTH_STACK=false
STOP_DBINFRA_STACK=false

usage() {
    echo "Usage: ./stopDockerStacks.sh [--all|-a] [--auth|-u] [--dbinfra|-d]"
    echo "Stop docker stacks"
    echo "  --all, -a: stop all stacks"
    echo "  --auth, -u: stop only auth stack"
    echo "  --dbinfra, -d: stop only dbinfra stack"
}

# Parse command line arguments using getopts
# --all: stop all stacks
# --auth: stop only auth stack
# --dbinfra: stop only dbinfra stack
# If no arguments are provided, print usage
VALID_ARGS=$(getopt -o 'aud' --long all,auth,dbinfra -- "$@")
# Check if getopt encountered an error and print usage
if [[ $? -ne 0 ]]; then
    echo "No arguments supplied";
    usage
    exit 1;
else
    echo "Arguments supplied";
fi
eval set -- "$VALID_ARGS"
while [ : ]; do
  case "$1" in
    -a|--all)
      STOP_ALL_STACKS=true
      shift
      ;;
    -u|--auth)
      STOP_AUTH_STACK=true
      shift
      ;;
    -d|--dbinfra)
      STOP_DBINFRA_STACK=true
      shift
      ;;
    --)
      break
      ;;
    *)
      echo "Invalid argument"
      usage
      exit 1
      ;;
  esac
done

export DOCKER_DEFAULT_PLATFORM=linux/amd64
PLATFORM=""
if [[ $(uname) == "Darwin" ]]; then
	export PLATFORM="linux/arm64"
else
    export PLATFORM="linux/amd64"
fi
export LC_DEPLOY_ENV=""
export LC_VERSION=$(./mvnw org.apache.maven.plugins:maven-help-plugin:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)')$LC_DEPLOY_ENV
echo "Project version $LC_VERSION..."

if [ "$STOP_AUTH_STACK" = true ] || [ "$STOP_ALL_STACKS" = true ]; then
    echo "Stopping lipidcompass_dev_auth stack"!
    cd docker
    docker compose -f docker-compose-lipidcompass_dev_auth.yml down
    cd ../
fi

if [ "$STOP_DBINFRA_STACK" = true ] || [ "$STOP_ALL_STACKS" = true ]; then
    echo "Stopping lipidcompass_dev_dbinfra stack"!
    cd docker
    docker compose -f docker-compose-lipidcompass_dev_dbinfra.yml down
    cd ../
fi