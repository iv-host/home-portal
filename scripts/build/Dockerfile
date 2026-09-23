# Build environment image: JDK 21 + Node 18
#
# This image does NOT contain the project source. It's meant to be used as a
# throwaway build container with the repository mounted as a volume, e.g.:
#
#   docker build --tag home-portal-build --file scripts/build/Dockerfile scripts/build
#   docker run --rm \
#     --volume "${PWD}:/workspace" \
#     --volume home-portal-gradle-cache:/root/.gradle \
#     --workdir /workspace \
#     home-portal-build \
#     ./gradlew :backend:dockerJar --no-daemon
#
FROM eclipse-temurin:21-jdk-jammy

# Install Node.js 18.x (matches frontend/build.gradle.kts node { version = "18.16.0" })
RUN apt-get update \
    && apt-get install --no-install-recommends --yes curl ca-certificates gnupg \
    && curl --fail --silent --show-error --location https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install --no-install-recommends --yes nodejs \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace
