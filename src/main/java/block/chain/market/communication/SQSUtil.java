package block.chain.market.communication;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SQSUtil<T> {

    @Value("${sqs.url}")
    private String sqsUrl;

    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    private AmazonSQS amazonSQS;
    
    private ObjectMapper objectMapper;


    @PostConstruct
    private void postConstructor() {

        log.info("SQS URL: " + sqsUrl);
        log.info("Region: " + awsRegion);

        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsAccessKey, awsSecretKey)
        );

        this.amazonSQS = AmazonSQSClientBuilder.standard()
        					.withRegion(this.awsRegion)
        					.withCredentials(awsCredentialsProvider)
        					.build();
        
        this.objectMapper = new ObjectMapper();
    }

    public void sendSQSMessage(T message) throws JsonProcessingException {

        log.info("Sending SQS message: " + message);

        SendMessageResult result = this.amazonSQS.sendMessage(this.sqsUrl, this.objectMapper.writeValueAsString(message));

        log.info("SQS Message ID: " + result.getMessageId());
    }
}