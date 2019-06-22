package com.skilldistillery.filmquery.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.midi.SysexMessage;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
	boolean userWrongInput = true;

	public FilmQueryApp() throws ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
	}

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws Exception {
		FilmQueryApp app = new FilmQueryApp();
		// app.test();
		app.launch();
	}

	private void test() {
		Film film = db.findFilmById(1);
		film.printActors();
		Actor actor = db.findActorById(1);
		System.out.println(actor);
		List<Actor> actors = db.findActorsByFilmId(1);
		System.out.println(actors.size());
	}

	private void launch() {
		Scanner input = new Scanner(System.in);
		while(userWrongInput){
		try {
		startUserInterface(input);
		}catch (Exception e) {
			System.out.println("Invalid input try again");
			input.nextLine();
		}
		}

		input.close();
	}

	private void startUserInterface(Scanner input) throws Exception {
		
		
		do {
			System.out.println("Menu");
			System.out.println("1. Look up a film by its id.");
			System.out.println("2. Look up a film by a search keyword.");
			System.out.println("3. Exit the application.");
			int userInput = input.nextInt();
			switch (userInput) {
			// films by id
			case 1:
				System.out.println("Input a film id");
				int id = input.nextInt();
				Film byID = db.findFilmById(id);
				if(byID instanceof Film) {
					System.out.println(byID);
				}else {
					System.out.println("Film not found");
				}
				break;

			// film by keyword
			case 2:
				System.out.println("Input a search keyword");
				String keyWord = input.next();	
				List<Film> films = db.findFilmByKeyword(keyWord);
				
				if(films.size() > 0) {
					for (Film film : films) {
					System.out.println(film);
					List<Actor> actors = db.findActorsByFilmId(film.getId());
					System.out.println(actors);
				}

				}else {
					System.out.println("Keyword not found");
				}
				break;
				//exit program
			case 3:
				System.out.println("Come back again");
				userWrongInput = false;
				//System.exit(0);
				break;
				//invalid input
			default:
				System.out.println("Pick a correct choice");
			}
		} while (userWrongInput);

	}
}
