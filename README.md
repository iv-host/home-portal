# home-portal
A portal to your self-hosted applications. Simply put, it's an application of links.

## demo
<sup>Required: `docker`</sup>

```bash
docker run \
 --rm \
 --detach \
 --env DATABASE_URL=jdbc:h2:file:./h2/home-portal \
 --env DATABASE_USERNAME=root \
 --env DATABASE_PASSWORD=password \
 --publish 8080:8080 \
 registry.ivcode.org/home-portal
```
