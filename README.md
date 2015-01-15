mg-domain
=========

MMOMG (Massively Multiplayer Online Memory Game ) - domain model

##Installation

Check out `mg-domain`, `mg-web` and `mg-client` in 3 sibling directories.

`cd mg-domain`

`sbt publishLocal`. This will build JVM and JS versions of the "domain model".

`cd mg-web; sbt run` To start the server on port 8080

`cd mg-client; sbt fastOpJS` to build the browser JS download 
