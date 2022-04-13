# Android repository
An Android library with common implementations for remote and local repositories

### Remote repository implementations available

- **Retrofit**

### Local repository implementations available

_Coming soon_

## How to use

### Retrofit

1. **Define an API client interface for Retrofit**

```java
public interface MyClientInterface {

    @GET("api/users/{user_id}")
    Observable<Object> getUser(@Path("user_id") int userId);
}
```
_**Important:** Each method must return an Observable_

2. **Define a Repository class extending from `RetrofitRepository<T>` where T is your API client interface.** 
  For calling yor API just invoce the method `super.call(@NonNull Observable<T> observable, Consumer<T> onSuccess, Consumer<ErrorResponse> onError, ErrorResponseInterceptor errorResponseInterceptor)` 

```java
public class MyRepository extends RetrofitRepository<MyClientInterface> {

    public JSONPlaceholderRepository(JSONPlaceholderClient client) {
        super(client);
    }

    public void getUser(int userId, Consumer<Object> onSuccess,
                        Consumer<ErrorResponse> onFailure,
                        ErrorResponseInterceptor errorResponseInterceptor) {
                        
        // Get your observable form the client calling super.getClient().getUser(1)
        call(super.getClient().getUser(userId), onSuccess, onFailure, errorResponseInterceptor);
    }
}
```

3. **Use `RetrofitClientBuilder` to generate your client, then you can create an instance of your repository**

```java
MyClient client = new RetrofitClientBuilder<>(
                BASE_URL,
                JSONPlaceholderClient.class,
                networkInterceptor
        ).build();

MyRepository repository = new MyRepository(client);

//Call example
repository.getUser(userId,
                response -> {
                    ...
                },
                error -> {
                    ...
                }, null);
```




