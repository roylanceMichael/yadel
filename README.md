## yadel

Welcome to the readme/faq for yadel.


***

## What is this?

This is **Y**et **A**nother **D**ag **E**xecution **L**ibrary

This is a library that uses Akka's actor cluster system behind the scenes to distribute work. 
 

***


## Why was this being made?

Think of Airbnb's Airflow, but on Java, with each task being defined with custom executables. 

Workers can be executed anywhere, which makes scaling much easier. 
 

***


## How do I get it?


In gradle, reference the following URL for your repository:
```groovy
repositories {
    mavenCentral()
    maven { url 'https://bintray.com/roylancemichael/maven' }
}

dependencies {
    compile(group: 'org.roylance.yadel', name: 'core', version: '0.188')
}
```

Maven:
```xml
<repositories>
    <repository>
        <snapshots />
        <id>roylanceBintray</id>
        <name>roylanceBintray</name>
        <url>https://bintray.com/roylancemichael/maven</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.roylance.yadel</groupId>
        <artifactId>core</artifactId>
        <version>0.188</version>
    </dependency>
</dependencies>
```

Next, look at the cli project for actor server implementation. Look at the sapi project for calling actors from the rest server.

***

### What are the different projects? Which should I use?

I also developed another framework called yaclib (yet another cross language interface builder) that I use in yaorm as well. It contains all the protobuf models in the api project (as well as any logic needed). It will then programmatically build out an n-tier solution based on the model and controller definitions. This will build Java, CSharp, Python, and TypScript models and communication implementations with the rest endpoint (currently all it does is report meta information about the service). Here is a description of the projections:

api - the core model definition of yadel. This is just the model definition, if you wanted to communicate with the rest server from Android or iOS, you can reference these (and capi).

plugin - the yadel plugin is a bunch of gradle scripts I use to package up a yadel app easier into a debian file.

sapi - server api, this is the sample rest endpoint project created. 

cli - this is a simple application implementing a sample dag. 

yaorm - this has dag store implementations with the yaorm data structures (instead of memory or file system).

***