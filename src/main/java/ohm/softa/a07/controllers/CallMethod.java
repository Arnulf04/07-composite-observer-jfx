package ohm.softa.a07.controllers;

import ohm.softa.a07.api.OpenMensaAPI;
import ohm.softa.a07.model.Meal;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CallMethod {
	private static OpenMensaAPI openMensaAPI;

	public static List<Meal> getMeals() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String today = sdf.format(new Date());

		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient client = new OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.build();

		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("https://openmensa.org/api/v2/")
			.client(client)
			.build();

		openMensaAPI = retrofit.create(OpenMensaAPI.class);

		Call<List<Meal>> call = openMensaAPI.getMeal(today);
		Response<List<Meal>> resp = null;
		try {
			resp = call.execute();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (resp.isSuccessful()){
			System.out.println(resp.body());
		}

		List<Meal> meals = resp.body();

		return meals;
	}
}
