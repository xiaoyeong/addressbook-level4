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

    public static final String DEFAULT_PHOTO_PATH = Photo.class.getResource("/PhotoTest/weiqing.png").getPath();
    private static final String MESSAGE_PHOTOFOLDER_NOT_SUCCESSFULLY_CREATED = "PhotoFolder created successfully";
    private static final String DEFAULT_MESSAGE_PHOTO = "Filepath be less than 10MB and FilePath must be valid ";

    private static final String PHOTO_INITIAL_REGEX = "[^\\s].*";
    private static final int TENMB_SIZE = 1048576;
    private static final String SYSTEM_OPERATING_PATH = "PhotoFolder";
    private static final Logger LOGGER = LogsCenter.getLogger(Photo.class);


    private String photoPath;

    public Photo() {
        this.photoPath = DEFAULT_PHOTO_PATH;
    }

    public Photo(String path) {
        requireNonNull(path);
        if (checkPath(path)) {
            this.photoPath = path;
        } else {
            this.photoPath = DEFAULT_PHOTO_PATH;
            this.photoPath = getClass().getResource("/PhotoTest/weiqing.png").getPath();
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
        this.photoPath = SYSTEM_OPERATING_PATH + "/" + newPhoto + ".png";
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
        String finalPath = filePath;
        makePhotoFolder();
        LOGGER.info("makephoto");
        LOGGER.info(filePath);
        if ("delete".equals(filePath)) {
            finalPath = "images/default_person.png";
        } else {
            finalPath = "/" + filePath;
        }

        File getImage = new File(finalPath);
        String fileName = SYSTEM_OPERATING_PATH + "/" + newPhoto + ".png";
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
        File locationFolder = new File(SYSTEM_OPERATING_PATH);
        if (!locationFolder.exists()) {
            boolean isDirectoryCreated = locationFolder.mkdir();
            if (isDirectoryCreated) {
                LOGGER.info("Folder containing the photo has been successfully created");
            } else {
                throw new FileNotFoundException(MESSAGE_PHOTOFOLDER_NOT_SUCCESSFULLY_CREATED);
            }
        }
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
        if (path.equals(DEFAULT_PHOTO_PATH)) {
            return true;
        }
        if (path.matches(PHOTO_INITIAL_REGEX)) {
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

        return pictureNew.length() <= TENMB_SIZE;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Photo // instanceof handles nulls
                && this.photoPath.equals(((Photo) other).photoPath));
    }
}
