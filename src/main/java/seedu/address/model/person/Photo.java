package seedu.address.model.person;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represent a photo object associated with each unique person involved in a transaction with the user.
 */
public class Photo {

    public static final String DEFAULT_MESSAGE_PHOTO = "Filepath be less than 10MB and FilePath must be valid ";
    public static final String DEFAULT_PHOTO = "images/default_person.png";

    private static final int TENMB = 1048576;
    private static final String FOLDER = getOperatingPath();
    private static final String PHOTO_INTITAL_REGEX_ = "[^\\s].*";

    private String photoPath;

    public Photo() {
        this.photoPath = DEFAULT_PHOTO;
    }

    public static boolean isValidPhoto(String path) {

        if (checkPath(path)) {
            return true;
        } else {
            return false;
        }

    }
    public Photo(String path) {
        requireNonNull(path);
        if (checkPath(path)) {
            this.photoPath = path;
        } else {
            this.photoPath = DEFAULT_PHOTO;
        }

    }

    public Photo(String filePath, String newPhoto) throws IllegalValueException {
        System.out.println("before photo");
        System.out.println(filePath);
        requireNonNull(filePath);

        if (checkPath(filePath)) {
            throw new IllegalValueException(DEFAULT_MESSAGE_PHOTO);
        }
        //link to the path
        this.photoPath = FOLDER + "/" + newPhoto+".png";
        System.out.println(FOLDER);
        makePhoto(filePath, newPhoto);
    }

    /**
     * Makes a {@code newPhoto} at the given {@code filePath} if it doesn't already exist.
     */
    private void makePhoto(String filePath, String newPhoto) {

        makePhotoFolder();

        System.out.println("makephoto");
        System.out.println(filePath);
        //get image from source
        String immm =  System.getProperty("user.home") + "/Documents/cs2103/debt-tracker/docs/images/weiqing-nic.png";
        File getImage = new File(filePath);
        //File getImage = new File(immm);


        //create file object
        File pictureFinal = new File(FOLDER + "/" + newPhoto+".png");

        //if cannot get file object create an empty object
        if (!pictureFinal.exists()) {
            try {
                pictureFinal.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.copy(getImage.toPath(), pictureFinal.toPath(), REPLACE_EXISTING);
            this.photoPath = pictureFinal.toPath().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a folder holding the photo for a person
     */
    public void makePhotoFolder() {
        File locationFolder = new File(FOLDER);
        if (!locationFolder.exists()) {
            locationFolder.mkdir();
        }
    }

    /**
     * Check the Operating System that the user's local machine is running
     */

    private static String getOperatingPath() {
        String oSystem = System.getProperty("os.name").toLowerCase();

        //mac
        if (oSystem.contains("mac") || oSystem.contains("nux")) {
            System.out.println(System.getProperty("user.home"));
            return System.getProperty("user.home") + "/Documents/cs2103/debt-tracker/PhotoFolder";
        } else {
            return System.getenv("APPDATA") + "/PhotoFolder";
        }
    }

    /**
     * Get Operating System of User
     */
    private static String getOsName() {
        return System.getProperty("os.name");
    }

    /**
     * Returns the path of the image file within the directory structure.
     */
    public String getPicturePath() {
        return this.photoPath;
    }

    /**
     * Checks whether the path of the given picture meets certain criteria.
     */
    public static boolean checkPath(String path) {
        System.out.println(path);
        if (path.equals(DEFAULT_PHOTO)) {
            System.out.println("lol");
            return true;
        }
        if (path.matches(PHOTO_INTITAL_REGEX_)) {
            return checkPicture(path);
        }
        System.out.println("lol");

        return false;
    }

    /**
     * Checks whether the picture exists in the given path.
     */
    public static boolean checkPicture(String path) {
        File pictureNew = new File(path);
        try {
            if (ImageIO.read(pictureNew) == null) {
                return false;
            }
        } catch (IOException error) {
            return false;
        }
        if (pictureNew.length() > TENMB) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Photo // instanceof handles nulls
                && this.photoPath.equals(((Photo) other).photoPath));
    }
}
