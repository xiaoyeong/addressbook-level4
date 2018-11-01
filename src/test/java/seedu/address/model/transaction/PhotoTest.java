package seedu.address.model.transaction;

import org.junit.Test;
import seedu.address.model.person.Photo;
import org.junit.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

public class PhotoTest {
    @Test
    public void makePhoto_successfullyCreatePhoto() {
        AtomicReference<Photo> newPhoto = new AtomicReference<>(new Photo());
        try {
            newPhoto.get().makePhotoFolder();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(new File(Photo.getOperatingPath()).exists());
        //Assert.assertThrows(IOException.class, () -> newPhoto.makePhoto(folderPath, newPhoto.getPicturePath()));
    }
}
