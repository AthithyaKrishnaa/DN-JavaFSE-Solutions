/*
 * Hands on 5 - Spring REST - DepartmentController GetMapping /departments, verify via logs
 */
import java.util.ArrayList;
import java.util.List;

public class DepartmentControllerGetAllDepartmentsDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class Department {
        String deptId;
        String deptName;

        Department(String deptId, String deptName) {
            this.deptId = deptId;
            this.deptName = deptName;
        }

        String toJson() {
            return "{\"deptId\":\"" + deptId + "\",\"deptName\":\"" + deptName + "\"}";
        }
    }

    static class DepartmentDao {
        static final List<Department> DEPARTMENT_LIST = new ArrayList<>();

        static {
            DEPARTMENT_LIST.add(new Department("D001", "Engineering"));
            DEPARTMENT_LIST.add(new Department("D002", "Human Resources"));
            DEPARTMENT_LIST.add(new Department("D003", "Finance"));
        }

        List<Department> getAllDepartments() {
            return DEPARTMENT_LIST;
        }
    }

    static class DepartmentService {
        private final DepartmentDao departmentDao;

        DepartmentService(DepartmentDao departmentDao) { this.departmentDao = departmentDao; }

        List<Department> getAllDepartments() { return departmentDao.getAllDepartments(); }
    }

    static class DepartmentController {
        private final DepartmentService departmentService;
        private final Logger logger;

        DepartmentController(DepartmentService departmentService, Logger logger) {
            this.departmentService = departmentService;
            this.logger = logger;
        }

        List<Department> getAllDepartments() {
            logger.info("GET /departments invoked, annotation=@GetMapping(\"/departments\")");
            List<Department> departments = departmentService.getAllDepartments();
            logger.debug("response status=200, department count={}", departments.size());
            return departments;
        }
    }

    private static final Logger LOGGER = new Logger("DepartmentControllerGetAllDepartmentsDemo");

    private static void simulatePostmanRequest(DepartmentController controller) {
        LOGGER.info("Postman request: GET http://localhost:8080/departments");
        List<Department> departments = controller.getAllDepartments();
        StringBuilder responseBody = new StringBuilder("[");
        for (int i = 0; i < departments.size(); i++) {
            responseBody.append(departments.get(i).toJson());
            if (i != departments.size() - 1) responseBody.append(",");
        }
        responseBody.append("]");
        LOGGER.info("Postman response body: " + responseBody);
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        DepartmentDao departmentDao = new DepartmentDao();
        DepartmentService departmentService = new DepartmentService(departmentDao);
        DepartmentController departmentController = new DepartmentController(departmentService, LOGGER);

        simulatePostmanRequest(departmentController);

        LOGGER.info("Verifying logs: department service and dao calls were logged above via LOGGER.info/debug");

        LOGGER.info("End");
    }
}
