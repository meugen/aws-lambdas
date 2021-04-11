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
        String delay = input.getQueryStringParameters().get("delay");
        Duration duration = delay == null ? Duration.ZERO : Duration.parse(delay);
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            context.getLogger().log(e.toString());
        }

        String response = gson.toJson(UUID.randomUUID());
        return new APIGatewayProxyResponseEvent()
                .withBody(response)
                .withStatusCode(HttpURLConnection.HTTP_OK);
    }
}
