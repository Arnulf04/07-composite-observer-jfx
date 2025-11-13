package ohm.softa.a07.controllers;

import javafx.application.Platform;
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
import java.util.stream.Collectors;

public class MainController implements Initializable {
	// use for debugging
	private static final Logger LOGGER = Logger.getLogger( MainController.class.getName() );
	private List<Meal> meals = CallMethod.getMeals();

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
//				List<Meal> meals = CallMethod.getMeals();

				// create a new (observable) list and tie it to the view
				LOGGER.info("go in refresh");
				List<String> mealNames = meals.stream()
					.map(Meal::getName) // Ruft getName() für jedes Meal auf
					.collect(Collectors.toList());
				ObservableList<String> list = FXCollections.observableArrayList(mealNames);
				LOGGER.info(list.toString());
				mealsList.setItems(list);
			}
		});

		// add additional event handlers
		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				LOGGER.info("go in close");

				// leave program
				Platform.exit();
				System.exit(0);
			}
		});
		chkVegetarian.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				LOGGER.info("go in vegetarian");

				// create a new (observable) list and tie it to the view
				LOGGER.info("go in refresh");
				List<String> mealNames = meals.stream()
					.map(Meal::getName) // Ruft getName() für jedes Meal auf
					.filter(meal -> meal.toLowerCase().equals("vegetarisch"))
					.collect(Collectors.toList());
				ObservableList<String> list = FXCollections.observableArrayList(mealNames);
				LOGGER.info(list.toString());
				mealsList.setItems(list);
			}
		});
	}
}
