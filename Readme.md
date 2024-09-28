First I would like to thank, Rajeev Kumar Singh. He created amazing tutorial with SpringBoot and React. I have extended this project and added few more functionaleties to this projects. You just need to setup your own properties in mysql database and download node modules. On this link you can find which all node modules you need to download. ht…

## Building a Full Stack Polls app similar to twitter polls with Spring Boot, Spring Security, JWT, React and Ant Design

### Deploy to AWS services

So that frontend, backend, database will work, I used AWS services.. 

For frontend I used AWS S3 bucket, for backend I used AWS Elastic BeanStalk, for database I used RDS .. 

FRONTEND

So for frontend I need to create new bucket, then enable Static website hosting, then click on edit for Static website hosting ( there I needed to set "index.html" for "Index Document" and also for "Error document"

After that I need to go to "permission" tab ,, needed to "Enable public access (In other words, uncheck "Block all public access")"
After that on the same tab "permission", I needed to change "Bucket policy", something like that
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Statement1",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::reactjs-polls-app-website/*"
        }
    ]
}

After that you can build reactjs project with "npm run build", and then to upload "build" folder you can use drag and drop ,, or launch this command from AWS CLI "  aws s3 sync build/ s3://reactjs-polls-app-website " .. Of course also install AWS CLI if you want to use it

This is also a good website to deploy your "ReactJs" project " https://www.newline.co/fullstack-react/articles/deploying-a-react-app-to-s3/ " 

BACKEND

To deploy backend I needed to create new enviroment.. Application name set what you want , for platform if you develop Java, choose Java, click upload you code, find you local file, ( create local file with mvn clean install ) ..  for service access, create your own user and role and then select this role inside "existing service roles", "EC2 instance profile" , 

how to create this new user and role ? 

Inside AWS "IAM" service, you need to create new user, new role .. 

For user click add, create new user 
Choose attach policies directly
Add this permission
AdministratorAccess
AWSElasticBeanstalkMulticontainerDocker
AWSElasticBeanstalkWebTier
AWSElasticBeanstalkWorkerTier

For roles , click add, create new role

Choose AWS Services, Chose EC2 ,, add the same permission as for user, add role name .. 

Then inside trust relationship, click "Edit trust policy",, 
Click "Add a princial, Principal type "IAM users" ,, connect with previous user .. 
For "{Account}" put, set account, user id
 For "{UserName}" put, set name of role


### Tutorials

I've written a complete tutorial series for this application on The CalliCoder Blog -

+ [Part 1: Bootstrapping the Project and creating the basic domain models and repositories](https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-1/)

+ [Part 2: Configuring Spring Security along with JWT authentication and Building Rest APIs for Login and SignUp](https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-2/)

+ [Part 3: Building Rest APIs for creating Polls, voting for a choice in a Poll, retrieving user profile etc](https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-3/)

+ [Part 4: Building the front-end using React and Ant Design](https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-4/)

## Steps to Setup the Spring Boot Back end app (polling-app-server)

1. **Clone the application**

	```bash
	git clone https://github.com/callicoder/spring-security-react-ant-design-polls-app.git
	cd polling-app-server
	```

2. **Create MySQL database**

	```bash
	create database polling_app
	```

3. **Change MySQL username and password as per your MySQL installation**

	+ open `src/main/resources/application.properties` file.

	+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

4. **Run the app**

	You can run the spring boot app by typing the following command -

	```bash
	mvn spring-boot:run
	```

	The server will start on port 5000. The spring boot app includes the front end build also, so you'll be able to access the complete application on `http://localhost:5000`.

	You can also package the application in the form of a `jar` file and then run it like so -

	```bash
	mvn package
	java -jar target/polls-0.0.1-SNAPSHOT.jar
	```
5. **Add the default Roles**
	
	The spring boot app uses role based authorization powered by spring security. Please execute the following sql queries in the database to insert the `USER` and `ADMIN` roles.

	```sql
	INSERT INTO roles(name) VALUES('ROLE_USER');
	INSERT INTO roles(name) VALUES('ROLE_ADMIN');
	```

	Any new user who signs up to the app is assigned the `ROLE_USER` by default.

## Steps to Setup the React Front end app (polling-app-client)

First go to the `polling-app-client` folder -

```bash
cd polling-app-client
```

Then type the following command to install the dependencies and start the application -

```bash
npm install && npm start
```

The front-end server will start on port `3000`.
