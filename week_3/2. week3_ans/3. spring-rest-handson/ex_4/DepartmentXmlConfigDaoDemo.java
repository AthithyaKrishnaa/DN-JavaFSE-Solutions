/*
 * Hands on 4 - Spring REST - Create static department list using spring xml configuration
 */
import java.util.ArrayList;
import java.util.List;

public class DepartmentXmlConfigDaoDemo {

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

        @Override
        public String toString() {
            return "Department{deptId=" + deptId + ", deptName=" + deptName + "}";
        }
    }

    static final class DepartmentXmlConfig {
        static List<Department> loadDepartments() {
            List<Department> departments = new ArrayList<>();
            departments.add(new Department("D001", "Engineering"));
            departments.add(new Department("D002", "Human Resources"));
            departments.add(new Department("D003", "Finance"));
            return departments;
        }
    }

    static class DepartmentDao {
        static final List<Department> DEPARTMENT_LIST;

        static {
            DEPARTMENT_LIST = DepartmentXmlConfig.loadDepartments();
        }

        List<Department> getAllDepartments() {
            return DEPARTMENT_LIST;
        }
    }

    private static final Logger LOGGER = new Logger("DepartmentXmlConfigDaoDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        DepartmentDao departmentDao = new DepartmentDao();
        List<Department> departments = departmentDao.getAllDepartments();
        LOGGER.debug("DEPARTMENT_LIST size={}", departments.size());
        for (Department department : departments) {
            LOGGER.info(department.toString());
        }

        LOGGER.info("End");
    }
}
