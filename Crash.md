FAILURE: Build failed with an exception.
* What went wrong:
Execution failed for task ':compileJava'.
> Could not resolve all files for configuration ':compileClasspath'.
   > Could not resolve net.neoforged:neoforge:26.1.2.0-beta.
     Required by:
         root project :
      > No matching variant of net.neoforged:neoforge:26.1.2.0-beta was found. The consumer was configured to find a library for use during compile-time, compatible with Java 21, preferably in the form of class files, preferably optimized for standard JVMs, and its dependencies declared externally but:
          - Variant 'changelog':
1 actionable task: 1 executed
              - Incompatible because this component declares documentation and the consumer needed a library
              - Other compatible attributes:
                  - Doesn't say anything about how its dependencies are found (required its dependencies declared externally)
                  - Doesn't say anything about its elements (required them preferably in the form of class files)
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
                  - Doesn't say anything about its target Java version (required compatibility with Java 21)
                  - Doesn't say anything about its usage (required compile-time)
          - Variant 'installerJar' declares a library for use during runtime, compatible with Java 8, and its dependencies bundled (fat jar):
              - Other compatible attributes:
                  - Doesn't say anything about its elements (required them preferably in the form of class files)
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
          - Variant 'modDevApiElements' declares a library for use during compile-time, and its dependencies declared externally:
              - Other compatible attributes:
                  - Doesn't say anything about its elements (required them preferably in the form of class files)
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
                  - Doesn't say anything about its target Java version (required compatibility with Java 21)
          - Variant 'modDevBundle' declares a component, and its dependencies declared externally:
              - Incompatible because this component declares a component of category 'data' and the consumer needed a library
              - Other compatible attributes:
                  - Doesn't say anything about its elements (required them preferably in the form of class files)
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
                  - Doesn't say anything about its target Java version (required compatibility with Java 21)
                  - Doesn't say anything about its usage (required compile-time)
          - Variant 'modDevConfig' declares a component, and its dependencies declared externally:
              - Incompatible because this component declares a component of category 'data' and the consumer needed a library
              - Other compatible attributes:
                  - Doesn't say anything about its elements (required them preferably in the form of class files)
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
                  - Doesn't say anything about its target Java version (required compatibility with Java 21)
                  - Doesn't say anything about its usage (required compile-time)
          - Variant 'modDevModulePath' declares a library, and its dependencies declared externally:
              - Other compatible attributes:
                  - Doesn't say anything about its elements (required them preferably in the form of class files)
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
                  - Doesn't say anything about its target Java version (required compatibility with Java 21)
                  - Doesn't say anything about its usage (required compile-time)
          - Variant 'modDevRuntimeElements' declares a library for use during runtime, and its dependencies declared externally:
              - Other compatible attributes:
                  - Doesn't say anything about its elements (required them preferably in the form of class files)
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
                  - Doesn't say anything about its target Java version (required compatibility with Java 21)
          - Variant 'modDevTestFixtures' declares a library for use during runtime, and its dependencies declared externally:
              - Other compatible attributes:
                  - Doesn't say anything about its elements (required them preferably in the form of class files)
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
                  - Doesn't say anything about its target Java version (required compatibility with Java 21)
          - Variant 'sourcesElements' declares a component for use during runtime, and its dependencies declared externally:
              - Incompatible because this component declares documentation and the consumer needed a library
              - Other compatible attributes:
                  - Doesn't say anything about its elements (required them preferably in the form of class files)
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
                  - Doesn't say anything about its target Java version (required compatibility with Java 21)
          - Variant 'universalJar' declares a library for use during runtime, packaged as a jar, and its dependencies declared externally:
              - Incompatible because this component declares a component, compatible with Java 25 and the consumer needed a component, compatible with Java 21
              - Other compatible attribute:
                  - Doesn't say anything about its target Java environment (preferred optimized for standard JVMs)
* Try:
> No matching variant errors are explained in more detail at https://docs.gradle.org/8.11.1/userguide/variant_model.html#sub:variant-no-match.
> Review the variant matching algorithm at https://docs.gradle.org/8.11.1/userguide/variant_attributes.html#sec:abm_algorithm.
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.
> Get more help at https://help.gradle.org.
BUILD FAILED in 17s
Error: Process completed with exit code 1.