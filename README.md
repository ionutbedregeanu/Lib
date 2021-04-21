# Lib

**Architecture**: MVVM

**Libraries**: 

1.  Hilt -> dependency injection
2.  Retrofit -> Networking
3.  Room -> Persistency
4.  Mockk -> Testing
5.  Espresso -> Instrumented Testing

**Packages**:

1. Di 

   Contains Hilt database, repository and service modules

2. Remote 

   Contains remote models and services

3. Cache

   Contains a local database model and its entities

4. Data 

   Contains:
    - mappers used to map cache, remote models to data models

    - repository used to get the data from cache or remote and send it forward to presentation layer

5. Presentation

   Contains:
   - mappers to map data models to ui models

   - view model which binds data layer with UI layer

6. UI

   Contains the main activity, custom recycler view adapter, custom views, decorators and ui models

   **Improvemets:**

   - Test coverage

   - Implement a worker manager to refresh offline the data

   - Replace project packages with modules to constraint how dependency is used. For example to not add classes from data layer as dependencies in the ui layer
   

