package seedu.address.model.person;
import seedu.address.commons.exceptions.IllegalValueException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;

/**
 *  Each Person should have a photo
 */

public class Photo {

    public static final String DEFAULT_MESSAGE_PHOTO = "Filepath be less than 10MB and FilePath must be valid ";
    public static final String DEFAULT_PHOTO = "images/default_person.png";

    private static final int tenMB = 1048576;
    private static final String FOLDER = getOperatingPath();
    private static final String PHOTO_INTITAL_REGEX_ = "[^\\s].*";

    private String photoPath;

    public Photo() {
        this.photoPath = DEFAULT_PHOTO;
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
        requireNonNull(filePath);

        if (checkPath(filePath)) {
            throw new IllegalValueException(DEFAULT_MESSAGE_PHOTO);
        }
        //link to the path
        this.photoPath = FOLDER + "//" + newPhoto;
        makePhoto(filePath, newPhoto);
    }

    /**
     * Make a photo
     */
  
    private void makePhoto(String filePath, String newPhoto) {

        makePhotoFolder();


        //get image from source
        File getImage = new File(filePath);

        //create file object
        File pictureFinal = new File(FOLDER + "//" + newPhoto);
        
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
     * Make a photoFolder
     */

    public void makePhotoFolder() {

        File locationFolder = new File(FOLDER);

        if (!locationFolder.exists()) {
            locationFolder.mkdir();
        }

    }

    /**
     * Check Operating System of User
     */

    private static String getOperatingPath() {
        String oSystem = System.getProperty("os.name");

        //mac
        if (oSystem.contains("mac")) {
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
     * Get Photo Path
     */

    public String getPhoto() {
        return this.photoPath;
    }

    /**
     * Check Photo Path of User
     */

    public static boolean checkPath(String path) {

        if (path.equals(DEFAULT_PHOTO)) {
            return true;
        }

        if (path.matches(PHOTO_INTITAL_REGEX_)) {
            return checkPicture(path);

        }

        return false;

    }

    /**
     * Photo Validation
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

        if (pictureNew.length() > tenMB) {
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
