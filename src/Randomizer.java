import java.util.Random;

// class to create random strings used as internal IDs for patients
// format for intID will be 2 letters (fname, lname) + 8 random numbers
public class Randomizer {

	private final static String candidateChars = "0123456789";
	private static int length = 8;
	
	public static String generateRandomCh(){
		StringBuilder sb = new StringBuilder();
		Random rnd = new Random();
		for (int i = 0; i < length; i++){
			sb.append(candidateChars.charAt(rnd.nextInt(candidateChars.length())));
		}
		return sb.toString();
	}
}
