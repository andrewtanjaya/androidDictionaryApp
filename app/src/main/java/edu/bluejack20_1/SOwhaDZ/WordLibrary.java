package edu.bluejack20_1.SOwhaDZ;

import java.util.Random;

public class WordLibrary {
    private String words[] = {
            "Car",
            "Women",
            "Baby",
            "Father",
            "Mother",
            "Love",
            "Watch",
            "Genius",
            "Hate",
            "Symphony",
            "Sick",
            "Shy",
            "Ice",
            "Save",
            "Hear",
            "Know",
            "Hold",
            "Hug",
            "Say",
            "Same",
            "Numb",
            "Pain",
            "Day",
            "Blood",
            "Fall",
            "All",
            "Guard",
            "Pull",
            "Believe",
            "Enough",
            "Rewrite",
            "Discount",
            "Apply",
            "Cart",
            "Mail",
            "Fortitude",
//            "Cognizant",
//            "Steadfast",
//            "Scrupulous",
//            "Utterly",
//            "Nuance",
//            "Unflappable",
//            "Mercurial",
//            "Frivolous",
//            "Augur",
//            "Denouement",
//            "Expeditious",
//            "Chagrin",
//            "Innocuous",
//            "Belie",
//            "Doldrums",
//            "Scunner",
//            "Indigenous",
//            "Roister",
//            "Newfangled",
//            "Discommode",
//            "Idiolect",
//            "Zinger",
//            "Asunder",
//            "Finagle",
//            "Capacious",
//            "Leitmotif",
//            "Susurration",
//            "Ecoanxiety",
//            "Avow",
//            "Illation",
//            "Supernumerary",
//            "Whillikers",
//            "Cachinnate",
//            "Butyraceous",
//            "Caducous"

    };

    private String[] kata ={
            "Saya","Kamu","Teman","Kaki","Sama","Banyak Orang","Gunung","Rumah","Pelindung","Pukul","Adjektiva",
            "Akurat","Akselerasi","Antagonis","Artis","Bergaul","File","Generik","Global","Harmoni","Hiperbola",
            "Heroisme","Heterogen","Identitas","Impulsif","Instruktur","Jeda","Kedap","Komedi","Kondusif","Penting",
            "Kuno","Laba","Lokasi","Mistis","Ralat","Sandang","Sanksi","Semboyan","Sinkron","Sosialisasi"
    };


    public String getRandomwordEnglish(){
        Random rand = new Random();
        return words[rand.nextInt(words.length)];
    }

    public int getIndexEnglish(String s){
        for(int i=0;i<words.length;i++){
            if(words[i].equals(s)){
                return i;
            }
        }

        return -1;
    }

    public String getRandomWordIndo(){
        Random rand = new Random();
        return kata[rand.nextInt(kata.length)];
    }

    public int getIndexIndo(String s){
        for(int i=0;i<kata.length;i++){
            if(kata[i].equals(s)){
                return i;
            }
        }

        return -1;
    }
}