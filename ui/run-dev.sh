#!/bin/bash
CONFIG_URI=http://localhost:28095/lcconfig EUREKA_URI=http://localhost:28092/eureka ../mvnw -Plh-dev spring-boot:run
