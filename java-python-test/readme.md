- The hierarchy directory contains a main.py file that can be executed to test python access
of the JVM

- *performance_java_loop.py* tests the performance of adding 150,000 items to a Java stack from Python
- *performance_python_loop.py* tests the performance of calling the Java stack 150,000 times to add items from python
- *stack_main.py* tests the performance of python stack implementation with lists

NB: The Java project should be running on the default Py4J port of 25333 to successfully connect to the JVM