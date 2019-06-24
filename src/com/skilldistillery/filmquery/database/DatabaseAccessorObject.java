package com.skilldistillery.filmquery.database;

import java.sql.*;
import java.util.*;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	@Override
	public Film findFilmById(int filmId) {
		final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
		String user = "student";
		String pass = "student";
		Connection conn = null;
		{
			try {
				conn = DriverManager.getConnection(URL, user, pass);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Film film = null;
			try {
				String sql = "SELECT film.id, film.title, film.description, film.release_year, language.name, film.rental_duration, film.rental_rate, film.length, film.replacement_cost,film.rating, film.special_features FROM film Join language on film.language_id = language.id Where film.id = ?";

				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, filmId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//int id = rs.getInt("id");
					String title = rs.getString("title");
					String desc = rs.getString("description");
					short releaseYear = rs.getShort("release_year");
					String langId = rs.getString("language.name");
					int rentDur = rs.getInt("rental_duration");
					double rate = rs.getDouble("rental_rate");
					int length = rs.getInt("length");
					double repCost = rs.getDouble("replacement_cost");
					String rating = rs.getString("rating");
					String features = rs.getString("special_features");
					List<Actor> actors = findActorsByFilmId(filmId);
					film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
							features, actors);
				}
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return film;
		}
	}

	@Override
	public Actor findActorById(int actorId) {
		final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
		String user = "student";
		String pass = "student";
		Connection conn = null;
		Actor actor = null;
		{
			try {
				conn = DriverManager.getConnection(URL, user, pass);

				String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, actorId);
				ResultSet ar = stmt.executeQuery();
				if (ar.next()) {
					actor = new Actor();
					// Here is our mapping of query columns to our object fields:
					actor.setId(ar.getInt(1));
					actor.setFirstName(ar.getString(2));
					actor.setLastName(ar.getString(3));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
		String user = "student";
		String pass = "student";
		Connection conn = null;
		{
			List<Actor> actorList = new ArrayList<>();
			try {
				conn = DriverManager.getConnection(URL, user, pass);

				String sql = "select actor.id, actor.first_name, actor.last_name from actor "
						+ "join film_actor on actor.id = film_actor.actor_id "
						+ "join film on film.id = film_actor.film_id " + "where film.id = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, filmId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					int id = rs.getInt(1);
					String firstName = rs.getString(2);
					String lastName = rs.getString(3);
					Actor actor = new Actor(id, firstName, lastName);
					actorList.add(actor);
				}
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return actorList;
		}
	}

	public List<Film> findFilmByKeyword(String filmKeyword) {

		final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
		String user = "student";
		String pass = "student";
		Connection conn = null;
		{

			List<Film> films = new ArrayList<>();
			try {
				conn = DriverManager.getConnection(URL, user, pass);
				String sql = "SELECT film.id, film.title, film.description, film.release_year, language.name,"
						+ "film.rental_duration, film.rental_rate, film.length, film.replacement_cost,"
						+ "film.rating, film.special_features"
						+ " FROM film Join language on film.language_id = language.id WHERE film.title LIKE ? OR film.description LIKE ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, "%" + filmKeyword + "%");
				stmt.setString(2, "%" + filmKeyword + "%");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Film film = new Film();
					film.setId(rs.getInt("id"));
					film.setTitle(rs.getString("title"));
					film.setDescription(rs.getString("description"));
					film.setReleaseYear(rs.getInt("release_year"));
					film.setLanguageId(rs.getString("language.name"));
					film.setRentalDuration(rs.getInt("rental_Duration"));
					film.setRentalRate(rs.getDouble("rental_rate"));
					film.setLength(rs.getInt("length"));
					film.setReplacementCost(rs.getDouble("replacement_cost"));
					film.setRating(rs.getString("rating"));
					film.setSpecialFeatures(rs.getString("special_features"));
					films.add(film);
				}
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return films;
		}
	}

	public List<Actor> findActorInFilm(int filmId) {

		final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
		String user = "student";
		String pass = "student";
		Connection conn = null;
		{

			List<Actor> actors = new ArrayList<>();

			try {
				conn = DriverManager.getConnection(URL, user, pass);
				String sql = "select actor.id, actor.first_name, actor.last_name" + " from actor"
						+ " join film_actor on actor.id = film_actor.actor_id"
						+ " join film on film.id = film_actor.film_id" + " where film.id = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, filmId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("actor.id");
					String firstName = rs.getString("actor.first_name");
					String lastName = rs.getString("actor.last_name");

					Actor actor = new Actor(id, firstName, lastName);
					actors.add(actor);
				}
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return actors;
		}

	}

}
