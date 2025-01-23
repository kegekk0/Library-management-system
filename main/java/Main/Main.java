/**
 * Main method starting the Library Management Application.
 *
 * <p>
 * This document provides the steps to set up, run, and extend the library
 * management project. It assumes you have received all the necessary files and
 * need to set up the project manually without using Git or command-line tools.
 * </p>
 *
 * <h2>Prerequisites</h2>
 * <ul>
 * <li>Java Development Kit (JDK): Version 8 or higher.</li>
 * <li>Apache Maven: Installed and configured for dependency management.</li>
 * <li>Database Management System (DBMS): Used H2.</li>
 * <li>Integrated Development Environment (IDE): IntelliJ IDEA, Eclipse, or a
 * similar Java IDE.</li>
 * </ul>
 *
 * <h2>Steps to Set Up the Project</h2>
 * <h3>1. Open the Project in an IDE</h3>
 * <ol>
 * <li>Launch your Java IDE.</li>
 * <li>Select <b>Open Project</b> and navigate to the folder containing the
 * project files.</li>
 * <li>Open the project.</li>
 * <li>Allow the IDE to index the project and download dependencies if Maven is
 * used.</li>
 * </ol>
 *
 * <h3>2. Configure the Database Connection</h3>
 * <ol>
 * <li>Open the <code>persistence.xml</code> file located in the
 * <code>src/main/resources/META-INF</code> directory.</li>
 * <li>Modify the following properties to match your database configuration:
 * <pre>{@code
 * <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/library_management"/>
 * <property name="javax.persistence.jdbc.user" value="your_database_username"/>
 * <property name="javax.persistence.jdbc.password" value="your_database_password"/>
 * <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
 * }</pre>
 * </li>
 * <li>Save the changes.</li>
 * <li>Create the required database schema (<code>library_management</code>) in
 * your DBMS, either using a graphical tool like MySQL Workbench or the database
 * administration feature in your IDE.</li>
 * <li>Run the application once to auto-generate the required database tables
 * based on the JPA annotations.</li>
 * </ol>
 *
 * <h3>3. Build the Project</h3>
 * <ol>
 * <li>In the IDE, locate the <b>Build</b> or <b>Maven</b> tab.</li>
 * <li>Perform a <b>Clean and Build</b> action to compile the project and
 * resolve any missing dependencies.</li>
 * </ol>
 *
 * <h3>4. Run the Application</h3>
 * <ol>
 * <li>Locate the main class in the project, such as
 * <code>LoginScreen.java</code>.</li>
 * <li>Right-click the file and select <b>Run</b> (or use the corresponding
 * option in your IDE).</li>
 * </ol>
 *
 * <h3>5. Verify Functionality</h3>
 * <ol>
 * <li>Use the application to ensure it connects to the database and performs as
 * expected.</li>
 * <li>If errors occur, double-check the database credentials, schema, and
 * connection URL in <code>persistence.xml</code>.</li>
 * </ol>
 *
 * <h2>Extending the Project</h2>
 * <h3>Adding New Features</h3>
 * <ul>
 * <li>To add new entities, create new classes and annotate them with JPA
 * annotations (<code>@Entity</code>, <code>@Table</code>, etc.).</li>
 * <li>For new business logic, add appropriate methods to the service or
 * controller classes.</li>
 * <li>Update the GUI files to include new forms or features for user
 * interaction.</li>
 * </ul>
 *
 * <h3>Modifying the Database Schema</h3>
 * <ol>
 * <li>Modify existing entity classes or add new ones.</li>
 * <li>Ensure that relationships (<code>@OneToMany</code>,
 * <code>@ManyToOne</code>, etc.) are correctly defined.</li>
 * <li>Update the database schema by running the application after making
 * changes.</li>
 * </ol>
 *
 * <h3>Documentation Updates</h3>
 * <ul>
 * <li>Use JavaDocs to describe new classes, methods, and features.</li>
 * <li>Regenerate the JavaDocs to reflect the updates (instructions below).</li>
 * </ul>
 *
 * <h2>Generating JavaDocs</h2>
 * <h3>Using Your IDE</h3>
 * <h4>IntelliJ IDEA:</h4>
 * <ol>
 * <li>Open the <b>Tools</b> menu and select <b>Generate JavaDoc</b>.</li>
 * <li>In the dialog, select the source files to include.</li>
 * <li>Specify the output directory for the generated JavaDocs.</li>
 * <li>Click <b>OK</b> to generate the JavaDocs as an HTML package.</li>
 * </ol>
 * <h4>Eclipse:</h4>
 * <ol>
 * <li>Right-click the project in the <b>Package Explorer</b>.</li>
 * <li>Select <b>Export &gt; JavaDoc</b>.</li>
 * <li>Specify the source files and the output directory.</li>
 * <li>Click <b>Finish</b> to generate the JavaDocs.</li>
 * </ol>
 * <h3>Accessing the JavaDocs</h3>
 * <ol>
 * <li>Open the output directory specified during generation.</li>
 * <li>Open the <code>index.html</code> file in any web browser to view the
 * documentation.</li>
 * </ol>
 *
 * @author Michał Piekuś
 * @version 1.0
 */

package Main;

import GUI.LoginScreen;
import service.PersistenceManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
        Runtime.getRuntime().addShutdownHook(new Thread(PersistenceManager::closeEntityManagerFactory));
    }
}
