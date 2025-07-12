package data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import models.PriorityLevel;
import models.Task;
import models.User;

/**
 * Authenticates using credentials.json from src/main/resources
 * Stores OAuth token in a local tokens/ directory
 * Returns a ready-to-use Sheets service client
 */
public class SheetsServiceUtil {
	private static final String APPLICATION_NAME = "StudyPlanner";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	
	// scopes for reading/ writing spreadsheets
	//private static final List<String> SCOPES =  Arrays.asList(SheetsScopes.SPREADSHEETS);;
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	
	private static final String SPREADSHEET_ID = "1iZvc_XNtGPA7FtMmm1Jhg-CA7Vb6-zIP-UCsyp1ivBM";
	/**
     * Loads credentials and authorizes the app using OAuth 2.0.
     */
	private static com.google.api.client.auth.oauth2.Credential getCredentials
		(final NetHttpTransport HTTP_TRANSPORT) throws IOException{
		
		InputStream inputStream = SheetsServiceUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if(inputStream == null) {
			throw new IOException("Resource not found " + CREDENTIALS_FILE_PATH);
		}
		
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
		
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.setAccessType("offline")
				.build();
		
		
		return new AuthorizationCodeInstalledApp(flow, 
				new LocalServerReceiver.Builder().setPort(8888).build()).authorize("user");
		
	}
	
	 /**
     * Returns an authorized Sheets API client service.
     */
	public static Sheets getSheetsService() throws IOException, GeneralSecurityException{
		
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME)
				.build();
	}
	
	/**
     * Reads all users from the Google Sheet's "Users" tab.
     * @return a list of all users (username-password pairs)
     */
	public static ArrayList<User> readUsersFromSheets () {
		ArrayList<User> users = new ArrayList<>();	
		try {
			Sheets service = getSheetsService();
			// sheet Users has 2 columns : Username, Password
			String range = "Users!A2:B";
			ValueRange response = service.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
			List<List<Object>> values = response.getValues();
			
			// if null, empty list is returned
			if (values != null) {
				// skip header row
				for (List<Object> row : values) {
					String username = row.size() > 0 ? row.get(0).toString() : "";
					String password = row.size() > 1 ? row.get(1).toString() : "";
					// if username is not blank
					if (!username.isBlank()) {
						users.add(new User(username, password));
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();		
		}
		
		return users;
	}
	
	 /**
     * Appends a new user to the "Users" tab in the Google Sheet.
     * @param user the new user to add
     */
	public static void writeUserToSheet(User user) {
		try {
			Sheets service = getSheetsService();
			
			List<List<Object>> values = new ArrayList<>();
			List<Object> row = List.of(user.getUsername(), user.getPassword());
			values.add(row);
			
			ValueRange body = new ValueRange().setValues(values);
			
			service.spreadsheets().values()
				.append(SPREADSHEET_ID, "Users!A:B", body) // add to next available row
				.setValueInputOption("RAW")
				.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * Saves all tasks of the current user to the Google Sheet.
     * Clears any previous task entries of that user from the sheet.
     * 
     * @param tasks all current tasks of the user
     * @param username the username used to name the file
     */
	public static void writeTasksToSheet(ArrayList<Task> tasks, String username) {
		try{
			Sheets service = getSheetsService();
			String dataRange = "Tasks!A2:F";
			
			// clear previous tasks
			ValueRange response = service.spreadsheets().values().get(SPREADSHEET_ID, dataRange).execute();
			List<List<Object>> allRows = response.getValues();
			
			if(allRows == null) {
				allRows = new ArrayList<>();
			}
			 
			// filter out current user's tasks
			List<List<Object>> remainingRows = allRows.stream()
					.filter(row -> row.size() > 0 && !row.get(0).toString().equals(username))
					.collect(Collectors.toList());
 			
			// add updated/new tasks
			for(Task task : tasks) {
				List<Object> row = List.of(
						username,
						task.getTitle(),
						task.getDescription(),
						task.getPriorityLevel().name(),
						task.getDeadline().toString(),
						String.valueOf(task.isCompleted())
						);
				remainingRows.add(row);
			}
			
			// sort by names alphabetically
			remainingRows.sort(Comparator.comparing(row -> row.get(0).toString()));
			
			// clear all data except header
			service.spreadsheets().values()
				.clear(SPREADSHEET_ID, "Tasks!A2:F", new ClearValuesRequest())
				.execute();
			
			ValueRange newData = new ValueRange().setValues(remainingRows);
			
			service.spreadsheets().values()
				.append(SPREADSHEET_ID, "Tasks!A2:F", newData)  
				.setValueInputOption("RAW")
				.execute();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
     * Loads all tasks belonging to the given user from the Google Sheet.
     *
     * @param username the username of the user
     */
	public static ArrayList<Task> readTasksFromSheet(String username){
		ArrayList<Task> tasks = new ArrayList<>();
		try {
			Sheets service = getSheetsService();
			// skip header row
			String range = "Tasks!A2:F";
			ValueRange response = service.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
			List<List<Object>> values = response.getValues();
			
			// return empty list if no data found
			if(values != null) {
				
				for(List<Object> row : values) {
					
					// skip invalid rows
					if (row.size() < 6) {
						continue;
					}
					String usernameStr = row.get(0).toString();
					
					// skip rows not for this user
					if (!usernameStr.equals(username)) {
						continue;
					}
					String title = row.get(1).toString();
					String description = row.get(2).toString();
					PriorityLevel priorityLevel = PriorityLevel.valueOf(row.get(3).toString());
					LocalDate deadline = LocalDate.parse(row.get(4).toString());
					Boolean completed = Boolean.parseBoolean(row.get(5).toString());
					
					Task task = new Task(title, description, deadline, priorityLevel);
					task.setCompleted(completed);
					tasks.add(task);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tasks;
	}
}
