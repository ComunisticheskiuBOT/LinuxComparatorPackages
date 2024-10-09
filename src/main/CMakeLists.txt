cmake_minimum_required(VERSION 3.5)
#Название проекта
project(Java_RpmVersionComparator_compareVersions) 
#Пути
set(JAVA_HOME "/home/sovok/idea-IC-242.21829.142")
set(JAVA_INCLUDE_PATH "${JAVA_HOME}/jbr/include")
set(RPM_INCLUDE_PATH "/usr/include/rpm")
set(ORG_EXAMPLE_RPM_VERSION_COMPARATOR "/home/sovok/IdeaProjects/LinuxComparatorPackages/")
find_package(JNI REQUIRED)

set(SOURCE_FILES Java_RpmVersionComparator.cpp org_example_RpmVersionComparator.h)

add_library(LinuxComparatorPackages SHARED ${SOURCE_FILES})

include_directories(${JAVA_INCLUDE_PATH} ${RPM_INCLUDE_PATH} ${ORG_EXAMPLE_RPM_VERSION_COMPARATOR})

target_link_libraries(LinuxComparatorPackages rpm)

set_target_properties(LinuxComparatorPackages PROPERTIES LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/lib")