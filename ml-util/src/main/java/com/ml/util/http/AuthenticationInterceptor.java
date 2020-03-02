package com.ml.util.http;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * com.ml.util.http
 *
 * @author yansongda <me@yansongda.cn>
 * @version 2019/11/6 下午12:59
 */
public class AuthenticationInterceptor implements Interceptor {

    private HashMap<String, String> headerAuthentication = new HashMap<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request userRequest = chain.request();
        Request.Builder requestBuilder = userRequest.newBuilder();

        buildHeaderAuthentication(userRequest, requestBuilder);

        return chain.proceed(requestBuilder.build());
    }

    /**
     * http basic 认证.
     *
     * @param username String
     * @param password String
     */
    public void basicAuthentication(String username, String password) {
        if (null != username && null != password) {
            headerAuthentication.put("Authorization", Credentials.basic(username, password));
        }
    }

    private void buildHeaderAuthentication(Request userRequest, Request.Builder requestBuilder) {
        if (headerAuthentication.size() > 0) {
            Headers.Builder headerBuilder = userRequest.headers().newBuilder();

            headerAuthentication.forEach(headerBuilder::add);
            requestBuilder.headers(headerBuilder.build());
        }
    }
}
