# 🏥 Hospital Cleaning Management System

A web-based Hospital Cleaning Management System designed to efficiently manage sanitation tasks, staff, floors, and hospital areas. The system ensures hygiene compliance through structured checklists, role-based access, and real-time task tracking.

🔗 **Live Demo:**  
https://hospital-cleaning-management-system.onrender.com/login  

### 🔑 Demo Credentials
- **Admin:** admin / admin123  
- **Manager:** manager / manager123  
- **Staff:** staff / staff123  

---

## 📌 Features

### 🔐 Authentication & Authorization
- Secure login system
- Role-based access control (Admin, Manager, Staff)
- Session-based authentication
- Remember Me option

### 📋 Checklist Management
- Create, view, update, and delete cleaning checklists
- Assign checklists to specific staff members
- Track checklist status (Pending / Completed)
- Room-wise and area-wise sanitation records

### 👥 Staff Management
- Add, edit, and remove staff members
- Assign roles (ADMIN, MANAGER, STAFF)
- Activate / deactivate staff accounts
- View staff contact and role details

### 🏢 Floor & Area Management
- Manage hospital floors (First Floor, Ground Floor, Basement)
- Define areas under each floor (OT, IPD Ward, OPD, Pharmacy, etc.)
- Track cleaning coverage per floor and area

### 📊 Dashboard & Monitoring
- Real-time dashboard with key statistics
- Completed and pending task overview
- Staff availability tracking
- Floor-wise cleaning progress visualization
- Recent activity logs

### 📅 Schedule & Task Tracking
- View daily assigned schedules
- Monitor completed and pending tasks
- Centralized monitoring for administrators

---

## 🛠️ Tech Stack

### Backend
- Java
- Spring Boot
- Spring MVC
- Spring Security
- Hibernate / JPA
- RESTful APIs

### Frontend
- HTML
- CSS
- JavaScript
- Bootstrap

### Database
- MySQL

### Tools & Deployment
- Maven
- Git & GitHub
- Render (Cloud Deployment)

---

## 👤 User Roles

| Role    | Permissions |
|--------|-------------|
| Admin  | Full system access, staff & data management |
| Manager | Staff assignment, checklist monitoring |
| Staff  | View & update assigned cleaning tasks |

---

## 🚀 Getting Started (Local Setup)

### Prerequisites
- Java 17+
- Maven
- MySQL
- Git

### Installation & Setup

```bash
# Clone the repository
git clone https://github.com/your-username/hospital-cleaning-management-system.git

# Navigate to the project directory
cd hospital-cleaning-management-system
