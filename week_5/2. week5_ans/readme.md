# Student Course Portal – Angular Hands-On Project

## Overview

The **Student Course Portal** is an Angular application developed as part of a hands-on learning series. The project demonstrates Angular fundamentals as well as intermediate and advanced concepts including components, forms, routing, dependency injection, HTTP communication, RxJS, route guards, lazy loading, and HTTP interceptors.

---

# Angular Hands-On Summary

## Hands-On 1: Project Setup & Basic Components
- Created a new Angular project named **Student Course Portal**.
- Generated Header and Home components.
- Configured the root App component.
- Displayed the portal title, navigation menu, and dashboard statistics.
- Successfully ran the application using `ng serve`.

---

## Hands-On 2: Component Communication
- Created a reusable **Course Card** component.
- Passed course data from parent to child using `@Input()`.
- Displayed course name, code, credits, status, and an Enroll button.
- Used `*ngFor` to dynamically display multiple course cards.

---

## Hands-On 3: Directives & Pipes
- Used Structural Directives (`*ngIf`) for conditional rendering.
- Applied Attribute Directives (`ngStyle`, `ngClass`) for dynamic styling.
- Used Angular Pipes:
  - Uppercase
  - Currency
  - Date
- Styled course cards based on course status.

---

## Hands-On 4: Template-Driven Forms & Validation
- Built a Student Enrollment Form.
- Implemented two-way data binding using `ngModel`.
- Added form validations:
  - Required
  - Minimum Length
  - Email
- Displayed validation messages.
- Disabled Submit button for invalid forms.
- Implemented Submit and Reset functionality.
- Displayed success messages after successful submission.

---

## Hands-On 5: Reactive Forms
- Configured `ReactiveFormsModule`.
- Built forms using `FormBuilder` and `FormGroup`.
- Used `FormControl` and `FormArray`.
- Applied built-in Angular validators.
- Implemented custom synchronous validators.
- Implemented custom asynchronous validators.
- Added dynamic form controls for multiple course entries.

---

## Hands-On 6: Services & Dependency Injection
- Created `CourseService` to manage course data.
- Replaced hardcoded course data with service-based data.
- Injected services using Angular Dependency Injection.
- Displayed live course count using shared services.
- Created `EnrollmentService`.
- Implemented Enroll / Unenroll functionality.
- Shared data across components using Singleton Services (`providedIn: 'root'`).
- Displayed enrolled courses in the Student Profile page.
- Demonstrated Component-Level Dependency Injection using `NotificationService`.

---

## Hands-On 7: Angular Routing
- Configured application routes.
- Implemented nested routing.
- Added dynamic route parameters.
- Implemented query parameters for course search.
- Created Course Detail and 404 (Not Found) pages.
- Implemented lazy loading for the Enrollment feature module.
- Protected routes using `CanActivate` Guard.
- Prevented navigation with unsaved form data using `CanDeactivate` Guard.

---

## Hands-On 8: HTTP Client & API Integration
- Configured Angular `HttpClient`.
- Connected the application to a JSON Server backend.
- Implemented CRUD operations:
  - GET
  - POST
  - PUT
  - DELETE
- Used RxJS operators:
  - `map`
  - `tap`
  - `catchError`
  - `retry`
  - `switchMap`
- Implemented global error handling.
- Added HTTP Interceptors:
  - Authentication Interceptor
  - Error Handling Interceptor
  - Loading Interceptor
- Displayed a global loading indicator using `BehaviorSubject`.

---

# Technologies Used

- Angular
- TypeScript
- HTML5
- CSS3
- RxJS
- Angular Router
- Angular Forms
- Angular HttpClient
- JSON Server

---

# Angular Concepts Learned

- Angular Components
- Data Binding
- Parent–Child Component Communication
- Structural & Attribute Directives
- Angular Pipes
- Template-Driven Forms
- Reactive Forms
- Form Validation
- Custom Validators
- Dynamic Forms (`FormArray`)
- Angular Routing
- Route Parameters
- Query Parameters
- Nested Routes
- Lazy Loading
- Route Guards (`CanActivate`, `CanDeactivate`)
- Angular Services
- Dependency Injection
- Singleton Services
- Component-Level Providers
- HttpClient
- REST API Integration
- Observables
- RxJS Operators
- HTTP Interceptors
- Global Error Handling

---

# Project Outcome

This project demonstrates the implementation of Angular concepts from beginner to advanced level by building a complete **Student Course Portal** application. It covers component-based architecture, forms, routing, dependency injection, backend communication, state sharing, API integration, and modern Angular development practices.