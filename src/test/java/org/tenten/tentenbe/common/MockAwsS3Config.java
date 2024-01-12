package org.tenten.tentenbe.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.tenten.tentenbe.config.S3Config;

//@Configuration
public class MockAwsS3Config extends S3Config {
    @Bean
    @Primary
    @Override
    public AmazonS3Client amazonS3Client() {
        return Mockito.mock(AmazonS3Client.class);
    }
}