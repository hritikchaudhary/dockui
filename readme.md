# Backend Junior Developer (Intern) Test
Welcome! and thank you for applying!

## Submission
- [x] 🔄 Integrate the 🐳 Docker Java library (https://github.com/docker-java/docker-java/blob/main/docs/getting_started.md) to enable connect and manage 👷‍♀️ workers.
    - Comments: Integrated the latest version 3.3.0
- [x] ➕ Add the required fields and database migration for the 👷‍♂️ Worker entity to keep all the information associated on available at the container level (like 🔌 ports, 👨‍💼 name, 🟢 status, etc..)
    - Comments: Added all the fields I could find from the response of docker-java
- [x] 🆕 Add required entities and tables to track the 👷‍♂️ Worker statistics.
    - Comments: Updated Worker Entity and added DockerPort, DockerNetworkSettings, DockerNetwork DockerMount, DockerHostConfig Entities and their respective repositories as well even though not all repositories are used.
🆙 Update the 👷‍♂️ WorkerController to add actions for:
- [x] 📄 List workers (paginated)
  - It lists workers (paginated) from our database and only lists name and containerId of the worker, I have created a DTO to send that as response
- [x] ▶️ Start and ⏹️ Stop worker
  - It takes containerId as input and also checks if container is already running or already stopped.
- [x] 🔍 Get worker information
  - It takes containerId as input and fetch all the details of that worker from our database, it is mapped to /api/v1/worker/workers/info/{containerId}
- [x] 📊 Get worker statistics
  - It also takes containerId as input and fetch the current statistics from dockerClient and is mapped to /api/v1/worker/workers/stats/{containerId}
I'll try to add as much comments I can to explain my code properly.. Hope you like the submission.
Thanks!! 

## Requirement

Your task is to add the necessary features to the current project's API blueprint structure to enable
it to manage **Docker** containers, also referred to as workers, as fallows:

🔄 Integrate the 🐳 Docker Java library (https://github.com/docker-java/docker-java/blob/main/docs/getting_started.md) to enable connect and manage 👷‍♀️ workers.

➕ Add the required fields and database migration for the 👷‍♂️ Worker entity to keep **all** the information associated on available at the container level (like 🔌 ports, 👨‍💼 name, 🟢 status, etc..)

🆕 Add required entities and tables to track the 👷‍♂️ Worker statistics.

🆙 Update the 👷‍♂️ WorkerController to add actions for:
* 📄 List workers (paginated)
* ▶️ Start and ⏹️ Stop worker
* 🔍 Get worker information
* 📊 Get worker statistics

## Constraints and restrictions

You are free to use any package or library you see feet as long as you follow these rules:

* 👎 You can't copy and paste from other peoples work

## Run

Once you have established a connection to the database, you can test the application by using Swagger.

You can access Swagger by navigating to the following link: http://localhost:8080/swagger-ui/#/.

## Submission

Your solution must be uploaded on GitHub, and submit us the link in **max 1 week** after receiving the task.

## Note

Keep in mind that this is the project that will be used to evaluate your skills.
So we do expect you to make sure that the app is fully functional and doesn't have any obvious missing pieces.