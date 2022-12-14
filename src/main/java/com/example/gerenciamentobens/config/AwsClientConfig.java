package com.example.gerenciamentobens.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

@Configuration
public class AwsClientConfig {

    @Value("${aws.s3.is-minio}")
    private Boolean isMinio;

    @Value("${aws.s3.url}")
    private String s3Url;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @Value("${aws.dynamodb.table-name}")
    private String dynamoTableName;

    @Value("${aws.dynamodb.is-local}")
    private Boolean isDynamoLocal;

    @Value("${aws.dynamodb.url}")
    private String dynamoDbUrl;

    @Bean
    public AmazonS3 amazonS3Client() {
        if (isMinio) {
            AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(accessKey, secretKey));
            return AmazonS3ClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withPathStyleAccessEnabled(true)
                    .withEndpointConfiguration(
                            new EndpointConfiguration(s3Url, "us-east-1"))
                    .build();
        }

        // when not using Minio, credentials will be provided by a external config file, since that's AWS Default.
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient amazonDynamoEnhancedClient(){
        if (isDynamoLocal) {
            DynamoDbClient dynamoClient = DynamoDbClient.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("some", "credentials")))
                    .endpointOverride(URI.create("http://dynamo:8000/"))
                    .region(Region.US_EAST_1)
                    .build();

            var enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoClient)
                    .build();

            try {
                dynamoClient
                        .createTable(CreateTableRequest
                                .builder()
                                .tableName(dynamoTableName)
                                .attributeDefinitions(AttributeDefinition.builder()
                                        .attributeName("id")
                                        .attributeType(ScalarAttributeType.S)
                                        .build())
                                .keySchema(KeySchemaElement
                                        .builder()
                                        .attributeName("id")
                                        .keyType(KeyType.HASH)
                                        .build())
                                .provisionedThroughput(ProvisionedThroughput.builder()
                                        .readCapacityUnits(10L)
                                        .writeCapacityUnits(10L)
                                        .build())
                                .build());
            } catch (Exception e) {
                System.out.println("Error creating dynamo table");
                e.printStackTrace();
            }

            return enhancedClient;
        }

        DynamoDbClient dynamoClient = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoClient)
                .build();
    }
}
