# home-portal
A portal to your self-hosted applications. Simply put, it's an application of links.

## build
<sup>Required: `docker`</sup>

The application JAR can be built either locally (requires JDK 21 and Node 18) or inside a
throwaway Docker container (requires only `docker`).

### Option A: Build locally

Requires a local JDK 21 and Node 18 installation.

```bash
./gradlew :backend:dockerJar
```

On Windows, use `gradlew.bat` instead of `gradlew`.

### Option B: Build in Docker

No local JDK/Node installation required. This builds a throwaway image containing JDK 21 and
Node 18, then runs the Gradle build inside a container with the repository mounted as a volume.

```bash
docker build --tag home-portal-build --file scripts/build/Dockerfile scripts/build
docker volume create home-portal-gradle-cache
docker run --rm \
 --volume "${PWD}:/workspace" \
 --volume home-portal-gradle-cache:/root/.gradle \
 --workdir /workspace \
 home-portal-build \
 sh ./gradlew :backend:dockerJar --no-daemon
```

### Build the Docker image

Once the JAR has been built (via either option above), build the application's Docker image:

```bash
docker build --file scripts/docker/Dockerfile --tag home-portal:local .
```

## demo

Run the local image:

```bash
docker run \
 --rm \
 --detach \
 --env DATABASE_URL=jdbc:h2:file:./h2/home-portal \
 --env DATABASE_USERNAME=root \
 --env DATABASE_PASSWORD=password \
 --publish 8080:8080 \
 --name home-portal-demo \
 home-portal:local
```
