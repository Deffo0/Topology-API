# Topology-API
## Table of Contents
- [Overview](#Overview)
- [Setup](#Setup)
- [Design Decisions](#Design-Decisions)
- [Main Modules](#Main-Modules)
- [UML Class Diagram](#UML-Class-Diagram)
- [Test](#Test)
---
## Overview
+ API includes mathods to:
  - parsing a topology JSON file and save it as a topology object.
  - write a topology from the resident set of topologies into a JSON file.
  - query on the resident set of topologies.
  - get the devices in a specific topology.
  - get the devices which are in a specific topology and connected to a specific netlistNode.
+ `Hint` A topology is a set of electronic components that are connected together.
+ Example JSON file (topology.json):
```ruby
{
  "id": "top1",
  "components": [
    {
      "type": "resistor",
      "id": "res1",
      "resistance": {
        "default": 100,
        "min": 10,
        "max": 1000
      },
      "netlist": {
        "t1": "vdd",
        "t2": "n1"
      }
    },
    {
      "type": "nmos",
      "id": "m1",
      "m(l)": {
        "default": 1.5,
        "min": 1,
        "max": 2
      },
      "netlist": {
        "drain": "n1",
        "gate": "vin",
        "source": "vss"
      }
    }
  ]
}
```
---
## Setup
+ Download the whole repo and make sure that dependencies are the same `recommended`.
  - jackson dependency:
    ```ruby
    <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.13.2.1</version>
    </dependency>
    ```
  - junit dependency:
    ```ruby
    <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.1</version>
            <scope>test</scope>
    </dependency>
    ```
+ Make sure that you put them properly in the pom.xml file
---
## Design Decisions
+ Choose java for this task as it provides an excellent environment for applying OOP concepts on it. 
In addition to, it supports a package called `Jackson` which has a great methods and classes to deal with JSON files
+ Assume that the netlist for nmos type must have drain, gate and source.
+ Assume that the netlist for resistor type must have t1 and t2.
+ Let all devices implement `Device` interface to keep the consistency of the devices and make it flexible for any future additions
+ Change the data structure of the JSON Object from `HashMap` to `LinkedHashMap` to preserve the order of the entries in the output JSON file
---
## Main Modules
+ Topology:
  - id.
  - components (Devices).
+ Device:
  - type.
  - id.
  - specifications (resistance or m(l)).
  - netlist.
+ Resistor implements Device.
+ Nmos implements Device.
+ Specifications:
  - default.
  - min.
  - max.
---
## UML Class Diagram
![image](https://raw.githubusercontent.com/Deffo0/Topology-API/main/UML%20Diagram.jpg)

---
## Test
+ Was done using `Junit` package in java.
