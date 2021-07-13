package edu.bluejack20_1.SOwhaDZ;

public class WordAnswer {
    private String answer[] ={
            "Mobil",
            "Perempuan",
            "Bayi",
            "Ayah",
            "Ibu",
            "Cinta",
            "Jam Tangan",
            "Jenius",
            "Benci",
            "Simfoni",
            "Sakit",
            "Malu",
            "Es",
            "Selamat",
            "Mendengar",
            "Tahu",
            "Tahan",
            "Memeluk",
            "Mengatakan",
            "Sama",
            "Mati Rasa",
            "Sakit",
            "Hari",
            "Darah",
            "Jatuh",
            "Semua",
            "Penjaga",
            "Tarik",
            "Percaya",
            "Cukup",
            "Menulis Ulang",
            "Diskon",
            "Menerapkan",
            "Gerobak",
            "Surat",
            "Ketabahan"
    };

    public String[] jawaban ={
            "Me","You","Friend","Foot","Together","Crowded","Mountain","Home","Protector","Hit","Adjective",
            "Accurate","Acceleration","Antagonist","Artist","Get Along","File","General","World","Harmony","Hyperbole",
            "Heroic","Heterogeneous","Identity","Impulsive","Instructor","Pause","Impermeable","Comedy","Condusive","Important",
            "Ancient","Profit","Location","Mistical","Ralat","Clothing","Penalty","Motto","In Syn","Socialization"
    };

    public String getAnswerEnglish(int index){
        return answer[index];
    }
    public String getAnswerIndo(int index){
        return jawaban[index];
    }
}