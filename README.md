Githook

## Build

    $ mvn package

## Run in dev mode

    $ mvn spring-boot:run

## Run in prod mode

    $ java -jar target/*.jar --server.port=9999　--irc.bot.name=githook --irc.bot.serverHostname=localhost --irc.bot.joinChannel=#hoge

`http://localhost:9999/webhook` is URL to hook :)