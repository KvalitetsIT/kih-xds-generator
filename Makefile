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

bootRun: ### Start boot run - called from start
	./gradlew bootRun

tag: ## Set version
	@echo "tagging repo... "
ifndef TAG
	$(error "TAG is not - please set it")
else
	@echo "Version is: $$TAG"
	@echo "Tagging version..."
	git tag -a $$TAG -m "Tagging $$TAG" -f
	@git push --tags
endif

buildcontainer: ### Builds docker container
	@docker build          \
	--build-arg version=${DOCKER_TAG}       \
	-t oth/xdsgenerator:${DOCKER_TAG} .

ecr-login: ### Performs ECR login
	aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 401334847138.dkr.ecr.eu-west-1.amazonaws.com

tag-container: ### tags docker image
	@echo "tagging - ${DOCKER_TAG}"
	docker tag oth/xdsgenerator:${DOCKER_TAG} 401334847138.dkr.ecr.eu-west-1.amazonaws.com/oth/xdsgenerator:${DOCKER_TAG}
	@echo "Done tagging"

docker-run: buildcontainer ### Runs application in container
	docker run --rm --name xdsgenerator --network openteledev -p 9010:9010 -v $$(pwd)/docker/conf/application.yaml:/app/application.yaml:ro \
    -v $$(pwd)/src/main/resources/VOCES_gyldig_2022.p12:/app/VOCES_gyldig_2022.p12:ro \
	oth/xdsgenerator:${DOCKER_TAG}

docker-stop: ### Stop running container
	@docker stop xdsgenerator

docker-enter: ### Enter container
	@docker exec -it xdsgenerator bash

docker-app-logs: ### Application logs from docker container
	@docker exec -it xdsgenerator tail -F /var/log/xdsgenerator/stdout/current /var/log/xdsgenerator/tomcat/1

push-to-ecr: ### Push container to ecr
	@echo "Pushing - ${DOCKER_TAG}"
	@docker push 401334847138.dkr.ecr.eu-west-1.amazonaws.com/oth/xdsgenerator:${DOCKER_TAG}

release: clean setup ecr-login buildcontainer tag-container push-to-ecr ## Release component
	@echo "Built docker container and pushed to AWS... $(TAG)"
	@if [ ! -d release ]; then mkdir release; fi
ifeq ($(TAG), master)
	git archive --format zip --output release/xdsgenerator.zip origin/master
else
	git archive --format zip --output release/xdsgenerator.zip $(TAG)
endif

dockerize: ## Dockerize component
	@echo "Docker image already build and pushed as part of release target"

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
	@printf '=%.0s' {1..80}
	@echo -e "\nStandard OTH targets:"
	@printf '=%.0s' {1..80}
	@echo
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) |grep -v "###"| sort | awk 'BEGIN {FS = ":.*?## "}; {printf ${format}, $$1, $$2}'
	@echo
	@printf '=%.0s' {1..80}
	@echo -e "\nExtra targets:"
	@printf '=%.0s' {1..80}
	@echo
	@grep -E '^[a-zA-Z_-]+:.*?### .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?### "}; {printf ${format}, $$1, $$2}'

.PHONY: clean setup format compile test start set-version tag release dockerize help bootRun buildcontainer ecr-login tag-container push-to-ecr testtarget
