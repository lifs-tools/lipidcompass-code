#!/bin/bash
export PATH="./node_installation/bin:./node_installation/node:./node_modules/@angular/cli/bin/:$PATH"
./ng serve --poll 5000 --proxy-config dev-proxy.conf.json --watch true --configuration development lipidcompass-client
