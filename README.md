# java-performance-issues


Java's automatic garbage collection process is a fundamental feature that distinguishes it from its predecessors like C++ and C. The Java Virtual Machine (JVM) uses this process for memory management, so you can focus on developing the core business logic. Despite being an efficient tool, it's not without flaws, leading to significant memory-hogging issues.

# Popular memory leaks in Java

A memory leak occurs when object references that are no longer needed are unnecessarily maintained. These leaks are bad. For one, they put unnecessary pressure on your machine as your programs consume more and more resources.

Think of memory leakage as a disease and the OutOfMemoryError as a symptom. But not all OutOfMemoryErrors imply memory leaks, and not all memory leaks manifest themselves as OutOfMemoryErrors.
few of the the possible error messages:
- java.lang.OutOfMemoryError: Java heap space

- java.lang.OutOfMemoryError: PermGen space

- java.lang.OutOfMemoryError: Requested array size exceeds VM limit

- java.lang.OutOfMemoryError: request <size> bytes for <reason>. Out of swap space?

- java.lang.OutOfMemoryError: <reason> <stack trace> (Native method)

# Diagnosing Leaks
In most cases, diagnosing memory leaks requires very detailed knowledge of the application in question. Warning: the process can be lengthy and iterative.

Our strategy for hunting down memory leaks will be relatively straightforward:

- Identify symptoms

- Enable verbose garbage collection

- Enable profiling

- Analyze the trace

# Do we need to worry about GCs?
Yes, We have to worry about the GC and Its behaviors. Because It can provide a significant performance difference. Each GC has It’s own advantages and disadvantages. As developers, we have to have a clear idea about the behaviors of all garbage collectors and we have to select a garbage collector based on our business scenario. We can choose a garbage collector by, passing the choice as a JVM argument.

# How to Analyze GC Logs
Understanding garbage collection logs is not easy. It requires an understanding of how the Java virtual machine works and the understanding of memory usage of the application.
The garbage collection logs will be able to answer questions like:

- When was the young generation garbage collector used?
- When was the old generation garbage collector used?
- How many garbage collections were run?
- For how long were the garbage collectors running?
- What was the memory utilization before and after garbage collection?

The minor garbage collector will always be triggered when there is not enough memory to allocate a new object on the heap, i.e. when the Eden generation is full or is getting close to being full.
If your application creates new objects very often you can expect the minor garbage collector to run often.

Major garbage collection means that the tenured generation clearing event was performed. The tenured generation is also widely called the old generation space.
# Choosing a garbage collector
Java offers many garbage collectors to meet different application needs. Choosing the right garbage collector for your application majorly impacts its performance. The essential criteria are:

- Throughput: The percentage of total time spent in useful application activity versus memory allocation and garbage collection. For example, if your throughput is 95%, that means the application code is running 95% of the time and garbage collection is running 5% of the time. You want higher throughput for any high-load business application.
- Latency: Application responsiveness, which is affected by garbage collection pauses. In any application interacting with a human or some active process (such as a valve in a factory), you want the lowest possible latency.
- Footprint: The working set of a process, measured in pages and cache lines.

![img_5.png](img_5.png)

# GC Algorithm JVM argument

There are 7 different GC algorithms in OpenJDK:

| GC Algorithm  | VM Args |
| -------- | ------- |
| Serial GC  | 	-XX:+UseSerialGC    |
| Parallel GC |	-XX:+UseParallelGC    |
| Concurrent Mark & Sweep (CMS) GC  | -XX:+UseConcMarkSweepGC  |
| G1 GC    | -XX:+UseG1GC    |
| Shenandoah GC   | -XX:+UseShenandoahGC    |
| Z GC  | -XX:+UseZGC    |
| Epsilon GC   | -XX:+UseEpsilonGC   |



#### Serial Garbage Collector
   This is the simplest GC implementation. It basically designed for a single thread environment. This GC implementation freezes all application threads when it runs. It uses single thread for garbage collection. Hence, it is not a good idea to use it in multi-threaded applications like server environments.

#### Parallel Garbage Collector
The parallel garbage collector is also called as a throughput collector. Unlike the serial garbage collector, this uses multiple threads for garbage collection. Similar to serial garbage collector this also freezes all the application threads while performing garbage collection. The garbage collector is suited best for those applications that can bear application pauses.

#### CMS Garbage Collector
Concurrent Mark Sweep (CMS) garbage collector uses multiple garbage collector threads for garbage collection. It scans the heap memory to mark instances for eviction and then sweep the marked instances. It’s designed for applications that prefer shorter garbage collection pauses, and that can afford to share processor resources with the garbage collector while the application is running.

CMS garbage collector holds all the application threads in the following two scenarios only

During marking the referenced objects in the old generation space.
Any change in heap memory in parallel with doing the garbage collection
In comparison with the parallel garbage collector, CMS collector uses more CPU to ensure better application throughput. If we can allocate more CPU for better performance then CMS garbage collector is the preferred choice over the parallel collector.

####G1 (Garbage First) Garbage Collector 
is designed for applications running on multi-processor machines with large memory space. It’s available since JDK7 Update 4 and in later releases.
It separates the heap memory into regions and does collection within them in parallel. G1 also does compacts the free heap space on the go just after reclaiming the memory. But CMS garbage collector compacts the memory on stop the world (STW) situations. G1 collector will replace the CMS collector since it’s more performance efficient.

In G1 collector contains two phases;

Marking
Sweeping
Unlike other collectors, G1 collector partitions the heap into a set of equal-sized heap regions, each a contiguous range of virtual memory. When performing garbage collections, G1 shows a concurrent global marking phase to determine the liveness of objects throughout the heap.

After the mark phase is completed, G1 knows which regions are mostly empty. It collects in these areas first, which usually yields a significant amount of free space. It is why this method of garbage collection is called Garbage-First.

####
# Tuning Garbage Collection with Oracle JDK
- The simplest and most reliable way to achieve short garbage collection times over the lifetime of a production server is to use a fixed heap size with the collector and the parallel young generation collector, restricting the new generation size to at most one third of the overall heap.
- Oracle recommends using the Garbage-First (G1) garbage collector.
- The following example JVM settings are recommended for most production engine servers:
``` 

-server -Xms24G -Xmx24G -XX:PermSize=512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:ParallelGCThreads=20 -XX:ConcGCThreads=5 -XX:InitiatingHeapOccupancyPercent=70
 
 ```
- For production replica servers, use the example settings:
```
 
-server -Xms4G -Xmx4G -XX:PermSize=512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:ParallelGCThreads=20 -XX:ConcGCThreads=5 -XX:InitiatingHeapOccupancyPercent=70
 
 ```
- For standalone installations, use the example settings:
```

-server -Xms32G -Xmx32G -XX:PermSize=512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:ParallelGCThreads=20 -XX:ConcGCThreads=5 -XX:InitiatingHeapOccupancyPercent=70
 
 ```

- Starting with Java 8, the size of Metaspace isn’t defined. Once it reaches the global limit, JVM automatically increases it. However, to overcome any unnecessary instability, we can set Metaspace size with:
```
-XX:MaxMetaspaceSize=<metaspace size>[unit]
```
- Handling out of Memory : It’s very common for a large application to face an out of memory error, which in turn results in an application crash. It’s a very critical scenario, and very hard to replicate to troubleshoot the issue. That’s why JVM comes with some parameters to dump heap memory into a physical file that we can use later to find leaks:

```
-XX:+HeapDumpOnOutOfMemoryError 
-XX:HeapDumpPath=./java_pid<pid>.hprof
-XX:OnOutOfMemoryError="< cmd args >;< cmd args >" 
-XX:+UseGCOverheadLimit

```

# The above options have the following effect:

+ -Xms, -Xmx: Places boundaries on the heap size to increase the predictability of garbage collection. The heap size is limited in replica servers so that even Full GCs do not trigger SIP retransmissions. -Xms sets the starting size to prevent pauses caused by heap expansion.

+ -XX:+UseG1GC: Use the Garbage First (G1) Collector.

+ -XX:MaxGCPauseMillis: Sets a target for the maximum GC pause time. This is a soft goal, and the JVM will make its best effort to achieve it.

+ -XX:ParallelGCThreads: Sets the number of threads used during parallel phases of the garbage collectors. The default value varies with the platform on which the JVM is running.

+ -XX:ConcGCThreads: Number of threads concurrent garbage collectors will use. The default value varies with the platform on which the JVM is running.

+ -XX:InitiatingHeapOccupancyPercent: Percentage of the (entire) heap occupancy to start a concurrent GC cycle. GCs that trigger a concurrent GC cycle based on the occupancy of the entire heap and not just one of the generations, including G1, use this option. A value of 0 denotes 'do constant GC cycles'. The default value is 45.


# Top Five Reasons for Memory Hogging in Java

- Overuse of static fields
- Incorrect equals() and hashCode() implementations
- Unclosed resources
- Not removing ThreadLocals
- Overriding finalize() methods

### Overuse of Static Fields

In any Java application, the static variables declared will remain in the heap memory throughout the lifecycle of that application, that is, while it is still running. The more static fields you declare, the more heap memory you consume in the application lifecycle. Using static fields excessively can expose your Java program to a potential memory leak.

### Incorrect equals() and hashCode() Implementations
Objects are usually distinguished from one another by their equals() and hashCode() values. When you create a custom class, it's important that you override and define its equals() and hashCode() methods. This is because collections that contain unique elements depend on the implementation of these methods.
When these methods are not properly overridden and the class is declared as the key in a HashMap, the collection will probably contain duplicate keys. 

### Unclosed Resources
When you open a new connection or stream, memory is allocated to that resource. As long as it's open, that resource will continue to be allocated memory even if you're not using it. If these resources are not properly implemented.

### Not Removing ThreadLocal Objects

A ThreadLocal(https://docs.oracle.com/javase/7/docs/api/java/lang/ThreadLocal.html) is a Java object that allows you to wrap other objects that are accessible only by specific threads. Each thread has a reference to the ThreadLocal instance throughout its lifecycle.

Most web servers today maintain a thread pool(https://docs.oracle.com/javase/tutorial/essential/concurrency/pools.html), or a collection of multiple threads, each of which processes individual web requests. Threads in the thread pool are not removed after they complete their operation; instead, they are recycled for the next web request. When a web request creates a ThreadLocal instance and fails to remove it after it completes its operation, the object remains in the thread even during subsequent requests. Eventually, this blocks the garbage collector from cleaning up the unused object.

### Overriding finalize() Methods
Sometimes, you might override the finalize() method to perform custom operations before the object is garbage collected. However, objects with overridden finalizers are not immediately garbage collected, even if you invoke System.gc(). They are sent to a queue and cleaned up each time the garbage collector automatically sweeps for unreferenced objects.

When the rate at which objects with overridden finalize() methods are sent to the queue is higher than what the queue can accommodate, memory is depleted, which will eventually lead to an OutOfMemoryError.
