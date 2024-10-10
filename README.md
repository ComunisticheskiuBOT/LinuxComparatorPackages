# LinuxComparatorPackages

LinuxComparatorPackages is a utility for comparing binary packages between branches from the public REST API of ALT Linux. The utility is written in Java and uses a native library to accurately compare package versions.


## Installation

 Dependencies:
 Make sure that Java 11+, CMake, gcc, make, and the librpm package are installed.

 On ALT Linux, install the necessary dependencies:

<code>sudo apt-get update</code>
<code>sudo apt-get install -y cmake gcc make librpm librpm-devel maven</code>


## Cloning a repository:

<code>git clone https://github.com/ComunisticheskiuBOT/LinuxComparatorPackages.git</code>
<code>cd LinuxComparatorPackages</code>

## Set up variables in CMakeLists.txt:

Open it CMakeLists.txt and edit the following lines:
Make sure that the specified path to JAVA_HOME corresponds to your installed JDK:

<code>set(JAVA_HOME "/path/to/your/jdk")</code>

Make sure that the ORG_EXAMPLE_RPM_VERSION_COMPARATOR variable points to the correct path to your Java package:

<code>set(ORG_EXAMPLE_RPM_VERSION_COMPARATOR "path/to/project")</code>

## Building a native library:

Go to the src directory and run the following commands:

<code>mkdir -p native</code>
<code>mkdir -p build</code>
<code>cd build</code>
<code>cmake ..</code>
<code>make</code>

This will create a file libLinuxComparatorPackages.so in the build folder.
Transfer the collected library .so to the standard directory for ALT Linux dynamic libraries:

<code>sudo cp {ALL_WAY}/build/libLinuxComparatorPackages.so /usr/lib/</code>

## FAT JAR Assembly:

Go to the root directory of the project and build the FAT JAR using the command:

<code>mvn clean package</code>

This will create a JAR file target/LinuxComparatorPackages-1.0.jar .
Transfer the collected library to the standard directory for ALT Linux libraries:

<code>sudo cp {ALL_WAY}/target/LinuxComparatorPackages-1.0.jar /usr/lib/</code>

## Launch

Run the utility using java -jar:

<code>java -jar usr/lib/LinuxComparatorPackages-1.0.jar</code>

At startup, the utility will ask you for the names of the branches for comparison. Enter the names of the branches (for example, p10 and sisyphus), after which the utility will make a request to the ALT Linux REST API and output the comparison result to the console and save it to the comparison_result file.json in the current directory.

## Usage examples

 ### Comparing branches:

<code>java -jar usr/lib/LinuxComparatorPackages-1.0.jar</code>

Enter the first and second branch, for example, page 10 and page 9. Try to save the packages and create a JSON file with comments in the format:

### The json file
```
{
  "first_branch": "p10",
  "second_branch": "p9",
  "packages_only_in_first_branch": [ {
    "name" : "0ad",
    "epoch" : 1,
    "version" : "0.0.23b",
    "release" : "alt3",
    "arch" : "aarch64",
    "disttag" : "p9+236883.300.7.1",
    "buildtime" : 1576062757,
    "source" : "0ad"
  }],
  "packages_only_in_second_branch": [{
    "name" : "7-zip",
    "epoch" : 0,
    "version" : "23.01",
    "release" : "alt1",
    "arch" : "aarch64",
    "disttag" : "p10+338796.100.3.1",
    "buildtime" : 1706693839,
    "source" : "7-zip"
  }],
  "packages_with_bigger_version_in_first_branch": [{
    "name" : "389-ds-base-legacy-tools",
    "epoch" : 0,
    "version" : "1.4.1.18",
    "release" : "alt5.p9.1",
    "arch" : "aarch64",
    "disttag" : "p9+355704.100.1.1",
    "buildtime" : 1724253992,
    "source" : "389-ds-base"
  }]
}
Result saved to comparison_result.json
Do you want to male another comparison? [y/n]:
```
