package seedu.address.model.person;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represent a photo object associated with each unique person involved in a transaction with the user.
 */
public class Photo {
    public static final String FOLDER_CONTAINING_THE_PHOTO_HAS_NOT_BEEN_CREATED_SUCCESSFULLY = "Folder containing the photo has not been created successfully";
    private static final String DEFAULT_MESSAGE_PHOTO = "Filepath be less than 10MB and FilePath must be valid ";
    private static final String DEFAULT_PHOTO = "images/default_person.png";
    public static String FOLDER = getOperatingPath();
    private static final String PHOTO_INITIAL_REGEX_ = "[^\\s].*";
    private static final int TENMB = 1048576;

    public static final Logger LOGGER = LogsCenter.getLogger(Photo.class);

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
        LOGGER.info("before photo");
        LOGGER.info(filePath);
        requireNonNull(filePath);

        if (checkPath(filePath)) {
            throw new IllegalValueException(DEFAULT_MESSAGE_PHOTO);
        }
        //link to the path
        this.photoPath = FOLDER + "/" + newPhoto + ".png";
        try {
            makePhoto(filePath, newPhoto);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidPhoto(String path) {
        return checkPath(path);
    }
    /**
     * Makes a {@code newPhoto} at the given {@code filePath} if it doesn't already exist.
     */
    public void makePhoto(String filePath, String newPhoto) throws FileNotFoundException {
            makePhotoFolder();
            LOGGER.info("makephoto");
            LOGGER.info(filePath);
            if (filePath.equals("delete")) {
                filePath = "images/default_person.png";
            } else {
                filePath = "/" + filePath;
            }
            File getImage = new File(filePath);
            String fileName = FOLDER + "/" + newPhoto + ".png";
            File pictureFinal = new File(fileName);
            //if cannot get file object create an empty object
            if (!pictureFinal.exists()) {
                try {
                    boolean doesNamedFileExist = pictureFinal.createNewFile();
                    if (doesNamedFileExist) {
                        LOGGER.info(fileName + " has been newly created");
                    } else {
                        LOGGER.info(fileName + " already exists");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        try {
            Files.copy(getImage.toPath(), pictureFinal.toPath(), REPLACE_EXISTING);
            this.photoPath = pictureFinal.toPath().toString();
        } catch (IOException e) {
            e.printStackTrace();
            this.photoPath = "images/default_person.png";
        }
    }

    /**
     * Creates a folder holding the photo for a person
     */
    public void makePhotoFolder() throws FileNotFoundException {
        File locationFolder = new File(FOLDER);
        if (!locationFolder.exists()) {
            boolean isDirectoryCreated = locationFolder.mkdir();
            if (isDirectoryCreated) {
                LOGGER.info("Folder containing the photo has been successfully created");
            } else {
                throw new FileNotFoundException(FOLDER_CONTAINING_THE_PHOTO_HAS_NOT_BEEN_CREATED_SUCCESSFULLY);
            }
        }
    }

    /**
     * Check the Operating System that the user's local machine is running
     */
    public static String getOperatingPath() {
        String oSystem = System.getProperty("os.name").toLowerCase();

        //mac
        if (oSystem.contains("mac") || oSystem.contains("nux")) {
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
    private static boolean checkPath(String path) {
        if (path.equals(DEFAULT_PHOTO)) {
            return true;
        }
        if (path.matches(PHOTO_INITIAL_REGEX_)) {
            return checkPicture(path);
        }

        return false;
    }

    /**
     * Checks whether the picture exists in the given path.
     */
    private static boolean checkPicture(String path) {
        File pictureNew = new File(path);
        try {
            if (ImageIO.read(pictureNew) == null) {
                return false;
            }
        } catch (IOException error) {
            return false;
        }

        return pictureNew.length() <= TENMB;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Photo // instanceof handles nulls
                && this.photoPath.equals(((Photo) other).photoPath));
    }
}
