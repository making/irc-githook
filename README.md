## Build

    $ mvn package

## Run in dev mode

    $ mvn spring-boot:run

## Run in prod mode

    $ java -jar target/*.jar --server.port=9999 --irc.bot.serverHostname=foobar --irc.bot.joinChannel=#hoge