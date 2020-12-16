package com.dietmanager.dietician.network;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class SingleErrorUtils {

    public static APISingleError parseError(Response<?> response) {
        Converter<ResponseBody, APISingleError> converter =
                ApiClient.getRetrofit()
                        .responseBodyConverter(APISingleError.class, new Annotation[0]);

        APISingleError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APISingleError();
        }

        return error;
    }
}