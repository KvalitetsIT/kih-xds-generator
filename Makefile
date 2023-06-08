.DEFAULT_GOAL := help

## #################################################################
## Parameters to override
## #################################################################

BRANCH := "master"
TAG ?=

ifndef TAG
DOCKER_TAG := master
TAG := master
else
DOCKER_TAG := ${TAG}
endif

REFLEX := $(shell command -v reflex 2> /dev/null) # Helps  to restart on changes


## #################################################################
## Targets
## #################################################################
documentation: README.org ### Compiles the README.md from README.org
	emacs README.org --batch -f org-md-export-to-markdown --kill

clean: ## Cleans source code and dependencies
	@echo "Cleaning source code and removing dependencies..."
	@./gradlew clean
	@rm -rf release

setup: clean ## Runs setup if any
	@echo "Setting up project..."

compile: clean setup ## Compiles source code
	@echo "Compiling source code..."
	@./gradlew build

format: setup compile ## Runs formatter
	@echo "Formatting source code..."

test: clean setup ## Run tests for component
	@echo "Running tests..."
	@./gradlew test --info

start: clean ## Starts in development mode
	@echo "Starting project..."
ifndef REFLEX
	@echo "Reflex is not availeble no auto-reload"
	@echo "Reflex is: go get github.com/cespare/reflex"
	@./gradlew bootRun
else
	@echo "reflex is available restarting on changes"
#@reflex -v -d fancy -g '*.java' -g '*.groovy' -r '\.java$$' -r '\.groovy$$' -s -- sh -c 'make bootRun'
	./gradlew bootRun
endif

bootRun: ## Start boot run - called from start
	./gradlew bootRun

buildcontainer: ## Builds docker container
	@docker build          \
	--build-arg version=${DOCKER_TAG}       \
	-t oth/xdsgenerator:${DOCKER_TAG} .

docker-run: buildcontainer ## Runs application in container
	docker run --rm -d --name xdsgenerator --network openteledev -p 9010:9010 -v $$(pwd)/docker/conf/application.yaml:/app/application.yaml:ro \
    -v $$(pwd)/src/main/resources/VOCES_gyldig_2022.p12:/app/VOCES_gyldig_2022.p12:ro \
	kvalitetsit/kih-xds-generator:${DOCKER_TAG}

docker-stop: ## Stop running container
	@docker stop kih-xds-generator

docker-enter: ## Enter container
	@docker exec -it kih-xds-generator bash

docker-app-logs: ## Application logs from docker container
	@docker exec -it kih-xds-generator tail -F /var/log/kih-xds-generator/stdout/current /var/log/kih-xds-generator/tomcat/1

## #################################################################
## Generate help
## #################################################################
# Handles nice readme in shell and documentationn
S ?=
ifndef S
format:= "\033[36m%-20s\033[0m %s\n"
else
format:= "%-20s %s\n"
endif

help: ## This help
	@echo
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf ${format}, $$1, $$2}'

.PHONY: clean setup format compile test start set-version tag release dockerize help bootRun buildcontainer ecr-login tag-container push-to-ecr testtarget
