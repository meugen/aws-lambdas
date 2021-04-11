package meugeninua.awslambdas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;
import java.time.Duration;
import java.util.UUID;

public class SlowRestHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final Gson gson = new GsonBuilder().create();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Duration duration = extractDuration(input);
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            context.getLogger().log(e.toString());
        }

        String response = gson.toJson(UUID.randomUUID());
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpURLConnection.HTTP_OK)
                .withBody(response);
    }

    private Duration extractDuration(APIGatewayProxyRequestEvent input) {
        try {
            String delay = input.getQueryStringParameters().get("delay");
            return Duration.parse(delay);
        } catch (Throwable e) {
            return Duration.ZERO;
        }
    }
}
