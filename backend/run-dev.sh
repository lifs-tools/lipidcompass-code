#!/bin/bash
CONFIG_URI=http://localhost:28095/lcconfig EUREKA_URI=http://localhost:28092/eureka LIPIDSPACE_URI=http://localhost:28100/lipidspace/v1 ../mvnw -Plh-dev spring-boot:run 
