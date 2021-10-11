import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=EioQCWTQ5WMvbqY4HfVXZcE9RmG4MeVg255cBLmL");

        CloseableHttpResponse response = httpClient.execute(request);
        Post post = mapper.readValue(response.getEntity().getContent(), Post.class);

        HttpGet secondRequest = new HttpGet(post.getUrl());
        CloseableHttpResponse secondResponse = httpClient.execute(secondRequest);
        byte[] bytes = secondResponse.getEntity().getContent().readAllBytes();
        File file = new File("NasaImage.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bos.write(bytes);
        bos.flush();
        bos.close();
    }
}

