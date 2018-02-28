package andrewang.hw2;

/**
 * Created by andrewang on 1/24/18.
 */

public class Contacts {
    //Return the resource ID representing the contact's name
    public int getmNameResId() {
        return mNameResId;
    }

    //Set the resource ID representing the contact's name
    public void setmNameResId(int mNameResId) {
        this.mNameResId = mNameResId;
    }

    //Return the resource ID representing the contact's photo
    public int getmImageResId() {
        return mImageResId;
    }

    //Set the resource ID representing the contact's photo
    public void setmImageResId(int mImageResId) {
        this.mImageResId = mImageResId;
    }

    // get the type of the pokemon
    public String getmType() {
        return mType;
    }

    // set the type of the pokemon to another type
    public void setmType(String mType) {
        this.mType = mType;
    }

    //Constructor for a contact sets its name, image, type, resources by parameter,
    // level is default 0 and type nickname is empty
    public Contacts(int nameResId, int imageResId, String type) {
        mNameResId = nameResId;
        mImageResId = imageResId;
        mType = type;
        mNickName = "";
        mLevel = 0;
    }

    // set the level of the pokemon
    public void setmLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    //get the level of the pokemon
    public int getmLevel() {
        return mLevel;
    }

    // get the nickname of the pokemon
    public String getmNickName() {
        return mNickName;
    }

    // change the nickname of the pokemon
    public void setmNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    //Refers to a string defined in strings.xml
    private int mNameResId; //not storing actual text here!
    private int mLevel;
    //Refers to an image defined in res/drawable (one image for all DPIs for now)
    private int mImageResId; //not storing actual images here!
    private String mNickName;
    // Refers to the type of the pokemon
    private String mType;
}
