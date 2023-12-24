
# Project Manager App

The goal of this app is to track employees, projects and tasks. It provides statistics and summary of colleagues working together the longest on a project and the respective duration.

**NOTE**: Being part of Sirma Academy and successfully reaching the final stage for both paths: Java and JavaScript, I challenged myself to build a "Full Stack" project. Some of the requirements are fulfilled on the front-end, some on the back-end and some on both sides.

[Project Manager - Front End Link](https://github.com/Ivan-S-Petkov/Project-Manager-FE)

The project has been deployed:
[Live link](https://project-manager-6ccf3.web.app/)

## Description

### API Reference

#### Employees

| Endpoint| Method | Parameter | Body | Response | Description |
| :------ | :----- | :-------- | :--- |:-------- | :-----------|
| `/api/employees` | `GET`| | | employees | get all employees |
| `/api/employees?name={name}` | `GET`| `name` | | employees | get all employees containing name |
| `/api/employees/{id}` | `GET`| `id` | | employee | get employee by id|
| `/api/employees` | `POST` | |`{name:"", department: ""}` | employee | create new employee |
| `/api/employees/{id}` | `PUT` | `id` |`{name:"", department: ""}` | employee | update employee |
| `/api/employees/{id}` | `DELETE` | `id` | | | delete employee |
| `/api/employees` | `DELETE` |  | | | delete all employees |

#### Projects

| Endpoint| Method | Parameter | Body | Response | Description |
| :------ | :----- | :-------- | :--- |:-------- | :-----------|
| `/api/projects` | `GET`| | | employees | get all projects |
| `/api/projects?name={name}` | `GET`| `name` | | projects | get all projects containing name |
| `/api/projects/{id}` | `GET`| `id` | | project | get project by id|
| `/api/projects` | `POST` | | `{name:"", description: ""}` | project | create new project |
| `/api/projects/{id}` | `PUT` | `id` | `{name:"", description: ""}` | project | update project |
| `/api/projects/{id}` | `DELETE` | `id` | | | delete project |
| `/api/projects` | `DELETE` |  | | | delete all projects |

#### Tasks

| Endpoint| Method | Parameter | Body | Response | Description |
| :------ | :----- | :-------- | :--- |:-------- | :-----------|
| `/api/tasks` | `GET`| | | tasks | get all tasks |
| `/api/tasks?empoyee={employeeId}` | `GET`| `employee` | | tasks | get all tasks for employeeId |
| `/api/tasks?project={projectId}` | `GET`| `project` | | tasks | get all tasks for project |
| `/api/tasks/{id}` | `GET`| `id` | | task | get task by id|
| `/api/tasks` | `POST` | |`{employee:"", project: "", startDate: "", endDate: ""}` | task | create new task |
| `/api/tasks/upload` | `POST` |  |`[{employee:"", project: "", startDate: "", endDate: ""}]` | | create multiple tasks |
| `/api/tasks/{id}` | `DELETE` | `id` | | | delete task |
| `/api/tasks` | `DELETE` |  | | | delete all tasks |

#### Statistics

| Endpoint| Method | Parameter | Body | Response | Description |
| :------ | :----- | :-------- | :--- |:-------- | :-----------|
| `/api/statistics` | `GET`| | |  | get all statistics |

## Architecture

 Employee - The model contains id, name and department. The validation for name and department are implemented as they should be between 2 and 20 characters.

 Project - It contains id, name and description. The name should be between 2 and 20 character, description should be between 10 and 200 symbols.

 Task - This model contains id, employee, project, startDate and dueDate. The employee and project have relations ManyToOne to the representative models. When request for creating new task is received the TaskController performas several validations. First it checks where the provided projectId and employeeId exists in the DB. Client side is responsible for validating any date format and send only dates in format "2023/12/23" or "null" to the server side. The client side also checks if the startDate is before or equal to the endDate, and if those dates are not in the future. Thats why the responsibility of the server is only to check if the date "null" or in format "yyyy/MM/dd". The last validation is if the employee has already been working on this project project for the specified time frame. 

## Tech Stack

**Client:** React, Styled-Components

**Server:** Spring, Postgres

## Implementations

### Additional explanations regarding the business logic 

1. As already mentioned the app is fully built with React on the client side and Spring on the server side. That's the reason why the requirement for reading CSV is not fulfilled at the BE. The FE reads it and sends the data as array of JSON objects to the server side. 

2. The data is stored in postgres DB. Several DTOs were implemented to mask the information from and to the client side.

3. Validation service is responsible for handling the complex verifications and business logic.

3. The app supports endpoint: `api/statistics`. It returns all pairs of colleagues working at the same time on the same project and the duration. In order to acheive it the following algorithm was implemented.
 * Obtain all tasks from the DB sorted first by project ID and then by Start Date and last by End Date. 
 * This generates and array of tasks looking similar to Gantt chart /see image below/
 ![Gantt Chart](https://i.ibb.co/mqY3f6j/Gantt-Chart.png)
 
 * Using a nested "for loop" we check every record with ones below and perform the following two validations: 
   - The current record project ID is equal to the next record project ID
   - The current record End Date is after next record Start Date
  ```Java
  for (int current = 0; i < sortedTasks.size()-1; current++) {
    for (int next = i+1; j < sortedTasks.size() ; next++) {

          // Check if next project_Id is different from the current one - BREAK;

          // Check if current endDate is before next startDate - BREAK;

          // Find the smaller end date between current End Date and next End date in order to calculate the duration.
    }
  }
  ```
* If both conditions are met, concludes that we have a working pair togeher. Now is time to calculate the duration but in order to do that we have to check which end date is earliest.
* Once we got this we can calculate the duration, create a Pair record and push it to List.
* When the itterations finish we send the List to the FE, where they visualize the longest working pair(s) and project(s). More info on the client side implementation could be found here: [Project Manager - Front End Link](https://github.com/Ivan-S-Petkov/Project-Manager-FE)


## Final Words

The project has been deployed - [Live link](https://project-manager-6ccf3.web.app/)

https://firebase.google.com/ - hosting the front end

https://www.docker.com/ - creating and hosting Docker image

https://render.com/ - creating a web service consuming the Docker image

https://neon.tech/ - serverless postgres

In case you opened the live link, I hope you enjoyed the footer :) It represents a simple process flow of: 

### Inspiration &rarr; Learn &rarr; Develop

