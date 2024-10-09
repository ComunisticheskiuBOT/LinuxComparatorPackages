package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RpmVersionComparator {

    private final ObjectMapper objectMapper = new ObjectMapper();

    static { System.loadLibrary("LinuxComparatorPackages");}
    public native boolean compareVersions(String firstPackageVersion, String secondPackageVersion);

    public String compareBranches(String firstBranchPackages, String secondBranchPackages, String firstBranch, String secondBranch) throws Exception {
        ObjectNode firstBranchObj = (ObjectNode) objectMapper.readTree(firstBranchPackages);
        ObjectNode secondBranchObj = (ObjectNode) objectMapper.readTree(secondBranchPackages);

        // Получаем массивы пакетов из первой и второй ветки
        ArrayNode firstBranchPackageList = (ArrayNode) firstBranchObj.get("packages");
        ArrayNode secondBranchPackageList = (ArrayNode) secondBranchObj.get("packages");

        // Создаем итоговый JSON
        ObjectNode outputJSON = objectMapper.createObjectNode();
        outputJSON.put("first_branch", firstBranch);
        outputJSON.put("second_branch", secondBranch);

        int lastSecondIndex = 0;
        for (int i = 0; i < firstBranchPackageList.size(); i++) {
            ObjectNode firstPackage = (ObjectNode) firstBranchPackageList.get(i);
            boolean firstPackageNotFound = true;

            for (int j = lastSecondIndex; j < secondBranchPackageList.size(); j++) {
                ObjectNode secondPackage = (ObjectNode) secondBranchPackageList.get(j);

                if (firstPackage.get("arch").asText().compareTo(secondPackage.get("arch").asText()) < 0) {
                    break;
                }

                if (firstPackage.get("name").asText().equals(secondPackage.get("name").asText()) &&
                        firstPackage.get("arch").asText().equals(secondPackage.get("arch").asText())) {

                    firstPackageNotFound = false;

                    for (int k = lastSecondIndex; k < j; k++) {
                        addPackageToList("packages_only_in_second_branch", outputJSON, (ObjectNode) secondBranchPackageList.get(k));
                    }

                    lastSecondIndex = j + 1;

                    if (compareVersions(constructVersion(firstPackage), constructVersion(secondPackage))) {
                        addPackageToList("packages_with_bigger_version_in_first_branch", outputJSON, secondPackage);
                    }
                    break;
                }
            }

            if (firstPackageNotFound) {
                addPackageToList("packages_only_in_first_branch", outputJSON, firstPackage);
            }
        }

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(outputJSON);
    }

    private String constructVersion(ObjectNode packageObj) {
        return packageObj.get("epoch").asInt() + ":" +
                packageObj.get("version").asText() + "-" +
                packageObj.get("release").asText();
    }

    private void addPackageToList(String listName, ObjectNode outputJSON, ObjectNode packageObj) {
        ArrayNode packageList = (ArrayNode) outputJSON.get(listName);

        if (packageList == null) {
            packageList = objectMapper.createArrayNode();
            outputJSON.set(listName, packageList);
        }

        packageList.add(packageObj);
    }
}

