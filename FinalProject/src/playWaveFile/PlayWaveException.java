package playWaveFile;

/**
 * @author Giulio
 */
public class PlayWaveException extends Exception {

	private static final long serialVersionUID = 5670775667409108192L;

	public PlayWaveException(String message) {
	super(message);
    }

    public PlayWaveException(Throwable cause) {
	super(cause);
    }

    public PlayWaveException(String message, Throwable cause) {
	super(message, cause);
    }

}
