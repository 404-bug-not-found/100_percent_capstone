# **Invoicify - A capstone project**

##  Team Name : 100 Percent
* Iqbal Jamadar
* Sunita Kumari
* Zxander Rodriguez
* Rohit Ranjan


#**Invoicify is tool for contractors to record and bill companies for services.**

# **Prerequisites**

JDK 11,
Gradle 6.8.3,
IntelliJ IDEA

Plugins: Lombok

Tools: Postman

# **How to setup and run invoicify service in local:**

1. Clone the project repository from github : https://github.com/404-bug-not-found/100_percent_capstone
2. Setup project in IntelliJ IDEA
3. Fetch and pull latest code from branch main.
4. Run all the tests (IT and service).
5. download the postgres image: `docker pull postgres`
6. start the postgres container: `docker run --name postgres -e POSTGRES_PASSWORD=open -p 5432:5432 -d postgres`
7. Create the docker network: `docker network create --driver bridge invoicify-network`
8. Start Database Server on Network: `docker run --name postgres-db-cotainer --network invoicify-network -e POSTGRES_PASSWORD=open -e POSTGRES_DB=postgres -d postgres`
9. Build local docker image of project: `docker build -t invoicify-image .`
10. Start Docker Container based on Image: `docker run --name invoicify_postgres_container --network invoicify-network -e PORT=8080 -e SPRING_PROFILES_ACTIVE=postgres -p 9000:8080 -d invoicify-image:latest`
11. Run the Invoicify service with postgres DB : `http://localhost:9000/`
12. Refer the REST Documentation for end point details. (Rest docs page will get landed when service url is accessed.)


# **Heroku App Url:** `https://invoicify100percent.herokuapp.com/`

# ** Process to create authentication token:
[Note: While running service locally, process remains same, just replace app url with localhost url]

1. Create employee: https://invoicify100percent.herokuapp.com/employee

        {
            "employeeName":"Iqbal",
            "password":"capstone"
        }

2. Create token: https://invoicify100percent.herokuapp.com/authenticate

        {
            "employeeName":"Iqbal",
            "password":"capstone"
        }

3. Add below header information in postman:

         Authorization  :  Bearer <token>

# **End Point Details (REST Documentation):**

Security end point Rest documentation: https://invoicify100percent.herokuapp.com/docs/security.html

Service end point Rest documentation:  https://invoicify100percent.herokuapp.com/docs/index.html

# **TODO List for Next Sprint:**
Issue# 66 - Create separate entity for company contact details

Issue# 70 - SQL Injection and other Security attacks validation implementation