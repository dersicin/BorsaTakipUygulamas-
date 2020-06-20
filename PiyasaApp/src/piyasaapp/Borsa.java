/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piyasaapp;

/**
 *
 * @author lenovo
 */
public class Borsa {
    private String sembol;
    private String isim;
    private String deger;
    private String degisim; //pozitif-negatif degisim,virgüllü
    private String sonGuncelleme;
    
  
    
     public String getSembol() {
        return sembol;
    }
    public void setSembol(String sembol) {
        this.sembol = sembol;
    }
    public String getName() {
        return isim;
    }
    public void setName(String isim) {
        this.isim = isim;
    }
    public String getDeger() {
        return deger;
    }
    public void setDeger(String deger) {
        this.deger = deger;
    }
    public String getDegisim() {
        return degisim;
    }
    public void setDegisim(String degisim) {
        this.degisim = degisim;
    }
    public String getSonGuncelleme() {
        return sonGuncelleme;
    }
    public void setSonGuncelleme(String sonGuncelleme) {
        this.sonGuncelleme = sonGuncelleme;
    }
    
    //BU KISIM OBJEYİ DİREK STRİNGE CEVİRİYOR.
    @Override
    public String toString() {
        return "Borsa:: Sembol= "+this.sembol+" İsim= " + this.isim + " Değer= " + this.deger + " Değişim= " + this.degisim +
                " Son Güncelleme= " + this.sonGuncelleme;
    }
    
}

