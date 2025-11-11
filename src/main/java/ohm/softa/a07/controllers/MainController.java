package ohm.softa.a07.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import ohm.softa.a07.api.OpenMensaAPI;
import ohm.softa.a07.model.Meal;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainController implements Initializable {
	// use for debugging
	private static final Logger LOGGER = Logger.getLogger( MainController.class.getName() );
	private OpenMensaAPI openMensaAPI;

	// use annotation to tie to component in XML
	@FXML
	private Button btnRefresh;

	// add additional fields
	@FXML
	private Button btnClose;
	@FXML
	private CheckBox chkVegetarian;

	@FXML
	private ListView<String> mealsList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set the event handler (callback)
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
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

				// create a new (observable) list and tie it to the view
				LOGGER.info("go in refresh");
				ObservableList<String> list = FXCollections.observableArrayList(resp.body());
				mealsList.setItems(list);
			}
		});

		// add additional event handlers
		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				LOGGER.info("go in close");
				ObservableList<String> list = FXCollections.observableArrayList("AHA", "Dampf");
				mealsList.setItems(list);
			}
		});
		chkVegetarian.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				LOGGER.info("go in vegetarian");
				ObservableList<String> list = FXCollections.observableArrayList("VEGGI", "Dampf");
				mealsList.setItems(list);
			}
		});
	}
}
