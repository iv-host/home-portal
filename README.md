# home-portal
An application that simply serves links.

I personally use this application as a portal to my home network. It links to all of my self-hosted applications.

### Build
<sup>Required: `Node 18` `Java 17`</sup>

This builds the application directly on your machine. The builds output is in `${projectDir}/backend/build/libs/`

```
npm run build
```

### Build-In-Docker
<sup>Required: `npm` `Docker`</sup>

This containerizes the build and its dependencies. The output location is the same.

```
npm run build-in-docker
```

### Build-Image
<sup>Required: `npm` `Docker`</sup>

Runs the build script and constructs the docker image for this application.

```
npm run build-image
```

### Dev Environment
<sup>Required: `npm` `Docker-Compose`</sup>

The dev environment sets up the dependencies needed run the application locally while developing. One top of setting up the database and running scripts, it also sets up a reverse proxy. The project has the front-end and back-end as different modules. Testing it through the proxy allows you to work on the UI and back-end without worrying about issues related to CORS.

Start
```
npm run dev:up
```

Stop
```
npm run dev:down
```
