# Skratch

Kotlin Scratch Code

## BDD

Two styles of BDD in Kotlin: strings and functions.

- Strings: passes BDD text as [strings](src/main/kotlin/hm/binkley/skratch/bdd/strings)
- Functions: passes BDD text as [function names](src/main/kotlin/hm/binkley/skratch/bdd/funcs)

## Building

Gradle was tried out, and found lacking (and painful).  Returned to dull yet
reliable Maven.

### Gotchas

* MacOS maven/java runs out of memory:

    ```
    [DEBUG] Forking command line: /bin/sh -c cd /Users/boxley/src/kt/skratch && /Library/Java/JavaVirtualMachines/jdk1.8.0_152.jdk/Contents/Home/jre/bin/java -javaagent:/Users/boxley/.m2/repository/org/jacoco/org.jacoco.agent/0.7.10-SNAPSHOT/org.jacoco.agent-0.7.10-SNAPSHOT-runtime.jar=destfile=/Users/boxley/src/kt/skratch/target/jacoco.exec -jar /Users/boxley/src/kt/skratch/target/surefire/surefirebooter8470530871121900032.jar /Users/boxley/src/kt/skratch/target/surefire 2017-12-12T10-08-56_785-jvmRun1 surefire4558125765286735042tmp surefire_05376049204718236822tmp
    [INFO] Tests run: 36, Failures: 0, Errors: 0, Skipped: 0
    [INFO] 
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD FAILURE
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 17.267 s
    [INFO] Finished at: 2017-12-12T10:09:03-06:00
    [INFO] Final Memory: 33M/3098M
    [INFO] ------------------------------------------------------------------------
    [ERROR] Java heap space -> [Help 1]
    java.lang.OutOfMemoryError: Java heap space
        at java.util.Arrays.copyOf (Arrays.java:3332)
        at java.lang.AbstractStringBuilder.ensureCapacityInternal (AbstractStringBuilder.java:124)
        at java.lang.AbstractStringBuilder.append (AbstractStringBuilder.java:448)
        at java.lang.StringBuilder.append (StringBuilder.java:136)
        at org.apache.maven.plugin.surefire.booterclient.output.MultipleFailureException.getLocalizedMessage (MultipleFailureException.java:52)
        at org.apache.maven.plugin.surefire.booterclient.ForkStarter$CloseableCloser.run (ForkStarter.java:200)
        at org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.CommandLineUtils$1.call (CommandLineUtils.java:282)
        at org.apache.maven.plugin.surefire.booterclient.ForkStarter.fork (ForkStarter.java:626)
        at org.apache.maven.plugin.surefire.booterclient.ForkStarter.fork (ForkStarter.java:533)
        at org.apache.maven.plugin.surefire.booterclient.ForkStarter.run (ForkStarter.java:279)
        at org.apache.maven.plugin.surefire.booterclient.ForkStarter.run (ForkStarter.java:243)
        at org.apache.maven.plugin.surefire.AbstractSurefireMojo.executeProvider (AbstractSurefireMojo.java:1077)
        at org.apache.maven.plugin.surefire.AbstractSurefireMojo.executeAfterPreconditionsChecked (AbstractSurefireMojo.java:907)
        at org.apache.maven.plugin.surefire.AbstractSurefireMojo.execute (AbstractSurefireMojo.java:785)
        at org.apache.maven.plugin.DefaultBuildPluginManager.executeMojo (DefaultBuildPluginManager.java:134)
        at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:208)
        at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:154)
        at org.apache.maven.lifecycle.internal.MojoExecutor.execute (MojoExecutor.java:146)
        at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:117)
        at org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject (LifecycleModuleBuilder.java:81)
        at org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder.build (SingleThreadedBuilder.java:51)
        at org.apache.maven.lifecycle.internal.LifecycleStarter.execute (LifecycleStarter.java:128)
        at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:309)
        at org.apache.maven.DefaultMaven.doExecute (DefaultMaven.java:194)
        at org.apache.maven.DefaultMaven.execute (DefaultMaven.java:107)
        at org.apache.maven.cli.MavenCli.execute (MavenCli.java:955)
        at org.apache.maven.cli.MavenCli.doMain (MavenCli.java:290)
        at org.apache.maven.cli.MavenCli.main (MavenCli.java:194)
        at sun.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke (Method.java:498)
    [ERROR] 
    [ERROR] 
    [ERROR] For more information about the errors and possible solutions, please read the following articles:
    [ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/OutOfMemoryError
    (Exit 1) ./mvnw -X test
    ```
