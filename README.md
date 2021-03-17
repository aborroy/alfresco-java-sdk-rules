# Alfresco Java Event API - Folder Rules sample

Sample application using the pure Spring Integration approach for the Alfresco Java Event API providing sample implementation for Alfresco Folder Rules.


## Usage

### Pre-Requisites

To properly build and run the project in a local environment it is required to have installed some tools.

* Java 11:
```bash
$ java -version

openjdk version "11.0.1" 2018-10-16
OpenJDK Runtime Environment 18.9 (build 11.0.1+13)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.1+13, mixed mode)
```

* [Maven](https://maven.apache.org/install.html) version 3.3 or higher:
```bash
$ mvn -version

Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-04T21:00:29+02:00)
```

* [Docker](https://docs.docker.com/install/) version 1.12 or higher:
```bash
$ docker -v

Docker version 20.10.2, build 2291f61
```

* [Docker compose](https://docs.docker.com/compose/install/):
```bash
$ docker-compose -v

docker-compose version 1.27.4, build 40524192
```

### Build and run

Start Docker Compose.

```
$ docker-compose up --build --force-recreate
```

Build the project.

```
$ mvn clean package -Dlicense.skip=true
```

Run the application

```
$ java -jar target/alfresco-java-sdk-rules-5.0.0-SNAPSHOT.jar
```

## Folder Rules Coverage
In the library, [folder rules](https://docs.alfresco.com/content-services/latest/using/content/rules/) can be defined in order to manage the content automatically.

There are three parts to a content rule:

* The event that triggers the rule
* The conditions the content has to meet
* The action performed on the content

**Event** (Coverage 100%)

* Items are created or enter this folder: Listening to NODE_CREATED messages for a folder
* Items are updated: Listening to NODE_UPDATE messages for a folder
* Items are deleted or leave this folder: Listening to NODE_DELETED messages and MoveFilter for a folder

**Conditions** (Coverage 100%)

* Conditions on different PropertyFilter, TypeFilter, AspectFilter can be used
* Additional filters may be developed to match some conditions

**Actions** (Coverage 100%, mainly using REST API)

Actions may be a new Alfresco CLI command, that needs to be run once the condition has been met.

* Execute script (this may be replaced by some client logic)
* Copy (REST API)
* Move (REST API)
* Check in (REST API)
* Check out (REST API)
* Link to category (REST API)
* Add aspect (REST API)
* Remove aspect (REST API)
* Add simple workflow (REST API, setting Aspect "app:simpleworkflow")
* Send email (external process)
* Transform and copy content (external process ATS)
* Transform and copy image (external process with ATS)
* Extract common metadata fields (REST API)
* Import (external process)
* Specialise type (REST API)
* Increment Counter (REST API)
* Set property value (REST API)
* Start Process (REST API)

**Additional options** (Coverage 66%)

* Disable rule (custom code in client)
* Rule applies to subfolders (by using a Filter, like `InHierarchyFilter`)
* Run rule in background (always, as it's fired by an ActiveMQ message)

>> Running rules in foreground is unsupported
