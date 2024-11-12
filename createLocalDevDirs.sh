#!/bin/bash
sudo mkdir -p /var/data/lipidcompass
sudo mkdir -p /var/data/lipidcompass/arangodb/lc-dev-apps-1
sudo mkdir -p /var/data/lipidcompass/arangodb/lc-dev-backup-1
sudo mkdir -p /var/data/lipidcompass/arangodb/lc-dev-bulk-data-1
sudo mkdir -p /var/data/lipidcompass/arangodb/lc-dev-data-1
sudo mkdir -p /var/data/lipidcompass/arangodb/lc-dev-entrypoint-initdb-1
sudo mkdir -p /var/data/lipidcompass/arangodb/lc-dev-export-1
#sudo mkdir -p /var/data/lipidcompass/batch
#sudo mkdir -p /var/data/lipidcompass/configuration
sudo mkdir -p /var/data/lipidcompass/docker
sudo mkdir -p /var/data/lipidcompass/import/external
sudo mkdir -p /var/data/lipidcompass/import/submissions
sudo mkdir -p /var/data/lipidcompass/minio/lc-dev-minio-1

# Set permissions on Linux or MacOS

if [[ $(uname) == "Darwin" ]]; then
    # set to $USER:$GROUP for MacOS
    GROUP=$(id -g -n $USER)
    sudo chown -R $USER:$GROUP /var/data/lipidcompass
    sudo chmod -R 755 /var/data/lipidcompass
else
    sudo chown -R root:root /var/data/lipidcompass
    sudo chmod -R 755 /var/data/lipidcompass
fi
