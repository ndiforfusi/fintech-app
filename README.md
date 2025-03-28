# 💳 FinTech Credit Card Expense Tracker

A full-stack Java-based FinTech application to manage and track credit card expenditures. Built with Spring Boot, Thymeleaf, MySQL, Docker, GitHub Actions CI/CD, and deployed to AWS EKS with ALB Ingress and ECR integration.

---

## 🌟 Features

- Credit Card Information Storage (masked card, type, expiry)
- Expense Tracking:
  - Vendor, Amount, Date, Category
  - Linked to specific credit cards
- Monthly Expense View & Aggregation
- Thymeleaf Web UI
- RESTful Backend APIs (extendable)
- Built-in Database Integration with MySQL
- CI/CD Pipeline with:
  - Maven build
  - SonarQube static analysis
  - Docker image build + push to Amazon ECR
  - Kubernetes deployment to Amazon EKS via GitHub Actions
- Exposed using ALB Ingress via `https://dominionsystem.org`

---

## 🚀 Tech Stack

| Layer      | Technology                             |
|------------|-----------------------------------------|
| Backend    | Java 17, Spring Boot, Spring MVC, JPA   |
| Frontend   | Thymeleaf, HTML                         |
| Database   | MySQL (JPA/Hibernate)                   |
| CI/CD      | GitHub Actions, Maven, SonarQube        |
| Container  | Docker                                  |
| Cloud      | AWS ECR + EKS + ALB Ingress             |
| Security   | (Pluggable: Spring Security optional)   |

---

## 📦 Project Structure

. ├── src/ │ ├── main/java/com/fintech/app/ │ ├── resources/templates/ # Thymeleaf HTML │ └── application.yml # Configurations ├── k8s/ # Kubernetes Manifests ├── Dockerfile # App containerization ├── pom.xml # Maven dependencies └── .github/workflows/ci-cd.yml # CI/CD pipeline

---

## ⚙️ How to Run Locally

1. **Start MySQL DB (optional if using local setup)**
2. **Build & Run:**
```bash
mvn clean install
mvn spring-boot:run
Access: http://localhost:8080/expenses

🐳 Docker Build & Run

docker build -t fintech-app .
docker run -p 8080:8080 fintech-app
☁️ AWS CI/CD Pipeline
CI Steps:
Maven Build (mvn package)

SonarQube Scan

Docker Image Push to Amazon ECR

CD Steps:
Kubernetes Deploy to EKS

ALB Ingress to expose public URL

💡 All automated via GitHub Actions (.github/workflows/ci-cd.yml)

🛠 GitHub Secrets Required
Key	Description
SONAR_TOKEN	SonarQube project token
SONAR_HOST_URL	URL of your SonarQube instance
AWS_ACCESS_KEY_ID	AWS credentials for ECR/EKS
AWS_SECRET_ACCESS_KEY	AWS credentials
🌍 Public Access
✅ https://dominionsystem.org
Hosted via AWS ALB Ingress Controller.

📈 Future Improvements (Open for Contribution)
User Login (Spring Security)

Role-based dashboards (Admin vs Card Holder)

Export to PDF/Excel

Pie charts & vendor analytics with Chart.js

RESTful APIs for mobile client integration

👨‍💻 Author
Name: Ndifor Fusi
Role: DevOps Engineer Instructor
GitHub: @ndiforfusi

📄 License
This project is open-source and licensed under the MIT License.

---

Let me know if you'd like a **`LICENSE`** file or **Swagger/OpenAPI docs** section added next! 📜🔥








