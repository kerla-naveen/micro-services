## 01: command to start the rabbitMQ container
```
docker run -d --name my-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:4-management
```

## 02: command to start the mysql container
```docker run -d --name mysql_container_name -e MYSQL_ROOT_PASSWORD=your_password -p 3306:3306 mysql:oraclelinux9
```

## 03: command for accessing the mysql container CLI
```
docker exec -it mysql_container_name mysql -u root -p
```

## 04: command for creating docker images with help of google:jib
```
mvn compile jib:dockerBuild -> creates images on localmachine
mvn compile jib:build -> create images and publishes on docerhub
```