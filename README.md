# webservice
**Assignment #1**

Description-
Create a web application using a technology stack that meets Cloud-Native Web Application Requirements. Start implementing APIs for the web application. Features of the web application will be split among various applications. For this assignment, we will focus on the backend API (no UI) service. Additional features of the web application will be implemented in future assignments. We will also build the infrastructure on the cloud to host the application. This assignment will focus on the user management aspect of the application. You will implement RESTful APIs based on user stories you will find below.



**Spring Boot Application**
-Creating a simple Restful API and writing a JUnit test case to check the HTTP status code=200


**Prerequisite and Technology-** 
- JAVA
- Maven
- Spring boot setup
- Git/Git Desktop and GitHub
- Postman
- Eclipse IDE.


**Build**
- Maven Clean and Install
- Right click project folder -> Run as -> Maven build -> add configuration-> spring-boot:run


**Run Project**
- Right click project folder -> Run as -> Maven build configuration-> spring-boot:run
- To **debug**-> right click project -> debug as Spring boot app

**Output**
- Once the application is started you can access http://localhost:8080/healthz through postman or web browser

**Screenshot**-
![Screenshot (47)](https://user-images.githubusercontent.com/90646523/153288772-eec38734-bd45-479a-bc3f-fd5383faf941.png)

To **view** the users using the basic authentication(using GET request) :
![image](https://user-images.githubusercontent.com/90646523/154387321-04cd296a-67ad-4d6d-867b-01a3117d8d8b.png)

To **add** new user we do not need any authentication and can be add as follows(using POST request):
![image](https://user-images.githubusercontent.com/90646523/154387558-433f2d8b-80b4-447c-889f-477cb703f7a7.png)

To **update** any user we need basic authentication as follows(using PUT request): 
![image](https://user-images.githubusercontent.com/90646523/154387774-b56308bb-db33-4458-a030-3bab81493013.png)
204 http status denoted- user updated successfully

