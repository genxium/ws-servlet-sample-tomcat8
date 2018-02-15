Project root
==

You're reading `<proj-root>/README.md`.

Building
==

user@proj-root> gradle build [-b build.gradle] --info

Deployment
==

Assuming that a preconfigured Tomcat@8.0.24 container is used, and that a 

```
<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000" />
```

is defined in `${catalina.base}/conf/server.xml` where ["HTTP/1.1" specifies WebSocket support by RFC6455](https://tools.ietf.org/html/rfc6455).

```
user@proj-root> scp ./build/libs/ws-servlet-sample.war /path/to/catalina/base/webapps/
user@${catalina.base}> sh bin/catalina.sh [run | start]
```

Visit `http://${whatever-host-configured}:8080/ws-servlet-sample/echo.html?token=aaaaaaaaa`.
