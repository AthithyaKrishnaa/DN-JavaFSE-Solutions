# ANGULAR HANDS-ON SUMMARY

# Student Course Portal – Hands-On Documentation

## Hands-On 1: Project Setup & Basic Components
- Created a new Angular project named **Student Course Portal**.
- Generated Header and Home components.
- Configured the App component.
- Displayed the portal title, navigation menu, and dashboard statistics.
- Successfully ran the project using `ng serve`.

---

## Hands-On 2: Component Communication
- Created a reusable **Course Card** component.
- Passed course data from the parent component to the child component using `@Input()`.
- Displayed course name, code, credits, and an Enroll button.
- Used `*ngFor` to display multiple course cards dynamically.

---

## Hands-On 3: Directives & Pipes
- Used **Structural Directives** (`*ngIf`) to display different course statuses.
- Used **Attribute Directives** (`ngStyle`, `ngClass`) for dynamic colors and card styling.
- Applied Angular Pipes:
  - `uppercase`
  - `currency`
  - `date`
- Styled course cards based on Passed, Pending, and Failed status.

---

## Hands-On 4: Template-Driven Forms & Validation
- Created a Student Enrollment Form.
- Used `ngModel` for two-way data binding.
- Added validations:
  - Required
  - Min Length
  - Email
- Displayed validation error messages.
- Disabled the Submit button for invalid forms.
- Added Submit and Reset functionality.
- Displayed a success message after successful form submission.

---

## Hands-On 5: Reactive Forms
  - ReactiveFormsModule
  - FormBuilder & FormGroup
  - FormControl & FormArray
  - Built-in Validators
  - Custom Synchronous Validator
  - Custom Asynchronous Validator
  - Dynamic Form Controls

---

## Hands-On 6: Services & Dependency Injection
- Created **CourseService** to store and manage course data.
- Moved hardcoded course data into the service.
- Injected CourseService into components using Dependency Injection.
- Displayed live course count from the service.
- Created **EnrollmentService** for managing enrolled courses.
- Implemented Enroll/Unenroll functionality.
- Shared data between components using Singleton Services (`providedIn: 'root'`).
- Displayed enrolled courses in the Student Profile page.
- Demonstrated Component-Level Dependency Injection using NotificationService.

---

## Concepts Learned
- Angular Components
- Data Binding
- Component Communication
- Directives
- Pipes
- Template-Driven Forms
- Form Validation
- Angular Routing
- Angular Services
- Dependency Injection
- Singleton Services
- Hierarchical Dependency Injection