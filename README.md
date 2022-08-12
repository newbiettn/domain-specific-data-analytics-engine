# Domain-specific Data Analytics Engine

A semantic data analytics engine that can (i) let users query data semantically and visually, and (ii) automatically perform and optimize complex data science tasks, such as data cleaning, building predictive models.

Demo: https://www.youtube.com/watch?v=BqOVRlL1VYo

## Instruction

- *Compile grammars in Jena*
```
cd jena-arq/Grammar
./grammar
```

- *Run Fuseki server*
1. Select Plugins/exec/exec:java
2. Click Run Maven Build (green arrow icon)

- *An ML query example:*
```
PREFIX : <file:///Users/newbiettn/Downloads/d2rq-0.8.1/mapping.nt#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX vocab: <file:///Users/newbiettn/Downloads/d2rq-0.8.1/vocab/>

CREATE PREDICTION MODEL ?m
TARGET ?survive
WHERE {
?pub FEATURE vocab:papers_Publish.
?title FEATURE vocab:papers_Title.
?year FEATURE vocab:papers_Year.
}
```
- *Maven clean install without testing*
```
clean install -Dmaven.test.skip=true
```

- *The project depends on VWorkflows (https://github.com/newbiettn/VWorkflows). Thus to make it possible to add dependency to Maven pom file, it is necessary to publish VWorkflows to local repo. Publish VWorkflows to local maven repo (~/.m2/repository)*
```
Run gradle build in Intellij in Gradle tab.
Run gradle publishToLocalMaven in IntelliJ Gradle tab.
```
