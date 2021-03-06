package gex.example.client.v1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import gex.example.dto.v1.ApiExceptionResponse;
import gex.example.dto.v1.ErrorResponse;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.RequestInterceptor;
import retrofit.RequestInterceptor.RequestFacade;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Builder {

  private Gson gson;
  private RestAdapter restAdapter;
  private OkHttpClient okHttpClient;
  private OAuthToken token;

  private final static Logger LOGGER = Logger.getLogger(Builder.class.getName());
  private String baseUrl;

  private Builder() {
    okHttpClient = new OkHttpClient();
    okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
    okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
    okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
  }

  public static Builder create() {
    Builder builder = new Builder();
    builder.setDefaultGson();
    builder.setDefaultApiConfig();
    return builder;
  }

  public Builder withBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  public Builder setDefaultApiConfig() {
    return withBaseUrl("http://localhost:9191");
  }

  public Builder withToken(OAuthToken token) {
    this.token = token;
    return this;
  }

  public Builder withLoginData(String username,
          String password,
          String grantType,
          String scope,
          String clientId,
          String clientSecret) {
    TokenService tokenService = new TokenService(okHttpClient, gson, baseUrl);
    this.token = tokenService.login(username, password, grantType, scope, clientId, clientSecret);
    return this;
  }

  public Builder setDefaultGson() {
    // TODO: Again we are defining a different GSON :(
    setGson(new GsonBuilder().create());
    return this;
  }

  public Builder setGson(Gson gson) {
    this.gson = gson;
    return this;
  }

  public Builder withRestAdapter(RestAdapter restAdapter) {
    this.restAdapter = restAdapter;
    return this;
  }

  private void ensureRestAdapterExists() {

    if (restAdapter == null) {
      class ApiErrorHandler implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
          Response r = cause.getResponse();
          if (r != null && isUserError(r.getStatus())) {
            if (isJsonResponse(r)) {
              ErrorResponse errorResponse = (ErrorResponse) cause.getBodyAs(ErrorResponse.class);
              if (errorResponse != null && errorResponse.getMessage() != null) {
                return new UserErrorException(errorResponse.getMessage(), errorResponse, r.getStatus(), null);
              }
            }
          }

          ApiExceptionResponse errorResponse = (ApiExceptionResponse) cause.getBodyAs(ApiExceptionResponse.class);
          if (errorResponse != null && errorResponse.getMessage() != null) {
            return new ApiException("Respuesta invalida. " + errorResponse.getMessage(), r, cause);
          } else {
            return new ApiException("Respuesta invalida. " + cause.getMessage(), r, cause);
          }
        }
      }

      RequestInterceptor authInterceptor = new RequestInterceptor() { 
        @Override
        public void intercept(RequestFacade request) {
          if(token != null) {
            request.addHeader("Authorization", "Bearer " + token.getAccessToken());
          }
        }
      };

      RestAdapter defaultAdapter = new RestAdapter.Builder()
          .setEndpoint(baseUrl)
          .setRequestInterceptor(authInterceptor)
          .setConverter(new GsonConverter(gson))
          .setClient(new OkClient(okHttpClient))
          .setErrorHandler(new ApiErrorHandler())
          .build();

      withRestAdapter(defaultAdapter);
    }
  }

  private boolean isJsonResponse(Response response) {
    boolean json = false;
    for (Header h : response.getHeaders()) {
      if (h.getName() != null && h.getName().toLowerCase().equals("content-type")) {
        return h.getValue().contains("json");
      }
    }
    return json;
  }

  private boolean isUserError(Integer statusCode) {
    return statusCode >= 400 && statusCode < 500;
  }

  public ApiClient buildApi() {
    ensureRestAdapterExists();
    return restAdapter.create(ApiClient.class);
  }


}
