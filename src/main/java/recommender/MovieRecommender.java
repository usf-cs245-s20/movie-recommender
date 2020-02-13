package recommender;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.io.BufferedReader;

/** recommender.MovieRecommender. A class that is responsible for:
 - Reading movie and ratings data from the file and loading it in the data structure recommender.UsersList.
 *  - Computing movie recommendations for a given user and printing them to a file.
 *  - Computing movie "anti-recommendations" for a given user and printing them to file.
 *  Fill in code in methods of this class.
 *  Do not modify signatures of methods.
 */
public class MovieRecommender {
    private UsersList usersData; // linked list of users
    private HashMap<Integer, String> movieMap; // maps each movieId to the movie title

    public MovieRecommender() {
        movieMap = new HashMap<>();
        usersData = new UsersList();
    }

    /**
     * Read user ratings from the file and save data for each user in this list.
     * For each user, the ratings list will be sorted by rating (from largest to
     * smallest).
     * @param movieFilename name of the file with movie info
     * @param ratingsFilename name of the file with ratings info
     */
    public void loadData(String movieFilename, String ratingsFilename) {

        loadMovies(movieFilename);
        loadRatings(ratingsFilename);
    }

    /** Load information about movie ids and titles from the given file.
     *  Store information in a hashmap that maps each movie id to a movie title
     *
     * @param movieFilename csv file that contains movie information.
     *
     */
    private void loadMovies(String movieFilename) {
        try (BufferedReader br = new BufferedReader(new FileReader(movieFilename))){
            String line;
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] movieInfo;
                String movieName;
                if (line.contains(",\"")){
                    movieInfo = line.split(",\"");
                    movieName = movieInfo[1].split("\",")[0];
                }else{
                    movieInfo = line.split(",");
                    movieName = movieInfo[1];
                }
                movieMap.put(Integer.parseInt(movieInfo[0]), movieName);
            }

        }catch (IOException e){
            System.out.println("Could not read from the file: " + movieFilename);
        }

    }

    /**
     * Load users' movie ratings from the file into recommender.UsersList
     * @param ratingsFilename name of the file that contains ratings
     */
    private void loadRatings(String ratingsFilename) {
        try(BufferedReader br = new BufferedReader(new FileReader(ratingsFilename))){
            String line;
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] ratingsInfo = line.split(",");
                usersData.insert(Integer.parseInt(ratingsInfo[0]),Integer.parseInt(ratingsInfo[1]), Double.parseDouble(ratingsInfo[2]));
            }
        }
        catch (IOException e){
            System.out.println("Could not read from the file: " + ratingsFilename);
        }

    }

    /**
     * * Computes up to num movie recommendations for the user with the given user
     * id and prints these movie titles to the given file. First calls
     * findMostSimilarUser and then getFavoriteMovies(num) method on the
     * "most similar user" to get up to num recommendations. Prints movies that
     * the user with the given userId has not seen yet.
     * @param userid id of the user
     * @param num max number of recommendations
     * @param filename name of the file where to output recommended movie titles
     *                 Format of the file: one movie title per each line
     */
    public void findRecommendations(int userid, int num, String filename) {

        UserNode simUser = usersData.findMostSimilarUser(userid);
        int[] favMovies = simUser.getFavoriteMovies(num);
        UserNode myUser = usersData.get(userid);
        FileWriter favWriter;
        try {
            favWriter = new FileWriter(filename);

        } catch (IOException e) {
            return;
        }

        for (int i = 0; i < num; i++) {
            int currentId = favMovies[i];

            if (myUser.getUsersRating(currentId) == 0){
                if (simUser.getUsersRating(currentId) == 5){
                    String movieName = movieMap.get(currentId);
                    try {
                        favWriter.write(movieName + "\n");

                    }
                    catch (IOException e){
                        return;
                    }
                }
            }
        }
        try {
            favWriter.close();
        }catch (IOException e){

        }

        // compute similarity between userid and all the other users
        // find the most similar user and recommend movies that the most similar
        // user rated as 5.
        // Recommend only the movies that userid has not seen (has not
        // rated).
        // FILL IN CODE

    }

    /**
     * Computes up to num movie anti-recommendations for the user with the given
     * user id and prints these movie titles to the given file. These are the
     * movies the user should avoid. First calls findMostSimilarUser and then
     * getLeastFavoriteMovies(num) method on the "most similar user" to get up
     * to num movies the most similar user strongly disliked. Prints only
     * those movies to the file that the user with the given userid has not seen yet.
     * Format: one movie title per each line
     * @param userid id of the user
     * @param num max number of anti-recommendations
     * @param filename name of the file where to output anti-recommendations (movie titles)
     */
    public void findAntiRecommendations(int userid, int num, String filename) {

        UserNode simUser = usersData.findMostSimilarUser(userid);
        int[] notFavMovies = simUser.getLeastFavoriteMovies(num);

        UserNode myUser = usersData.get(userid);
        FileWriter favWriter;
        try {
            favWriter = new FileWriter(filename);

        } catch (IOException e) {
            return;
        }

        for (int i = 0; i < num; i++) {
            int currentId = notFavMovies[i];

            if (myUser.getUsersRating(currentId) == 0){
                if (simUser.getUsersRating(currentId) == 1){
                    String movieName = movieMap.get(currentId);
                    try {
                        favWriter.write(movieName + "\n");

                    }
                    catch (IOException e){
                        return;
                    }
                }
            }
        }
        try {
            favWriter.close();
        }catch (IOException e){

        }



        // compute similarity between userid and all the other users
        // find the most similar user and anti-recommend movies that the most similar
        // user rated as 1.
        // Anti-recommend only the movies that userid has not seen (has not
        // rated).
        // FILL IN CODE
    }

}