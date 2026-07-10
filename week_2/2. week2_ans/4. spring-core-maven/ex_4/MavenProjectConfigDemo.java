// Exercise 4 - Creating and Configuring a Maven Project
import java.util.ArrayList;
import java.util.List;

public class MavenProjectConfigDemo {

    static final String POM_XML =
            "<project>\n" +
            "  <groupId>com.library</groupId>\n" +
            "  <artifactId>LibraryManagement</artifactId>\n" +
            "  <version>1.0.0</version>\n" +
            "  <properties>\n" +
            "    <maven.compiler.source>1.8</maven.compiler.source>\n" +
            "    <maven.compiler.target>1.8</maven.compiler.target>\n" +
            "  </properties>\n" +
            "  <dependencies>\n" +
            "    <dependency>\n" +
            "      <groupId>org.springframework</groupId>\n" +
            "      <artifactId>spring-context</artifactId>\n" +
            "      <version>5.3.30</version>\n" +
            "    </dependency>\n" +
            "    <dependency>\n" +
            "      <groupId>org.springframework</groupId>\n" +
            "      <artifactId>spring-aop</artifactId>\n" +
            "      <version>5.3.30</version>\n" +
            "    </dependency>\n" +
            "    <dependency>\n" +
            "      <groupId>org.springframework</groupId>\n" +
            "      <artifactId>spring-webmvc</artifactId>\n" +
            "      <version>5.3.30</version>\n" +
            "    </dependency>\n" +
            "  </dependencies>\n" +
            "  <build>\n" +
            "    <plugins>\n" +
            "      <plugin>\n" +
            "        <groupId>org.apache.maven.plugins</groupId>\n" +
            "        <artifactId>maven-compiler-plugin</artifactId>\n" +
            "        <configuration>\n" +
            "          <source>1.8</source>\n" +
            "          <target>1.8</target>\n" +
            "        </configuration>\n" +
            "      </plugin>\n" +
            "    </plugins>\n" +
            "  </build>\n" +
            "</project>";

    static List<String> extractDependencies(String pomXml) {
        List<String> dependencies = new ArrayList<>();
        for (String line : pomXml.split("\n")) {
            if (line.trim().startsWith("<artifactId>") && line.contains("spring")) {
                dependencies.add(line.trim().replace("<artifactId>", "").replace("</artifactId>", ""));
            }
        }
        return dependencies;
    }

    public static void main(String[] args) {
        System.out.println("pom.xml:\n" + POM_XML);

        System.out.println("\nDependencies found:");
        for (String dependency : extractDependencies(POM_XML)) {
            System.out.println("   " + dependency);
        }

        boolean hasCompilerPlugin = POM_XML.contains("maven-compiler-plugin");
        boolean java8Configured = POM_XML.contains("<source>1.8</source>") && POM_XML.contains("<target>1.8</target>");
        System.out.println("\nmaven-compiler-plugin present: " + hasCompilerPlugin);
        System.out.println("Java 1.8 source/target configured: " + java8Configured);
    }
}
