package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RpmVersionComparator comparator = new RpmVersionComparator();

        while (true) {
            System.out.print("Enter first branch: ");
            String firstBranch = scanner.nextLine();
            System.out.print("Enter second branch: ");
            String secondBranch = scanner.nextLine();

            try {
                String firstBranchPackages = requestPackagesFromBranch(firstBranch);
                String secondBranchPackages = requestPackagesFromBranch(secondBranch);

                System.out.println("Response received");

                String comparisonResult = comparator.compareBranches(
                        firstBranchPackages,
                        secondBranchPackages,
                        firstBranch,
                        secondBranch
                );

                System.out.println(comparisonResult);

                try (FileWriter file = new FileWriter("comparison_result.json")){
                    file.write(comparisonResult);
                    System.out.println("Result saved to comparison_result.json");
                } catch (IOException e){
                    System.out.println("Error saving result to file: " + e.getMessage());
                }

            } catch (Exception e) {
                System.err.println("Error occurred: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.print("Do you want to make another comparison? [y/n]: ");
            String userInput = scanner.nextLine();

            if (!userInput.equalsIgnoreCase("y")) {
                System.out.println("Canceled.");
                break;
            }
        }

        scanner.close();
    }

    public static String requestPackagesFromBranch(String branch) throws Exception {
        String host = "https://rdb.altlinux.org";
        String path = "/api/export/branch_binary_packages/" + branch;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(host + path))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}