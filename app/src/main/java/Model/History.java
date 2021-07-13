package Model;

import android.os.Parcel;
import android.os.Parcelable;

public class History implements Parcelable {
    private String historyId;
    private String word;
    private String def;
    private String antonym;
    private String synonym;
    private String userKey;

    public History(){

    }

    protected History(Parcel in) {
        historyId = in.readString();
        word = in.readString();
        def = in.readString();
        antonym = in.readString();
        synonym = in.readString();
        userKey = in.readString();
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getWord() {
        return word;
    }

    public String getDef() {
        return def;
    }

    public String getAntonym() {
        return antonym;
    }

    public String getSynonym() {
        return synonym;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(historyId);
        dest.writeString(word);
        dest.writeString(def);
        dest.writeString(antonym);
        dest.writeString(synonym);
        dest.writeString(userKey);
    }
    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
}
