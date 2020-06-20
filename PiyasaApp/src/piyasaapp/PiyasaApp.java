/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piyasaapp;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.xml.sax.SAXException;


public class PiyasaApp extends JFrame {
    
public static PiyasaApp piyasaApp;    

BorsaTableModel tableModel ;  

List<Borsa> globalListe ;

JTable table2;

DefaultCategoryDataset grafikDegerler = new DefaultCategoryDataset();

JFreeChart jchart ; 


CategoryPlot plot ;

ValueAxis rangeAxis ;

TickUnits ticks ;

boolean kontrol ;

int selectedRow ;

Double ilkDeger;
 public PiyasaApp()  {
     
     globalListe = new ArrayList<>();
     tableModel = new BorsaTableModel();
     table2 = new JTable(tableModel);
     
     table2.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent me) {
             if(me.getClickCount()==1){
                 grafikDegerler.clear();
                 kontrol=true;
                 //tıklanılan satırı döndürüyor.
                  selectedRow = table2.getSelectedRow();
                
                 String grafikDegerString = globalListe.get(selectedRow).getDeger();
                 grafikDegerString = grafikDegerString.replace(",",".");
                 ilkDeger = Double.valueOf(grafikDegerString);        
                 
        
        jchart = ChartFactory.createLineChart(globalListe.get(selectedRow).getName()+" İçin Zaman-Değer Grafiği", "Zaman", "Değer", grafikDegerler,PlotOrientation.VERTICAL,true,true,false);
        grafikDegerler.setValue(ilkDeger, "Değer", globalListe.get(selectedRow).getSonGuncelleme());
        plot  = jchart.getCategoryPlot();
        
        plot.setRangeGridlinePaint(Color.black);
        
        ChartFrame chartFrame = new ChartFrame("Grafik Arayüzü", jchart,true);
        chartFrame.setVisible(true);
        chartFrame.setSize(1000, 800);    
             }
         }
         
     });
     table2.setRowHeight(30);
     table2.setFont(new Font("Verdana",Font.PLAIN,18));
     for (int i =0; i<tableModel.getColumnCount();i++) {
         table2.setDefaultRenderer(table2.getColumnClass(i), new BorsaCellRenderer());
      }
     JScrollPane scrollPane = new JScrollPane(table2);
     add(scrollPane);
     
     JTextArea textBilgi = new JTextArea("Grafikleri görmek için Listeden Seçim Yapınız.Grafikler Yeni Veri Geldikçe Çizilecektir.");
     textBilgi.setFont(new Font("Verdana",Font.BOLD,15));
     add(textBilgi,BorderLayout.SOUTH);
     
     JTextArea text = new JTextArea("(Yükselen Değerler Yeşil)  (Düşen Değerler Kırmızı) (Değişim Olmayanlar Sarı) İle Gösterilmiştir.");
     text.setFont(new Font("Verdana", Font.BOLD,15));
     add(text,BorderLayout.NORTH);
     
        setSize(1100, 500);
        setTitle("Xml Borsa Takibi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setVisible(true);
}
 
 public boolean dizilerEsitMi(List<Borsa>dizi1,List<Borsa>dizi2){
     boolean value = true;
            if(dizi1.size() == dizi2.size()){
                for(int i = 0 ; i<dizi1.size();i++){
                       if(dizi1.get(i).getName().equals(dizi2.get(i).getName()) && dizi1.get(i).getSembol().equals(dizi1.get(i).getSembol())&&dizi1.get(i).getDeger().equals(dizi2.get(i).getDeger())&&dizi1.get(i).getDegisim().equals(dizi2.get(i).getDegisim())&&dizi1.get(i).getSonGuncelleme().equals(dizi2.get(i).getSonGuncelleme())){
                           value = true;
                       }
                       else{
                           
                           value = false;
                           System.out.println("Yeni veriler geldi.");
                           return false;
                       }
                }   
                    } 
             else{
                 System.out.println("SİZE EŞİT OLMALI");
                 value = false;      
             }   
     return value;
 }
 
 public void grafik_cizdir(int selectedRow){  
                 String grafikDegerString = globalListe.get(selectedRow).getDeger();
                 grafikDegerString = grafikDegerString.replace(",",".");
                 Double grafikDeger = Double.valueOf(grafikDegerString);
                 
                 
                 
                 grafikDegerler.setValue(grafikDeger, "Değer", globalListe.get(selectedRow).getSonGuncelleme());

                     
                 
                 
                 if(ilkDeger < grafikDeger){
                     rangeAxis = plot.getRangeAxis();
                     rangeAxis.setRange(ilkDeger -0.002 ,grafikDeger + 0.002);
                 }
                 else{
                     rangeAxis = plot.getRangeAxis();
                     rangeAxis.setRange(grafikDeger-0.002,ilkDeger+0.002);
                 }
                 ilkDeger = grafikDeger;
 }
 
 public void dizi_isle(List<Borsa> empList){
        
          if(globalListe.isEmpty()){
        for(Borsa emp : empList){
                    
                    tableModel.listeyeEkle(emp);    
        }
            globalListe.clear();
            globalListe.addAll(0,empList);
        }
        else if(!globalListe.isEmpty()){
            if(!dizilerEsitMi(globalListe,empList)){
                globalListe.clear();
                globalListe.addAll(0,empList);
                
                tableModel.clearRows();
                for(Borsa emp : empList){
                tableModel.listeyeEkle(emp);
                }
                if(kontrol == true){
                grafik_cizdir(selectedRow);
                }          
            }          
        }
 }
 

public void get_response() {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            try {
        SAXParser saxParser = saxParserFactory.newSAXParser();
        Handler handler = new Handler();
        
        String url = "http://realtime.paragaranti.com/asp/xml/icpiyasaX.xml";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        saxParser.parse(con.getInputStream(), handler);
        //bundan sonrası listeyi parçalama ve sonraki işlemler. 
        //yani bu satırdan öncesinde urlden dosyayı çekmek için kullanılacak işlemler.

        //Get Employees list   
        List<Borsa> empList = handler.getEmpList();
        //tabloyu temizler. 
        
        dizi_isle(empList);
        
   
        
    } catch (ParserConfigurationException | SAXException | IOException e) {
        e.printStackTrace();
    }
    }
   
    public static void main(String[] args) {
        
        piyasaApp = new PiyasaApp();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                piyasaApp.get_response();       
            }
        };
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.SECONDS);      
}
    
   
    
   class BorsaCellRenderer extends DefaultTableCellRenderer {
   public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column)
   {
      BorsaTableModel btm = (BorsaTableModel) table.getModel();
      Borsa borsa = (Borsa) btm.getValueAtRow(row);
  
      if (Double.parseDouble(borsa.getDegisim()) > 0) {
         setBackground(Color.green);
      }
      else if(Double.parseDouble(borsa.getDegisim()) <0) {
         setBackground(Color.red);
      }
      else{
          setBackground(Color.yellow);
      }
  
      return super.getTableCellRendererComponent(table, value, isSelected,
                                                 hasFocus, row, column);
   }
   }
   
  public class BorsaTableModel extends AbstractTableModel {
   // holds the strings to be displayed in the column headers of our table
   final String[] columnNames = { "Sembol", 
"İsim", "Değer", "Değişim Oranı (%)", "Son Güncelleme" };
  
   // holds the data types for all our columns
   final Class[] columnClasses = {String.class, String.class, String.class, String.class, String.class};
  
   // holds our data
   final Vector data = new Vector();
   
   // adds a row
   public void listeyeEkle(Borsa w) {
      data.addElement(w);
      fireTableRowsInserted(data.size(), data.size());
   }
  
   public int getColumnCount() {
      return columnNames.length;
   }
          
   public int getRowCount() {
      return data.size();
   }
  
   public String getColumnName(int col) {
      return columnNames[col];
   }

   public void removeAllEntry(){
       data.clear();
       fireTableDataChanged();
   }
   
   public void clearRows(){
       data.clear();
       fireTableDataChanged();
   }
   
   public Object getValueAt(int row, int col) {
      Borsa borsa = (Borsa) data.elementAt(row);
      if (col == 0)      return borsa.getSembol();
      else if (col == 1) return borsa.getName();
      else if (col == 2) return borsa.getDeger();
      else if (col == 3) return borsa.getDegisim();
      else if (col == 4) return borsa.getSonGuncelleme();
      else return null;
   }
  
   public Object getValueAtRow(int row) {
      Borsa borsa = (Borsa) data.elementAt(row);
      return borsa;
   }
  
   public boolean isCellEditable(int row, int col) {
      return false;
   }
    }
  
}
