# Diabetes Discovery V2.2

Start from 08/01/2019.

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
