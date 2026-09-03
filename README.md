# SlotBot project template

## Checking Java coding style

With JDK 25 selected, run `./gradlew.bat checkstyleMain checkstyleTest` on Windows
to check production and test sources. These checks also run as part of
`./gradlew.bat check`. Both errors and warnings fail the check.

The rules in `config/checkstyle/checkstyle.xml` and test Javadoc exceptions in
`config/checkstyle/suppressions.xml` come from the
[SE-EDU AddressBook Level 3 configuration](https://github.com/se-edu/addressbook-level3/tree/master/config/checkstyle).
Checkstyle catches automated style violations; continue following the project's
coding standard for requirements that need human judgment.

After a run, open `build/reports/checkstyle/main.html` or
`build/reports/checkstyle/test.html` to inspect violations. Fix the reported code
and rerun the command. See the
[SE-EDU tutorial](https://se-education.org/guides/tutorials/checkstyle.html)
for optional IntelliJ integration; use Checkstyle 11.0.0 and this project's config.

This is a project template for a greenfield Java project called _SlotBot_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/SlotBot.java` file, right-click it, and choose `Run SlotBot.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see the SlotBot greeting as the output:
   ```
   Hello! I'm SlotBot.
   Let's keep your time and tasks in order.
   ____________________________________________________________
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
