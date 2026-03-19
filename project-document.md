# Project Document

# HomemadeFoodies – Home-Made Food Marketplace Platform

---

## 1. Project Name
**HomeBite – Home-Made Food Delivery Platform**

---

## 2. Main Motto of the Project

### Primary Goal
To create a platform that connects home cooks (vendors) with customers, enabling them to sell and buy home-made food easily, safely, and affordably.

### Core Mission
- Empower small home cooks to earn income
- Provide customers with authentic home-made food
- Simplify delivery using integrated logistics
- Maintain very low platform commission

### Problem the Project Solves

Many talented home cooks:
- Cannot reach customers
- Lack digital platforms
- Cannot manage delivery and payments

Customers:
- Want hygienic home food
- Cannot easily discover home chefs nearby

This platform solves both problems.

---

## 3. Stakeholders
- Vendor (Home Cook)
- Customer (Buyer)
- Delivery Partner (via Shiprocket integration)
- Platform Admin

---

## 4. System Overview

### System Flow
Customer → Orders food  
Platform → Processes payment  
Platform → Notifies vendor  
Delivery partner → Picks and delivers order

### Architecture
Mobile App / Web App
|
API Gateway
|
User Service
Vendor Service
Food Catalog Service
Order Service
Payment Service
Delivery Integration Service


---

## 5. Technology Stack

### Backend
- Spring Boot Microservices
- Spring Cloud Gateway
- Spring Security (JWT)
- MySQL / PostgreSQL
- Redis (optional)

### Frontend
- React (Web App)  
  or
- Flutter / React Native (Mobile App)

### Integrations
- Payment: Razorpay
- Delivery: Shiprocket

Companies like:
- Razorpay
- Shiprocket  
  provide APIs for this.

---

## 6. User Roles

### 1. Customer

Can:
- Register
- Browse food
- Order food
- Pay online
- Track delivery

### 2. Vendor (Home Cook)

Can:
- Register
- Add food items
- Accept orders
- Manage menu
- View earnings

### 3. Admin

Can:
- Approve vendors
- Monitor orders
- Manage platform

---

## 7. Core Features (Minimum Viable Product)

### Vendor
- Register as vendor
- Add food items
- Set price
- Upload food image
- Accept orders

### Customer
- Sign up / Login
- Browse vendors
- View food items
- Add to cart
- Place order
- Pay using Razorpay

### Platform
- Create order
- Notify vendor
- Create delivery request (Shiprocket)
- Track order

---

## 8. Microservices Breakdown

### 1. User Service
Handles login and registration.

**APIs**
- POST /register
- POST /login
- GET /profile

**Tables**
- users
- roles

---

### 2. Vendor Service
Handles home cooks.

**APIs**
- POST /vendor/register
- GET /vendor/menu
- POST /vendor/add-item

**Tables**
- vendors
- vendor_menu

---

### 3. Food Catalog Service
Stores food items.

**Tables**
- food_items
- categories
- images

---

### 4. Order Service
Handles order flow.

**Tables**
- orders
- order_items
- order_status

**Flow**
Customer order → Vendor notification → Delivery request

---

### 5. Payment Service
Integration with Razorpay.

**Steps**
1. Create payment order
2. Customer pays
3. Verify payment
4. Confirm order

---

### 6. Delivery Service
Integration with Shiprocket.

**Steps**
1. Create shipment
2. Assign courier
3. Track delivery

---

## 9. Product Backlog

### Epic 1 – User System
- User registration
- Login system
- JWT authentication

### Epic 2 – Vendor Platform
- Vendor onboarding
- Vendor approval system
- Add menu items

### Epic 3 – Food Marketplace
- Browse food
- Search food
- View vendor profile

### Epic 4 – Order System
- Add to cart
- Place order
- Order history

### Epic 5 – Payment Integration
- Razorpay integration
- Payment confirmation

### Epic 6 – Delivery Integration
- Shiprocket API integration
- Order tracking

---

## 10. Sprint Backlog Plan

Each sprint = **2 weeks**

### Sprint 1 – Project Setup
- Setup GitHub
- Setup microservices architecture
- Setup API gateway
- Setup database

**Goal:**  
System foundation ready.

---

### Sprint 2 – User & Vendor Registration
- Customer signup
- Vendor signup
- Authentication system

**Goal:**  
Users and vendors can join.

---

### Sprint 3 – Food Listing
- Vendor adds food
- Customer sees menu
- Image upload

**Goal:**  
Marketplace visible.

---

### Sprint 4 – Cart & Order
- Add to cart
- Place order
- Create order record

**Goal:**  
Customers can buy food.

---

### Sprint 5 – Payment Integration
- Integrate Razorpay
- Payment verification

**Goal:**  
Real payment flow working.

---

### Sprint 6 – Delivery Integration
- Connect Shiprocket
- Create delivery request
- Track delivery

**Goal:**  
Full order cycle working.

---

## 11. Database Design (Simplified)

### Users
- id
- name
- email
- password
- role

### Vendors
- id
- user_id
- kitchen_name
- location
- rating

### Food Items
- id
- vendor_id
- name
- price
- description

### Orders
- id
- user_id
- vendor_id
- total_amount
- status

---

## 12. Order Flow (Important for Viva)

1. Customer selects food
2. Customer pays
3. Platform confirms payment
4. Vendor receives order
5. Delivery partner assigned
6. Order delivered

---

## 13. Minimum Features to Complete Project

- Authentication
- Vendor menu
- Customer ordering
- Payment
- Delivery integration
- Admin approval

That is enough for a complete working system.
