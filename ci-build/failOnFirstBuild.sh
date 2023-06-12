#!/bin/sh

echo "${GITHUB_REPOSITORY}"
echo "${DOCKER_SERVICE}"
if [ "${GITHUB_REPOSITORY}" != "KvalitetsIT/kih-xds-generator" ] && [ "${DOCKER_SERVICE}" = "kvalitetsit/kih-xds-generator" ]; then
  echo "Please run setup.sh REPOSITORY_NAME"
  exit 1
fi
