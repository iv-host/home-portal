# home-portal
A portal to your self-hosted applications. Simply put, it's an application of links.

## demo
<sup>Required: `docker`</sup>

Build the application JAR and local Docker image from the repository root:

```bash
./gradlew.bat :backend:dockerJar
docker build --file scripts/docker/Dockerfile --tag home-portal:local .
```

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
