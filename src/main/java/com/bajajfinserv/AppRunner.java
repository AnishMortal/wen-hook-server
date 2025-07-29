package com.bajajfinserv;

import com.bajajfinserv.dto.SolutionRequest;
import com.bajajfinserv.dto.WebhookRequest;
import com.bajajfinserv.dto.WebhookResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AppRunner implements CommandLineRunner {

    private final WebClient webClient;

    public AppRunner() {
        this.webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application starting...");

        // Using your data
        WebhookRequest requestBody = new WebhookRequest("Anish Singal", "2210990116", "anish116.be22@chitkara.edu.in");

        generateWebhookAndSubmit(requestBody)
            .doOnSuccess(response -> System.out.println("\nProcess completed successfully. Server response: " + response))
            .doOnError(error -> System.err.println("\nProcess failed: " + error.getMessage()))
            .subscribe();
    }

    private Mono<String> generateWebhookAndSubmit(WebhookRequest requestBody) {
        String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        System.out.println("1. Preparing to generate webhook with data: " + requestBody);

        return webClient.post()
            .uri(generateUrl)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(WebhookResponse.class) // Directly parse to the object now that the mapping is correct
            .flatMap(response -> {
                System.out.println("2. Received and parsed response: " + response);

                if (response.getAccessToken() == null || response.getAccessToken().isEmpty()) {
                    return Mono.error(new RuntimeException("FATAL: Access Token is still null. This should not happen."));
                }

                String finalQuery = getFinalSqlQuery(requestBody.getRegNo());
                SolutionRequest solution = new SolutionRequest(finalQuery);

                System.out.println("3. Preparing to submit solution...");
                return submitSolution(response.getWebhook(), response.getAccessToken(), solution);
            });
    }

    private Mono<String> submitSolution(String webhookUrl, String token, SolutionRequest solution) {
        return webClient.post()
            .uri(webhookUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .bodyValue(solution)
            .retrieve()
            .bodyToMono(String.class);
    }

    private String getFinalSqlQuery(String regNo) {
        int number = Integer.parseInt(regNo.substring(regNo.length() - 2));
        System.out.println("\nRegistration number '" + regNo + "' has last two digits " + number + ".");

        if (number % 2 != 0) {
            System.out.println("Result: Odd number. Using SQL for Question 1.");
            return "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, e.DOB, p.PAYMENT_TIME) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE DAY(p.PAYMENT_TIME) <> 1 ORDER BY p.AMOUNT DESC LIMIT 1;";
        } else {
            System.out.println("Result: Even number. Using SQL for Question 2.");
            return "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, (SELECT COUNT(*) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e1.DEPARTMENT AND e2.DOB > e1.DOB) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID ORDER BY e1.EMP_ID DESC;";
        }
    }
}
