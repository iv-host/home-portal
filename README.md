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

## local development with PostgreSQL and Keycloak

Requires Docker Compose. Compose builds and starts the Home Portal app, PostgreSQL,
Liquibase, and Keycloak:

```powershell
docker compose -f scripts/dev/docker-compose.yml up -d --build
docker compose -f scripts/dev/docker-compose.yml ps
```

Liquibase runs once and initializes the Home Portal database before the app starts.
Open Home Portal at <http://localhost:8080> and Keycloak at
<http://localhost:8081>. The app uses Keycloak's internal Docker address to fetch signing keys
and exchange tokens while validating the issuer URL that browsers use.
The imported realm includes these local-only test accounts:
`admin` / `admin` (admin and user roles) and `user` / `user` (user role).
Compose sets `OAUTH2_ADMIN_ROLE=admin`; assign that realm role to users who should
manage links, images, and backgrounds.

Stop the containers with `docker compose -f scripts/dev/docker-compose.yml down`.
To also delete the local PostgreSQL data, use `docker compose -f scripts/dev/docker-compose.yml down -v`.

### Role-based links

Links can be restricted to one or more Keycloak realm roles. Enter role names as a
comma-separated list when creating a link. A link is shown when the current user's access token
contains any matching role in `realm_access.roles`; leave the field blank to show the link to
everyone. The backend filters the links API response using the same role check, so restricted
links are omitted from search results and other clients that use the API.
The realm role required for administrative operations is configured separately through
`OAUTH2_ADMIN_ROLE` (or `security.oauth2.admin-role`); it defaults to `admin`.
Admins can edit existing links from Settings; leaving the icon upload empty keeps the existing
icon.
