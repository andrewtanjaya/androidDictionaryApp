package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String idKey;
    private String id;
    private String username;
    private String email;
    private String language;
    private String photo;
    private int highscore;

    public User(){

    }
    public int getHighscore(){
        return highscore;
    }

    public void setHighscore(int highscore){
        this.highscore = highscore;
    }

    public String getIdKey(){
        return idKey;
    }

    public void setIdKey(String idKey){
        this.idKey = idKey;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getLanguage(){
        return language;
    }

    public void setLanguage(String language){
        this.language = language;
    }

    public String getPhoto(){
        return photo;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.photo);
        dest.writeString(this.language);
        dest.writeInt(this.highscore);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.email = in.readString();
        this.username = in.readString();
        this.photo = in.readString();
        this.language = in.readString();
        this.highscore = in.readInt();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


}
