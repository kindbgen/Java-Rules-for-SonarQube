[<img src="assets/KINDBGEN-logo.png" width=50% height=50%>](http://www.kindbgen.com/poland)


![Builds Status](https://github.com/kindbgen/Java-Rules-for-SonarQube/actions/workflows/build.yml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/Cognifide/Java-Rules-for-SonarQube/badge.svg?branch=master)](https://coveralls.io/github/Cognifide/Java-Rules-for-SonarQube?branch=master)
[<img src="https://rules.sonarsource.com/images/logos/SonarLint-black.svg" height="28" alt="Available in SonarLint">](https://www.sonarlint.org/)
[<img src="https://rules.sonarsource.com/images/logos/SonarCloud-black.svg" height="28" alt="Available in SonarCloud">](https://sonarcloud.io)
[<img src="https://rules.sonarsource.com/images/logos/SonarQube-black.svg" height="28" alt="Available in SonarQube">](https://www.sonarqube.org/)
# About Java Rules for SonarQube

![Java Rules for SonarQube](https://raw.githubusercontent.com/kindbgen/Java-Rules-for-SonarQube/master/assets/logo.png)

## Purpose

As we all know, SonarQube is a great tool that helps us increase quality of our codebase. However, it does apply mainly to general Java issues. As we know, we can hurt ourselves much more doing Java. [Adobe Experience Manager](https://experienceleague.adobe.com/en/docs) is a comprehensive content management platform solution for building websites, mobile apps and forms. This tool is intended to find common bugs and bad smells specific for Java development. Documentation of each rule is available from SonarQube interface after plugin installation.

## Prerequisites

Each release has its own prerequisites section, for more information please check [releases page](https://github.com/kindbgen/Java-Rules-for-SonarQube/releases).

## Installation

### Local docker image

Check the plugin locally with the prepared ```docker-compose.yml```. Just run these commands from the repo root to build the latest package and fire up a container.

```
mvn clean package
cd local-docker-image
docker-compose up -d
```

### Update Center

Go to your SonarQube instance administration console and open Update Center. Find Java Rules for SonarQube plugin and click install!

### Manual

1. [Download](https://github.com/kindbgen/Java-Rules-for-SonarQube/releases) `javarules-x.y.jar` or build **Java Rules for SonarQube** plugin.
2. Paste it into `sonarqube/extensions/plugins` directory.
3. Restart SonarQube.
4. Go to rules section and activate Java rules in your profile.

## Usage

SonarLint supports only JAVA rules. Currently SonarLint does not support custom languages.
Please refer to this [article](https://community.sonarsource.com/t/sonarlint-for-custom-languages/69904).

It was tried to add `HTL` rules to `web` language, but [sonar-html-plugin](https://github.com/SonarSource/sonar-html) is final and can not be extended.

### Running analysis

To avoid quality profiles collisions, the additional execution param has been added.

`-Dsonar.html.file.suffixes=.notexistingsuffix`

Running with Maven
```
mvn clean verify sonar:sonar \
    -Dsonar.projectKey={sonar_project_key} \
    -Dsonar.projectName='{sonar_project_name}' \
    -Dsonar.host.url=http://localhost:9000 \
    -Dsonar.token={sonar_project_token} \
    -Dsonar.html.file.suffixes=.notexistingsuffix
```

# Release notes

Release notes for each version can be found in [releases section](https://github.com/kindbgen/Java-Rules-for-SonarQube/releases).

# License

Copyright 2025-2026 KINDBGEN

Licensed under the Apache License, Version 2.0

# Commercial Support

Technical support can be made available if needed. Please [contact us](mailto:kindbgen@gmail.com) for more details.

We can:

* prioritize your feature request,
* tailor the product to your needs,
* provide a training for your engineers,
* support your development teams.
