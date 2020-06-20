/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piyasaapp;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class Handler extends DefaultHandler{
    //model sınıfından objeleri tutacak liste oluşturulacak.
    private List<Borsa> empList = null;
    private Borsa emp = null;
    private StringBuilder data = null;
    
    //getter method for employee list 
    public List <Borsa> getEmpList() {
        return empList;
    }
        
        boolean bSembol = false;
	boolean bİsim = false;
        boolean bDeger = false;
	boolean bDegisim = false;
	boolean bSonGuncelleme = false;
        
        
        //burası handler in xml dosyasını parçalayıp verileri çekmesi.
       @Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("STOCK")) {
			// create a new Employee and put it in Map
			// initialize Employee object and set id attribute
			emp = new Borsa();
			// initialize list
			if (empList == null)
				empList = new ArrayList<>();
		}
                else if(qName.equalsIgnoreCase("SYMBOL")){
                    bSembol = true;
                }
                else if (qName.equalsIgnoreCase("DESC")) {
			// set boolean values for fields, will be used in setting Employee variables
			bİsim = true;
		} else if (qName.equalsIgnoreCase("LAST")) {
			bDeger = true;
		} else if (qName.equalsIgnoreCase("PERNC_NUMBER")) {
			bDegisim = true;
		} else if (qName.equalsIgnoreCase("LAST_MOD")) {
			bSonGuncelleme = true;
		}
		// create the data container
		data = new StringBuilder();
	}
        
        //burası da handlerin çektiği verileri data stringbuilderından alıp setlemesi.
        @Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (bDeger) {
			// age element, set Employee age
			emp.setDeger(data.toString());
			bDeger = false;
		} else if (bİsim) {
			emp.setName(data.toString());
			bİsim = false;
		} else if (bSonGuncelleme) {
			emp.setSonGuncelleme(data.toString());
			bSonGuncelleme = false;
		} else if (bDegisim) {
			emp.setDegisim(data.toString());  //burası değiştirelecek float.
			bDegisim = false;
		}
                else if (bSembol) {
                        emp.setSembol(data.toString());
                        bSembol = false;
                }
		if (qName.equalsIgnoreCase("STOCK")) {
			// add Employee object to list
			empList.add(emp);
		}
	}
        
        @Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
        
}
