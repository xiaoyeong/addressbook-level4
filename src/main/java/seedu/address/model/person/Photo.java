package seedu.address.model.person;
import seedu.address.commons.exceptions.IllegalValueException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;


public class Photo {

    public static final String DEFAULT_MESSAGE_PHOTO = "Filepath be less than 10MB and FilePath must be valid ";
    public static final String DEFAULT_PHOTO = "images/default_person.png";
    private static final String FOLDER = getOperatingPath();
    //cannot be blank space
    //double space equals to one in java
    private static final String PHOTO_INTITAL_REGEX_ = "[^\\s].*";
    private static final int tenMB =  1048576;

    private String photoPath;


    public Photo(){
        this.photoPath = DEFAULT_PHOTO;
    }

    public Photo(String path){

        requireNonNull(path);

        if(checkPath(path)){
            this.photoPath = path;
        } else{
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
        
        makePhoto( filePath,   newPhoto);        
    }

    private void makePhoto(String filePath, String newPhoto) {

        makePhotoFolder();


        //get image from source
        File getImage = new File(filePath);

        //create file object
        File pictureFinal = new File(FOLDER + "//" + newPhoto);


       //if cannot get file object create an empty object


        if (!pictureFinal.exists() ) {

            try {
                pictureFinal.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {

            Files.copy(getImage.toPath(), pictureFinal.toPath(), REPLACE_EXISTING);
            this.photoPath =pictureFinal.toPath().toString();
        } catch (IOException e){
            e.printStackTrace();

        }

    }


    public void makePhotoFolder(){

        File locationFolder = new File(FOLDER);

        if ( !locationFolder.exists()) {
            locationFolder.mkdir();
        }

    }




    private static String getOperatingPath(){
        String oSystem  = System.getProperty("os.name");

        //mac
        if (oSystem.contains("mac")){
            return System.getProperty("user.home") +"/Documents/cs2103/debt-tracker/PhotoFolder";
        }
        //windows
        else{
            return System.getenv("APPDATA")+"/PhotoFolder";
        }
    }

    private static String getOsName(){
        return System.getProperty("os.name");
    }


    public String getPicturePath(){
        return this.photoPath;
    }

    public static boolean checkPath(String path){

        if(path.equals(DEFAULT_PHOTO)){
            return true;
        }

        if( path.matches(PHOTO_INTITAL_REGEX_) ){
                return checkPicture(path);
        }

        return false;

    }

    public static boolean checkPicture(String path){


        File pictureNew = new File(path);

        try{
            if (ImageIO.read(pictureNew) == null)
                return false;

        } catch (IOException error){
            return false;
        }

        if (pictureNew.length() > tenMB){
            return false;
        }

        return true;



    }





}
